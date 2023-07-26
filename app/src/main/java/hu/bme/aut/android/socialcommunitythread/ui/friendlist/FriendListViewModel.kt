package hu.bme.aut.android.socialcommunitythread.ui.friendlist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class FriendListViewModel @Inject constructor() : ViewModel() {
    private val coroutineScope = MainScope()

    private val _viewState: MutableStateFlow<FriendListViewState> = MutableStateFlow(FriendListViewState())
    val viewState = _viewState.asStateFlow()

    private val _oneShotEvents = Channel<FriendListOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    fun onAction(friendListUiAction: FriendListUiAction){
        when (friendListUiAction){

            else -> {}
        }
    }
}