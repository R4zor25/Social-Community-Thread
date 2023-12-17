package hu.bme.aut.android.socialcommunitythread.domain.interactors

import hu.bme.aut.android.socialcommunitythread.domain.auth.AuthRequest
import hu.bme.aut.android.socialcommunitythread.domain.dto.AppUser
import hu.bme.aut.android.socialcommunitythread.domain.dto.RefreshTokenRequest
import hu.bme.aut.android.socialcommunitythread.domain.dto.UserRequest
import hu.bme.aut.android.socialcommunitythread.network.BackendService
import hu.bme.aut.android.socialcommunitythread.network.FirebaseDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val backendService: BackendService,
) {
    private val IoDispatcher: CoroutineDispatcher = Dispatchers.IO
    companion object {
        var currentLoggedInUser: AppUser? = null
    }
    suspend fun register(username: String, password: String, email: String, callback: (String) -> Unit) {
        backendService.register(UserRequest().apply {
            this.username = username
            this.password = password
            this.email = email
        }) {
            callback.invoke(it)
        }
    }

    suspend fun login(username: String, password: String, callback: (String) -> Unit) {
        backendService.login(AuthRequest().apply {
            this.username = username
            this.password = password
        }) { errorMessage, appuser ->
            if (errorMessage.isBlank())
                currentLoggedInUser = appuser
            callback.invoke(errorMessage)
        }
    }

    suspend fun validateToken(token: String, callback: (String) -> Unit) {
        backendService.validateToken(token) {
            callback.invoke(it)
        }
    }

    suspend fun getAccessToken(refreshTokenRequest: RefreshTokenRequest, callback: (String) -> Unit) {
        backendService.getAccessToken(refreshTokenRequest) { errorMessage, appuser ->
            if (errorMessage.isBlank())
                currentLoggedInUser = appuser
            callback.invoke(errorMessage)
        }
    }

    suspend fun getAllUser(callback: (String, List<AppUser>?) -> Unit) {
        backendService.getAllUser() { errorMessage, userList ->
            callback.invoke(errorMessage, userList)
        }
    }

    suspend fun getUserByUsername(username: String, callback: (String, AppUser?) -> Unit){
        backendService.getUserByUsername(username){ errorMessage, user ->
            callback.invoke(errorMessage, user)
        }
    }

    suspend fun getUserById(id: Long, callback: (String, AppUser?) -> Unit){
        backendService.getUserById(id){ errorMessage, user ->
            callback.invoke(errorMessage, user)
        }
    }

    suspend fun updateUser(user: AppUser, callback: (String) -> Unit){
        backendService.updateUser(AuthInteractor.currentLoggedInUser!!.userId ,user){ errorMessage, appUser ->
            if(errorMessage.isBlank())
                currentLoggedInUser = appUser
            callback.invoke(errorMessage)
        }
    }
}