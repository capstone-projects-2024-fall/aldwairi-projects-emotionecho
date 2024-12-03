package com.temple.aldwairi_projects_emotionecho.ui.screens

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chaquo.python.Python
import com.google.gson.Gson
import com.temple.aldwairi_projects_emotionecho.ui.components.CustomButton
import com.temple.aldwairi_projects_emotionecho.ui.components.PieChartWithLegend
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.content.ContextCompat
import android.Manifest
import android.util.Log
import com.chaquo.python.PyObject
import com.temple.aldwairi_projects_emotionecho.MainActivity
import com.temple.aldwairi_projects_emotionecho.ui.components.AnimatedEmotionField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealTimeModeScreen(
    context: Context,
    modifier: Modifier
) {
    // State variables
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var option by rememberSaveable { mutableStateOf("") }
    var isRecording by rememberSaveable { mutableStateOf(false) }
    val python = Python.getInstance()
    val microphoneChoice = listOf("internal", "external")
    val floatList = rememberSaveable { mutableStateOf<List<Float>?>(null) }
    var loading by remember { mutableStateOf(false) } // Loading state

    // Audio recording variables (global within the composable)
    var audioRecord: AudioRecord? = remember { mutableStateOf<AudioRecord?>(null).value }
    var recordingThread: Thread? = remember { mutableStateOf<Thread?>(null).value }

    // State for Emotion and Color
    var currentEmotion by remember { mutableStateOf("No Emotion Yet...") }

    fun initializeFloatList(floatListParam: List<Float>) {
        floatList.value = floatListParam
    }

    fun stopRecording() {
        recordingThread?.interrupt()
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
        recordingThread = null
        isRecording = false
        Log.d("TESTING", "Recording stopped and resources released.")
    }

    fun startRecording(context: Context, python: Python) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            val sampleRate = 44100
            val bufferSize = AudioRecord.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
            if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
                Log.e("AUDIO_RECORDING", "Invalid buffer size")
                return
            }

            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            )

            // Start coroutine to load the Python module
            loading = true // Show spinner
            lateinit var acceptAudio: PyObject
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    acceptAudio = python.getModule("MainThread")
                    Log.d("Initialization", "Loaded Python module: $acceptAudio")
                } catch (e: Exception) {
                    Log.e("Initialization", "Error loading Python module: ${e.message}")
                } finally {
                    loading = false // Hide spinner
                }
            }

            audioRecord?.startRecording()
            Log.d("AUDIO_RECORDING", "Recording started")
            if (audioRecord?.recordingState != AudioRecord.RECORDSTATE_RECORDING) {
                Log.e("AUDIO_RECORDING", "Failed to start recording")
                return
            }

            val audioDataList = mutableListOf<Byte>()
            val emotionArrayList = ArrayList<String>()

            recordingThread = Thread {
                try {
                    val buffer = ByteArray(bufferSize)
                    while (!Thread.interrupted()) {
                        val readBytes = audioRecord?.read(buffer, 0, bufferSize) ?: 0
                        if (readBytes > 0) {
                            audioDataList.addAll(buffer.take(readBytes))
                        }
                        if (audioDataList.size >= sampleRate * 3 * 2) {
                            val audioData = audioDataList.toByteArray()

                            val emotion = acceptAudio.callAttr("process_and_classify", audioData)
                            emotionArrayList.add(emotion.toString())
                            val objList = python.getModule("resultProcess").callAttr("get_emotions_percentage",
                                Gson().toJson(emotionArrayList))
                            initializeFloatList(objList.asList().map { it.toString().toFloat() })

                            // Update current emotion and color based on the emotion result
                            currentEmotion = emotion.toString().replaceFirstChar { it.uppercase() }

                            audioDataList.clear()
                        }
                    }
                    acceptAudio.callAttr("clear_dir")

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            recordingThread?.start()
            isRecording = true
        } else {
            if (context is MainActivity) {
                context.requestAudioPermission()
            }
        }
    }

    fun toggleRecording() {
        if (isRecording) {
            stopRecording()
        } else {
            startRecording(context, python)
        }
    }

    Surface(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            floatList.value?.let { PieChartWithLegend(it) }

            // Animated field to display emotion and color
            Spacer(modifier = Modifier.height(20.dp))
            AnimatedEmotionField(
                emotion = currentEmotion
            )

            Spacer(modifier = Modifier.height(20.dp))

            ExposedDropdownMenuBox(
                expanded = true,
                onExpandedChange = { isExpanded = !isExpanded }
            ) {
                TextField(
                    readOnly = true,
                    value = option,
                    onValueChange = {},
                    label = { Text("Select a microphone") },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, enabled = true)
                )

                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    for (mic in microphoneChoice) {
                        DropdownMenuItem(
                            text = { Text(mic) },
                            onClick = {
                                option = mic
                                isExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            CustomButton(
                if (isRecording) "Stop Recording" else "Start Recording and Analyzing",

            ) {
                Toast.makeText(context, "Analyzing started using $option mic", Toast.LENGTH_LONG).show()
                toggleRecording()
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Show spinner while loading
            if (loading) {
                androidx.compose.material3.CircularProgressIndicator(
                    modifier = Modifier.padding(16.dp),
                    color = Color.Blue
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRealTimeModeScreen() {
    val mockContext = LocalContext.current
    RealTimeModeScreen(mockContext, Modifier.padding(20.dp))
}


