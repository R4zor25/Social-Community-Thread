package hu.bme.aut.android.socialcommunitythread.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDataSource @Inject constructor() {
    private var db = Firebase.firestore
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUserEmail(): String {
        return firebaseAuth.currentUser?.email!!
    }

    fun getCurrentUserName(): String? {
        return firebaseAuth.currentUser?.displayName
    }

    suspend fun createUser(email: String, userName: String, password: String): String {
        var message: String = ""
        val isTaken = isUserNameTaken(userName)
        if (isTaken) {
            return "Username already taken!"
        }
        try {
            firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    val firebaseUser = result.user
                    val profileChangeRequest = UserProfileChangeRequest.Builder()
                        .setDisplayName(userName)
                        .build()
                    firebaseUser?.updateProfile(profileChangeRequest)
                    addUserToDatabase(userName, email)
                    message = "Registration successful!"
                }
                .addOnFailureListener { exception ->
                    message = exception.localizedMessage ?: "Something went wrong!"
                }.await()

        } catch (e: Exception) {
            return e.localizedMessage
        }

        return message
    }

    private suspend fun isUserNameTaken(userName: String): Boolean {
        val query = db.collection("Users")
            .whereEqualTo("username", userName).get().await()
        return !query.isEmpty

    }

    private fun addUserToDatabase(userName: String?, email: String) {
        val data = hashMapOf(
            "username" to userName,
            "friends" to emptyList<String>(),
            "email" to email,
            "stocks" to emptyList<String>()
        )
        db.collection("Users").add(data)
    }

    private suspend fun getUserEmail(userName: String): String {
        val query = db.collection("Users")
            .whereEqualTo("username", userName).get().await()
        if (query.isEmpty)
            throw RuntimeException("No such user")
        return query.documents[0]?.get("email") as String
    }

    suspend fun login(email: String, password: String): String {
        var message: String = "Login successful!"
        try {
            firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    message = "Login successful!"
                }
                .addOnFailureListener { exception ->
                    message = exception.localizedMessage ?: "Cannot reach login server!"
                }.await()
        } catch (e: Exception) {
            return e.localizedMessage
        }
        return message
    }

}