package com.temple.aldwairi_projects_emotionecho

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage

class DataBaseEE {

    private val database = Firebase.firestore
    private val FirebaseAuth = Firebase.auth
    private val storage = Firebase.storage

    fun createUser(firstName: String, lastName : String, email: String, password: String, onSuccess: (FirebaseUser, User) -> Unit, onFailure: (Exception) -> Unit){
        FirebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{task ->
                if(task.isSuccessful){
                    val userMap = hashMapOf(
                        User.USERID to FirebaseAuth.currentUser!!.uid,
                        User.FIRST_NAME to firstName,
                        User.LAST_NAME to lastName,
                        User.EMAIL_ADDRESS to email
                    )

                    val user = User(
                        FirebaseAuth.currentUser!!.uid,
                        firstName,
                        lastName,
                        email
                    )

                    database.collection(User.USER_TABLE)
                        .add(userMap)
                        .addOnSuccessListener {
                            Log.d(TAG_FIREAUTH, "User Created with ID $it")
                            onSuccess(FirebaseAuth.currentUser!!, user)
                        }
                        .addOnFailureListener{ e ->
                            Log.w(TAG_FIREAUTH, "Error adding document", e)
                            onFailure(e)
                        }
                } else {
                    onFailure(task.exception!!)
                }
            }
    }
    fun signIn(email: String, password: String, onSuccess: (FirebaseUser) -> Unit, onFailure: (Exception) -> Unit
    ){
        FirebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    Log.d(TAG_FIREAUTH, "signInWithEmail:success")
                    onSuccess(FirebaseAuth.currentUser!!)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG_FIREAUTH, "signInWithEmail:failure", task.exception)
                    onFailure(task.exception!!)
                }
            }
    }
    fun signOut(){
        FirebaseAuth.signOut()
    }










    companion object{
        private val TAG_FIRESTORE = "FIRESTORE"
        private val TAG_FIREAUTH = "FIREAUTH"
        private val TAG_STORAGE = "STORAGE"
    }
}