package com.temple.aldwairi_projects_emotionecho


import android.util.Log
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException

class WAVCompressoranduploader {

    private val storage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }

    /**
     * Compresses a WAV file by reducing its bitrate.
     * @param originalFile The original WAV file.
     * @return A compressed WAV file.
     * @throws IOException if compression fails.
     */
    @Throws(IOException::class)
    fun compressWAVFile(originalFile: File, onCompletion: (File?, Exception?) -> Unit) {
        val compressedFile = File(originalFile.parent, "compressed_${originalFile.name}")
        val command = "-i \"${originalFile.absolutePath}\" -b:a 128k \"${compressedFile.absolutePath}\""

        FFmpegKit.executeAsync(command) { session ->
            val returnCode = session.returnCode
            if (ReturnCode.isSuccess(returnCode)) {
                Log.d(TAG, "Compression successful: ${compressedFile.absolutePath}")
                onCompletion(compressedFile, null)
            } else {
                val error = IOException("FFmpeg compression failed with return code: $returnCode")
                Log.e(TAG, "Compression failed", error)
                onCompletion(null, error)
            }
        }
    }

    /**
     * Uploads a file to Firebase Storage.
     * @param file The file to upload.
     * @param username The username for folder creation in Firebase Storage.
     * @param onSuccess A lambda expression called on successful upload.
     * @param onFailure A lambda expression called on upload failure.
     */

    fun wavFileToByteArray(wavFilePath: String): ByteArray? {
        return try {
            val wavFile = File(wavFilePath)
            if (wavFile.exists()) {
                wavFile.readBytes()
            } else {
                println("File not found: $wavFilePath")
                null
            }
        } catch (e: IOException) {
            println("Error reading file: ${e.message}")
            null
        }
    }

    /**
     * Uploads a byte array to Firebase Storage.
     * @param wavFileToByteArray The byte array to upload.
     * @param fileName The name of the file in Firebase Storage.
     * @param username The username for folder creation in Firebase Storage.
     * @param onSuccess A lambda expression called on successful upload.
     * @param onFailure A lambda expression called on upload failure.
     */
    fun uploadByteArrayToFirebaseStorage(
        wavFileToByteArray: ByteArray,
        fileName: String,
        username: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val storageRef: StorageReference = storage.reference
        val userFolderRef = storageRef.child("$username/$fileName")

        userFolderRef.putBytes(wavFileToByteArray)
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
