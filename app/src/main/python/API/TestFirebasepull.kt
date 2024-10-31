import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File

class MainActivity : AppCompatActivity() {

    // Firebase Storage reference
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Storage
        storage = Firebase.storage

        // File path in Firebase Storage
        val filePath = "path/in/firebase/storage/file.txt"  // Replace with your actual file path in Firebase Storage
        downloadFile(filePath)
    }

    private fun downloadFile(filePath: String) {
        // Reference to the file in Firebase Storage
        val storageRef = storage.reference.child(filePath)

        // Local file where the downloaded file will be saved
        val localFile = File.createTempFile("downloadedFile", ".txt")

        // Download the file
        storageRef.getFile(localFile)
            .addOnSuccessListener {
                Toast.makeText(this, "File downloaded successfully", Toast.LENGTH_SHORT).show()
                Log.d("FirebaseStorage", "File downloaded to: ${localFile.absolutePath}")
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Download failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.e("FirebaseStorage", "Download failed", exception)
            }
    }
}