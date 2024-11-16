package com.temple.aldwairi_projects_emotionecho

import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class WAVCompressoranduploader {

    private val storage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }

    /**
     * Compresses a WAV file and uploads it to Firebase Storage.
     * @param wavFile The WAV file to compress.
     * @param username The username to create a folder in Firebase Storage.
     * @param onSuccess A lambda expression called on successful upload.
     * @param onFailure A lambda expression called on upload failure.
     */
    fun compressAndUploadWAV(
        wavFile: File,
        username: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // Step 1: Compress the WAV file
        try {
            val compressedFile = compressWAVFile(wavFile)

            // Step 2: Upload the compressed file
            uploadToFirebaseStorage(compressedFile, username, onSuccess, onFailure)
        } catch (e: Exception) {
            Log.e(TAG, "Error during WAV compression or upload: ", e)
            onFailure(e)
        }
    }

    /**
     * Compresses a WAV file by reducing its bitrate.
     * @param originalFile The original WAV file.
     * @return A compressed WAV file.
     * @throws IOException if compression fails.
     */
    @Throws(IOException::class)
    private fun compressWAVFile(originalFile: File): File {
        val compressedFile = File(originalFile.parent, "compressed_${originalFile.name}")

        // Placeholder for actual compression logic (bitrate reduction, etc.)
        FileInputStream(originalFile).use { inputStream ->
            FileOutputStream(compressedFile).use { outputStream ->
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }
            }
        }

        Log.d(TAG, "Compressed file created at: ${compressedFile.absolutePath}")
        return compressedFile
    }

    /**
     * Uploads a file to Firebase Storage.
     * @param file The file to upload.
     * @param username The username for folder creation in Firebase Storage.
     * @param onSuccess A lambda expression called on successful upload.
     * @param onFailure A lambda expression called on upload failure.
     */
    private fun uploadToFirebaseStorage(
        file: File,
        username: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val storageRef: StorageReference = storage.reference
        val userFolderRef = storageRef.child("$username/${file.name}")

        userFolderRef.putFile(file.toURI().toURL().toUri())
            .addOnSuccessListener {
                Log.d(TAG, "File successfully uploaded to: $userFolderRef")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "File upload failed: ", e)
                onFailure(e)
            }
    }

    companion object {
        private const val TAG = "WAVCompressoranduploader"
    }
}
