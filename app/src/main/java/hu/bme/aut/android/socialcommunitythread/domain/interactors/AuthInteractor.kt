package hu.bme.aut.android.socialcommunitythread.domain.interactors

import hu.bme.aut.android.socialcommunitythread.network.FirebaseDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {
    private val IoDispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun createUser(email: String, userName: String, password: String): String = withContext(IoDispatcher) {
        firebaseDataSource.createUser(email, userName, password)
    }

    suspend fun login(email: String, password: String): String = withContext(IoDispatcher) {
        firebaseDataSource.login(email, password)
    }

    fun getCurrentUserName(): String? {
        return firebaseDataSource.getCurrentUserName()
    }

    fun getEmail(): String {
        return firebaseDataSource.getCurrentUserEmail()
    }
}