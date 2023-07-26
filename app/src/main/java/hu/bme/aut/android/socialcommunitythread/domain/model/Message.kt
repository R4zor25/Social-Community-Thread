package hu.bme.aut.android.socialcommunitythread.domain.model

data class Message(
    val senderId: Int,
    val messageText: String,
    val sentDate: String
)
