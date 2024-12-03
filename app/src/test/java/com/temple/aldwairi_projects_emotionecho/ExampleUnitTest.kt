package com.temple.aldwairi_projects_emotionecho

import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.io.IOException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @Test
    fun `compressWAVFile should compress WAV successfully`() {
        val originalFile = File("path/to/test.wav") // Use a sample WAV file.
        val compressorUploader = WAVCompressoranduploader()

        compressorUploader.compressWAVFile(originalFile) { compressedFile, error ->
            assertNotNull(compressedFile)
            assertNull(error)
            assertTrue(compressedFile!!.exists())
            assertTrue(compressedFile.length() < originalFile.length()) // Check size reduction.
        }
    }
    @Test
    fun `compressWAVFile should fail for non-WAV input`() {
        val nonWavFile = File("path/to/test.mp3")
        val compressorUploader = WAVCompressoranduploader()

        compressorUploader.compressWAVFile(nonWavFile) { compressedFile, error ->
            assertNull(compressedFile)
            assertNotNull(error)
            assertTrue(error is IOException)
        }
    }
    @Test
    fun `wavFileToByteArray should return byte array for valid file`() {
        val validFile = File("path/to/test.wav")
        val compressorUploader = WAVCompressoranduploader()

        val byteArray = compressorUploader.wavFileToByteArray(validFile.absolutePath)
        assertNotNull(byteArray)
        assertTrue(byteArray!!.isNotEmpty())
    }
    @Test
    fun `wavFileToByteArray should return null for missing file`() {
        val missingFile = "path/to/nonexistent.wav"
        val compressorUploader = WAVCompressoranduploader()

        val byteArray = compressorUploader.wavFileToByteArray(missingFile)
        assertNull(byteArray)
    }
    @Test
    fun `uploadByteArrayToFirebaseStorage should succeed`() {
        val byteArray = ByteArray(1024) { 0 } // Mock byte array.
        val compressorUploader = WAVCompressoranduploader()
        val username = "testUser"
        val fileName = "testFile.wav"

        compressorUploader.uploadByteArrayToFirebaseStorage(
            byteArray,
            fileName,
            username,
            onSuccess = { assertTrue(true) },
            onFailure = { fail("Upload should not fail") }
        )
    }
    @Test
    fun `uploadByteArrayToFirebaseStorage should fail with invalid data`() {
        val byteArray = ByteArray(0) // Empty byte array.
        val compressorUploader = WAVCompressoranduploader()
        val username = "testUser"
        val fileName = "testFile.wav"

        compressorUploader.uploadByteArrayToFirebaseStorage(
            byteArray,
            fileName,
            username,
            onSuccess = { fail("Upload should fail for empty data") },
            onFailure = { assertTrue(true) }
        )
    }
















}