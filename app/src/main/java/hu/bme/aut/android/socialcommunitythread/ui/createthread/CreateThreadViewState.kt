package hu.bme.aut.android.socialcommunitythread.ui.createthread

import hu.bme.aut.android.socialcommunitythread.domain.model.ThreadPublicity
import hu.bme.aut.android.socialcommunitythread.domain.model.TopicThread

data class CreateThreadViewState(
    val isLoading: Boolean = false,
    val threadName: String = "",
    val threadImage: ByteArray = byteArrayOf(),
    val description: String = "",
    val isPublic : Boolean = true
)

sealed class CreateThreadOneShotEvent {
    data class ShowToastMessage(val errorText: String) : CreateThreadOneShotEvent()
    class ThreadCreated : CreateThreadOneShotEvent()
}

