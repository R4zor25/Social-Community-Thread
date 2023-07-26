package hu.bme.aut.android.socialcommunitythread.domain.auth

interface AuthRepository {
    suspend fun signUp(username: String, email: String, password: String): LoginResult<Unit>
    suspend fun signIn(username: String, password: String): LoginResult<Unit>
    suspend fun authenticate(): LoginResult<Unit>
}