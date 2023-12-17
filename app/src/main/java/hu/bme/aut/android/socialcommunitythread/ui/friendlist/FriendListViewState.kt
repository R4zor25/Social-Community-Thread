package hu.bme.aut.android.socialcommunitythread.ui.friendlist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import hu.bme.aut.android.socialcommunitythread.domain.dto.AppUser

data class FriendListViewState(
    val isLoading: Boolean = false,
    val friendList : SnapshotStateList<AppUser> = mutableStateListOf(),
    val incomingList : SnapshotStateList<AppUser> = mutableStateListOf(),
    val outgoingList : SnapshotStateList<AppUser> = mutableStateListOf(),
    var usersList : SnapshotStateList<AppUser> = mutableStateListOf(),
)

sealed class FriendListOneShotEvent {
    data class ShowToastMessage(val errorText: String) : FriendListOneShotEvent()
}