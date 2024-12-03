package com.temple.aldwairi_projects_emotionecho

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.temple.aldwairi_projects_emotionecho", appContext.packageName)
    }
    @Test
    fun uploadByteArrayToFirebaseStorage() {
        val byteArray = ByteArray(1024) { 0 } // Mock byte array.
        val compressorUploader = WAVCompressoranduploader()
        val username = "testUser"
        val fileName = "testFile.wav"

        compressorUploader.uploadByteArrayToFirebaseStorage(
            byteArray,
            fileName,
            username,
            onSuccess = {
                // Validate in Firebase console if needed.
                assertTrue(true)
            },
            onFailure = { fail("Firebase upload should not fail") }
        )
    }
    @Test
    fun compressanduploadWAVfileendtoend() {
        val originalFile = File("path/to/test.wav")
        val compressorUploader = WAVCompressoranduploader()
        val username = "testUser"
        val fileName = "compressed_test.wav"

        compressorUploader.compressWAVFile(originalFile) { compressedFile, error ->
            assertNotNull(compressedFile)
            assertNull(error)

            compressedFile?.let {
                val byteArray = compressorUploader.wavFileToByteArray(it.absolutePath)
                assertNotNull(byteArray)

                compressorUploader.uploadByteArrayToFirebaseStorage(
                    byteArray!!,
                    fileName,
                    username,
                    onSuccess = { assertTrue(true) },
                    onFailure = { fail("End-to-end process should succeed") }
                )
            }
        }
    }







}