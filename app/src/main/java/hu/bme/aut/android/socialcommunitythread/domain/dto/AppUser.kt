package hu.bme.aut.android.socialcommunitythread.domain.dto

data class AppUser(
    var userId: Long = 0,
    var name: String = "",
    var userName: String = "",
    var email: String = "",
    var profileImage : ByteArray = byteArrayOf()
)
