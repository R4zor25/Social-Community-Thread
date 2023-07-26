package hu.bme.aut.android.socialcommunitythread.domain.model

data class ChatDetails(
    var id: Int,
    var chatParticipants: MutableList<String>,
    var messageList: MutableList<Message>
)