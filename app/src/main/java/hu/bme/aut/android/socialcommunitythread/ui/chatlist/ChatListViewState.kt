package hu.bme.aut.android.socialcommunitythread.ui.chatlist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import hu.bme.aut.android.socialcommunitythread.domain.model.PersonalChatConversation

data class ChatListViewState(
    val isLoading: Boolean = false,
    var items: SnapshotStateList<PersonalChatConversation> = mutableStateListOf())

sealed class ChatListOneShotEvent{
    data class ShowToastMessage(val errorText: String): ChatListOneShotEvent()
}