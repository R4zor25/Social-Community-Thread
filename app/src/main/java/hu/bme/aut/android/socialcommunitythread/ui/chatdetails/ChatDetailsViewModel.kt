package hu.bme.aut.android.socialcommunitythread.ui.chatdetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialcommunitythread.domain.repository.Repository
import hu.bme.aut.android.socialcommunitythread.ui.chatlist.ChatListOneShotEvent
import hu.bme.aut.android.socialcommunitythread.ui.chatlist.ChatListUiAction
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatDetailsViewModel @Inject constructor() : ViewModel() {
    private val coroutineScope = MainScope()

    var viewState by mutableStateOf(ChatDetailsViewState())

    private val _oneShotEvents = Channel<ChatListOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    private val repository = Repository

    init {
        viewModelScope.launch {

        }
    }

    fun onAction(chatDetailsUiAction: ChatListUiAction){
        when (chatDetailsUiAction){

            else -> {}
        }
    }

}