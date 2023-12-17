package hu.bme.aut.android.socialcommunitythread.domain.dto

data class UserRequest(
    var username: String = "",
    var password: String = "",
    var email: String = ""
)