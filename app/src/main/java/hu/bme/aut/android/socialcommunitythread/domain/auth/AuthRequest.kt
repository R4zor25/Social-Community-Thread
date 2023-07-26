package hu.bme.aut.android.socialcommunitythread.domain.auth

data class AuthRequest(
    val username: String,
    val email: String,
    val password: String
)
