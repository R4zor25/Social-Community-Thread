package hu.bme.aut.android.socialcommunitythread.domain.model

import hu.bme.aut.android.socialcommunitythread.domain.dto.AppUser
import hu.bme.aut.android.socialcommunitythread.domain.dto.ChatMessage
import java.util.Date

data class PersonalChatConversation(
    var id: Long? = null,
    var conversationName: String = "",
    var creationDate: Date = Date(),
    var chatCreator: AppUser = AppUser(),
    var chatParticipants: List<AppUser> = listOf(),
    var conversationImage: ByteArray? = byteArrayOf(),
    var messageList: List<ChatMessage> = listOf(),
    var lastMessageDate: Date = Date()
    )