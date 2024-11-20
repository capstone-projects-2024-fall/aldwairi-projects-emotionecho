package com.temple.aldwairi_projects_emotionecho

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.temple.aldwairi_projects_emotionecho.ui.navigation.App
import com.temple.aldwairi_projects_emotionecho.ui.navigation.EmotionEchoApp
import com.temple.aldwairi_projects_emotionecho.ui.navigation.EmotionEchoAppRouter
import com.temple.aldwairi_projects_emotionecho.ui.navigation.EmotionEchoLoginApp
import com.temple.aldwairi_projects_emotionecho.ui.theme.AldwairiprojectsemotionechoTheme

// Define your request code for clarity
const val REQUEST_CODE_RECORD_AUDIO = 1

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Python if not already started
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }

        // Set up the Composables
        setContent {
            AldwairiprojectsemotionechoTheme {
                Crossfade(
                    targetState = EmotionEchoAppRouter.currentApp, label = ""
                ) { currentApp ->
                    when (currentApp.value) {
                        App.Main -> {
                            // Pass `this` (MainActivity) as context so that the Composable can use it for permissions
                            EmotionEchoApp(this)
                        }

                        App.Login -> EmotionEchoLoginApp(this)
                    }
                }
            }
        }
    }

    // Function to request audio recording permission
    fun requestAudioPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_CODE_RECORD_AUDIO
            )
        }
    }

    // Handle permission result here
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_RECORD_AUDIO) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return
            } else {
                // Permission denied; notify the user
                Toast.makeText(this, "Audio permission is required to record.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}