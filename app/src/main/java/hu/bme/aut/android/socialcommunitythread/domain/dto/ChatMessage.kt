package hu.bme.aut.android.socialcommunitythread.domain.dto

import java.util.Date

data class ChatMessage(
    var id : Long? = null,
    var author : AppUser = AppUser(),
    var messageText: String = "",
    var sentDate: Date = Date()
)