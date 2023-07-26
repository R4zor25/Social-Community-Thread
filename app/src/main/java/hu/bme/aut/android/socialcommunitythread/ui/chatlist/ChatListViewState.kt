package hu.bme.aut.android.socialcommunitythread.ui.chatlist

import hu.bme.aut.android.socialcommunitythread.domain.model.ChatPreview

data class ChatListViewState(
    val isLoading: Boolean = false,
    var items: List<ChatPreview> = emptyList())

sealed class ChatListOneShotEvent{
    data class ShowToastMessage(val errorText: String): ChatListOneShotEvent()
}

sealed class ChatListUiAction{

}