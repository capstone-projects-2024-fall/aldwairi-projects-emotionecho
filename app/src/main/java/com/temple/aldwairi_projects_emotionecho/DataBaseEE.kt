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
    fun updateUser(
        user: User,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ){
        val newUserData = hashMapOf(
            User.USERID to user.userID,
            User.FIRST_NAME to user.firstName,
            User.LAST_NAME to user.lastName,
            User.EMAIL_ADDRESS to user.email
        )
        getUserByID(
            user.userID!!,
            { userDocument, _ ->
                database.collection(User.USER_TABLE)
                    .document(userDocument!!.id)
                    .set(newUserData, SetOptions.merge())
                onSuccess()
            },
            onFailure
        )
    }

    /**
     * Retrieves all user records from the database.
     * @param onSuccess A lambda expression that receives the QuerySnapshot result.
     * @param onFailure A lambda expression called upon failure to get the table.
     * @return void
     */
    fun getUserTable(
        onSuccess: (QuerySnapshot?) -> Unit,
        onFailure: () -> Unit
    ) {
        database.collection(User.USER_TABLE)
            .get()
            .addOnSuccessListener { result ->
                onSuccess(result)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG_FIRESTORE, "Error getting data.", exception)
                onFailure()
            }
    }

    /**
     * Retrieves a user record from the database by user ID.
     * @param userID The ID of the user to retrieve.
     * @param onSuccess A lambda expression called with the QueryDocumentSnapshot upon success.
     * @param onFailure A lambda expression called upon failure to retrieve the user.
     * @return void
     */
    fun getUserByID(
        userID: String,
        onSuccess: (QueryDocumentSnapshot?, User?) -> Unit,
        onFailure: () -> Unit
    ) {
        getUserTable(
            { users: QuerySnapshot? ->  // Explicitly specify the type of `users`
                if (users != null) {
                    for (document in users) {
                        if (document.data[User.USERID] == userID) {
                            onSuccess(document, document.toObject<User>())
                            return@getUserTable
                        }
                    }
                }
                onFailure() // Call onFailure if no match is found
            },
            {
                onFailure() // Handle the failure case
            }
        )
    }

    /**
     * @param user The ID of the user to delete.
     * @param onSuccess A lambda expression called with the Task on success.
     * @param onFailure A lambda expression called upon failure to delete the user.
     * @return void
     */
    fun removeUser(
        userID: String,
        onSuccess: (Task<Void>) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        getUserByID(
            userID,
            { document, _ ->
                database.collection(User.USER_TABLE)
                    .document(document!!.id)
                    .delete()
                    .addOnCompleteListener(onSuccess)
                    .addOnFailureListener(onFailure)
            },
            {
                //Need to handle this later but should not be a problem
            }
        )
    }

    /**
     * Gets the currently signed in user
     * @return The firebase user or null
     */
    fun getCurrentUser(): FirebaseUser?{
        return FirebaseAuth.currentUser
    }










    companion object{
        private val TAG_FIRESTORE = "FIRESTORE"
        private val TAG_FIREAUTH = "FIREAUTH"
        private val TAG_STORAGE = "STORAGE"
    }
}