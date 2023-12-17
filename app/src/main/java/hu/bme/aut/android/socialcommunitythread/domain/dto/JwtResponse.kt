package hu.bme.aut.android.socialcommunitythread.domain.dto

data class JwtResponse(
    val accessToken : String,
    val token : String,
    val user: AppUser
)