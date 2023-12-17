package hu.bme.aut.android.socialcommunitythread.ui.chatdetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialcommunitythread.domain.dto.ChatMessage
import hu.bme.aut.android.socialcommunitythread.domain.interactors.ChatInteractor
import hu.bme.aut.android.socialcommunitythread.domain.interactors.FriendInteractor
import hu.bme.aut.android.socialcommunitythread.ui.chatlist.ChatListOneShotEvent
import hu.bme.aut.android.socialcommunitythread.ui.postdetails.PostDetailsOneShotEvent
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
class ChatDetailsViewModel @Inject constructor(
    private val chatInteractor: ChatInteractor,
    private val friendInteractor: FriendInteractor
) : ViewModel() {
    private val coroutineScope = MainScope()

    private val viewState: MutableStateFlow<ChatDetailsViewState> = MutableStateFlow(ChatDetailsViewState())
    val uiState = viewState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        viewState.value
    )

    private val _oneShotEvents = Channel<ChatDetailsOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    private var chatConversationId : Long? = null

    init {
        coroutineScope.launch(Dispatchers.IO) {
            _oneShotEvents.send(ChatDetailsOneShotEvent.AcquireId)
        }
    }

    fun initChatConversationId(id: Long){
        chatConversationId = id
    }

    fun getFriendList(){
        coroutineScope.launch(Dispatchers.IO) {
            friendInteractor.getAllFriends { errorMessage, friendList ->
                coroutineScope.launch(Dispatchers.IO) {
                    if(errorMessage.isBlank() && friendList != null){
                        viewState.update { it.copy(isLoading = false, friendList = friendList.toMutableStateList()) }
                    } else {
                        _oneShotEvents.send(ChatDetailsOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun getChatConversationDetails(){
        coroutineScope.launch(Dispatchers.IO) {
            chatInteractor.getChatConversation(chatConversationId!!) { errorMessage, chatConversation ->
                coroutineScope.launch(Dispatchers.IO) {
                    if(errorMessage.isBlank() && chatConversation != null){
                        viewState.update { it.copy(isLoading = false, chatConversation = chatConversation) }
                    } else {
                        _oneShotEvents.send(ChatDetailsOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun sendMessage(chatMessage: String) {
        coroutineScope.launch(Dispatchers.IO) {
            chatInteractor.sendMessage(chatConversationId!!, ChatMessage().apply {
                this.messageText = viewState.value.currentText
            }) { errorMessage, message ->
                coroutineScope.launch(Dispatchers.IO) {
                    if(errorMessage.isBlank()){
                        viewState.update { it.copy(chatConversation = it.chatConversation.copy(messageList = it.chatConversation.messageList + message!!), currentText = "") }
                    } else {
                        _oneShotEvents.send(ChatDetailsOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun onTextChange(text: String){
        viewState.update { it.copy(currentText = text) }
    }

    fun addParticipant(userId: Long) {
        coroutineScope.launch(Dispatchers.IO) {
            chatInteractor.addParticipants(chatConversationId!!, listOf(userId)) { errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if(errorMessage.isBlank()){
                        viewState.update { it.copy(isLoading = false) }
                    } else {
                        _oneShotEvents.send(ChatDetailsOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun removeParticipant(userId: Long) {
        coroutineScope.launch(Dispatchers.IO) {
            chatInteractor.removeParticipants(chatConversationId!!, listOf(userId)) { errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if(errorMessage.isBlank()){
                        viewState.update { it.copy(isLoading = false) }
                    } else {
                        _oneShotEvents.send(ChatDetailsOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

}