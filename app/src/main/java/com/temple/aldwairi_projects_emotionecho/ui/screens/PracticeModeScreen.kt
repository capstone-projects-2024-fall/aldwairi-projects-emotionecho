package com.temple.aldwairi_projects_emotionecho.ui.screens

import android.content.Context
import android.widget.Toast
import android.util.Log
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.temple.aldwairi_projects_emotionecho.ui.components.CustomButton
import com.temple.aldwairi_projects_emotionecho.ui.theme.AldwairiprojectsemotionechoTheme
import android.media.MediaPlayer
import androidx.compose.runtime.LaunchedEffect
import com.google.common.reflect.TypeToken
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import java.io.File
import java.io.InputStream
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeModeScreen(
    context: Context,
    modifier: Modifier
){
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var option by rememberSaveable { mutableStateOf("") }
    var feedback by rememberSaveable { mutableStateOf("") }
    var currentFile by rememberSaveable { mutableStateOf("") }
    var correctEmotion by rememberSaveable { mutableStateOf("") }

    val emotions = listOf("Happy", "Sad", "Angry", "Neutral", "Fear", "Disgust")

    val jsonFileName = "metadata2.json"
    val metadata = remember { loadMetadata(context, jsonFileName) }

    // Selects a random audio file and its corresponding emotion
    fun selectFile() {
        if (metadata.isNotEmpty()) {
            val randomEntry = metadata[Random.nextInt(metadata.size)]
            currentFile = randomEntry["FileName"] ?: "Unknown File"
            correctEmotion = randomEntry["Emotion"] ?: "Unknown Emotion"
            Log.d("PracticeMode", "Selected file: $currentFile with emotion: $correctEmotion")
        }
    }

    Surface(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            ExposedDropdownMenuBox(
                expanded = true,
                onExpandedChange = { isExpanded = !isExpanded }
            ) {
                TextField(
                    readOnly = true,
                    value = option,
                    onValueChange = {},
                    label = { Text("Select an element")},
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, enabled = true)
                )
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    for (emotion in emotions){
                        DropdownMenuItem(
                            text = {Text(emotion)},
                            onClick = {
                                option = emotion
                                isExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier=Modifier.height(50.dp))

            // Button for submitting user response
            CustomButton(
                "SUBMIT RESPONSE"
            ) {
                if (option.equals(correctEmotion, ignoreCase = true)) {
                    feedback = "Correct! The emotion is $correctEmotion."
                } else {
                    feedback = "Incorrect. The correct emotion was $correctEmotion."
                }
                // Automatically picks a new random file if user submits a response
                selectFile()
                Toast.makeText(context, feedback, Toast.LENGTH_LONG).show()
            }
            Spacer(modifier=Modifier.height(20.dp))

            // Button for playing randomly chosen audio file
            CustomButton(
                "PLAY"
            ) {
                if (currentFile.isNotEmpty()) {
                    var count = 0
                    val max = 5

                    fun tryPlay() {
                        val storageReference =
                            FirebaseStorage.getInstance().reference.child("PracticeMode_WavFiles/$currentFile")
                        val localFile = File.createTempFile("random_audio", ".wav")

                        storageReference.getFile(localFile).addOnSuccessListener {
                            val mediaPlayer = MediaPlayer()
                            mediaPlayer.setDataSource(localFile.absolutePath)

                            mediaPlayer.setOnPreparedListener {
                                mediaPlayer.start()
                                Log.d("PracticeMode", "Playing: $currentFile")
                                //Toast.makeText(context, "Playing $currentFile", Toast.LENGTH_LONG).show()
                            }

                            mediaPlayer.setOnErrorListener { mp, what, extra ->
                                if (count < max) {
                                    count++
                                    selectFile()
                                    tryPlay()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Unable to play any files.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                true
                            }

                            mediaPlayer.prepareAsync()

                            mediaPlayer.setOnCompletionListener {
                                mediaPlayer.release()
                                Log.d("PracticeMode", "Playback complete")
                            }
                        }.addOnFailureListener { e ->
                            Log.e("PracticeMode", "Error downloading audio: ${e.message}")
                            if (count < max) {
                                count++
                                selectFile()
                                tryPlay()
                                //Toast.makeText(context, "Error downloading audio", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    tryPlay()

                } else {
                    Toast.makeText(context, "No file selected", Toast.LENGTH_LONG).show()
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Button for selecting a new file
            CustomButton("NEW AUDIO") {
                selectFile()
            }

        }
        // Initialize the first file selection
        LaunchedEffect(Unit) {
            selectFile()
        }
    }
}

// load JSON data
fun loadMetadata(context: Context, fileName: String): List<Map<String, String>> {
    return try {
        val inputStream: InputStream = context.assets.open(fileName)
        val json = inputStream.bufferedReader().use { it.readText() }
        val gson = Gson()
        val type = object : TypeToken<List<Map<String, String>>>() {}.type
        gson.fromJson(json, type)
    } catch (e: Exception) {
        Log.e("PracticeMode", "Error loading JSON: ${e.message}")
        emptyList()
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewPracticeModeScreen(){
    AldwairiprojectsemotionechoTheme(darkTheme = true) {
        // Use LocalContext if available, or a default fallback for Preview
        val mockContext = LocalContext.current
        PracticeModeScreen(mockContext, Modifier.padding(20.dp))
    }
}