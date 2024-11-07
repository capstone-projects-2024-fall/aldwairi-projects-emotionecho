package com.temple.aldwairi_projects_emotionecho.ui.screens

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import android.Manifest
import android.provider.MediaStore.Audio
import android.util.Log
import com.temple.aldwairi_projects_emotionecho.MainActivity

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

    // Audio recording variables (global within the composable)
    var audioRecord: AudioRecord? = remember { mutableStateOf<AudioRecord?>(null).value }
    var recordingThread: Thread? = remember { mutableStateOf<Thread?>(null).value }

    fun initializeFloatList(floatListParam: List<Float>) {
        floatList.value = floatListParam
    }
    fun isInitialize(): Boolean{
        return floatList.value != null
    }

    fun stopRecording() {
        // Stop and release the recording thread and AudioRecord
        recordingThread?.interrupt()
        audioRecord?.stop()
        audioRecord?.release()

        // Clear the references
        audioRecord = null
        recordingThread = null

        // Set recording state to false
        isRecording = false

        Log.d("TESTING", "Recording stopped and resources released.")
    }

    fun startRecording(context: Context, python: Python) {
        // Check permission before starting
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {

            val sampleRate = 44100
            val bufferSize = AudioRecord.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )

            // Initialize AudioRecord
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            )

            val acceptAudio = python.getModule("MainThread")
            var audioBuffer = ByteArray(sampleRate * 6)
            audioRecord?.startRecording()

            // Start a new thread for recording audio
            recordingThread = Thread {
                try {
                    var totalBytesRead = 0
                    while (!Thread.interrupted()) {
                        val readBytes = audioRecord?.read(audioBuffer, 0, bufferSize) ?: 0
                        if (readBytes > 0) {
                            totalBytesRead += readBytes
                        }
                        if (totalBytesRead >= sampleRate * 6){
                            acceptAudio.callAttr("audio_manager.processChunk", audioBuffer)
                            totalBytesRead = 0
                            audioBuffer = ByteArray(sampleRate * 6)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            recordingThread?.start()

            // Set recording state to true
            isRecording = true
        } else {
            // If permission is not granted, request permission
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
                listOf(Color.Black, Color.Gray)
            ) {
                Toast.makeText(context, "Analyzing started using $option mic", Toast.LENGTH_LONG).show()

                //change to display result screen
                val objectList = python.getModule("resultProcess").callAttr("get_emotions_percentage", Gson().toJson(arrayListOf(1,2,3,4,5,6,6,7)))
                initializeFloatList( objectList.asList().map { it.toString().toFloat() } )

                toggleRecording()
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
