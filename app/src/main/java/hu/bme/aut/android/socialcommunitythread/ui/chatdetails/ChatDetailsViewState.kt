package hu.bme.aut.android.socialcommunitythread.ui.chatdetails

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import hu.bme.aut.android.socialcommunitythread.domain.dto.AppUser
import hu.bme.aut.android.socialcommunitythread.domain.model.PersonalChatConversation
import hu.bme.aut.android.socialcommunitythread.domain.model.PersonalCommentModel

data class ChatDetailsViewState(
    val isLoading: Boolean = false,
    val chatConversation: PersonalChatConversation = PersonalChatConversation(),
    val currentText : String = "",
    val friendList: SnapshotStateList<AppUser> = mutableStateListOf(),
)

sealed class ChatDetailsOneShotEvent{
    data class ShowToastMessage(val errorText: String): ChatDetailsOneShotEvent()
    object AcquireId : ChatDetailsOneShotEvent()
}