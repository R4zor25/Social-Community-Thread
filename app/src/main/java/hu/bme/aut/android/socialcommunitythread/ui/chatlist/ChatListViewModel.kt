package hu.bme.aut.android.socialcommunitythread.ui.chatlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialcommunitythread.domain.repository.Repository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor() : ViewModel() {
    private val coroutineScope = MainScope()

    var viewState by mutableStateOf(ChatListViewState())

    private val _oneShotEvents = Channel<ChatListOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    private val repository = Repository

    init {
        viewModelScope.launch {
            val list = repository.getAllChat()
            viewState = viewState.copy(isLoading = false, items = list)
        }
    }


    fun onAction(chatListUiAction: ChatListUiAction){
        when (chatListUiAction){

            else -> {}
        }
    }
}