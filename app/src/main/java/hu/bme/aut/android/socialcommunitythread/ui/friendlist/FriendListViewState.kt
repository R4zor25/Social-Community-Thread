package hu.bme.aut.android.socialcommunitythread.ui.friendlist

import hu.bme.aut.android.socialcommunitythread.ui.mainthread.MainThreadOneShotEvent

data class FriendListViewState(val isLoading: Boolean = false)

sealed class FriendListOneShotEvent{
    data class ShowToastMessage(val errorText: String): FriendListOneShotEvent()
}

sealed class FriendListUiAction{

}