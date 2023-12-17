package hu.bme.aut.android.socialcommunitythread.ui.chatlist


import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialcommunitythread.domain.interactors.AuthInteractor
import hu.bme.aut.android.socialcommunitythread.domain.interactors.ChatInteractor
import hu.bme.aut.android.socialcommunitythread.domain.model.PersonalChatConversation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val chatInteractor: ChatInteractor,
    private val authInteractor: AuthInteractor
) : ViewModel() {
    private val coroutineScope = MainScope()

    private val viewState: MutableStateFlow<ChatListViewState> = MutableStateFlow(ChatListViewState())
    val uiState = viewState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        viewState.value
    )

    private val _oneShotEvents = Channel<ChatListOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()


    init {
        getAllChatConversationForUser()
    }

    fun getAllChatConversationForUser(){
        coroutineScope.launch(Dispatchers.IO)  {
            chatInteractor.getAllChatConversationForUser(){ errorMessage, chatList ->
                coroutineScope.launch(Dispatchers.IO) {
                    if(errorMessage.isBlank() && chatList != null){
                        viewState.update { it.copy(isLoading = false, items = chatList.toMutableStateList()) }
                    } else {
                        _oneShotEvents.send(ChatListOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun createChatConversation(chatConversation: PersonalChatConversation){
        coroutineScope.launch(Dispatchers.IO)  {
            chatConversation.apply {
                this.chatCreator = AuthInteractor.currentLoggedInUser!!
            }
            chatInteractor.createChatConversation(chatConversation){ errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if(errorMessage.isBlank()){
                        getAllChatConversationForUser()
                    } else {
                        _oneShotEvents.send(ChatListOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }

    }
}