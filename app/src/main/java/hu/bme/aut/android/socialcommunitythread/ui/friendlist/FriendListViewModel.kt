package hu.bme.aut.android.socialcommunitythread.ui.friendlist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialcommunitythread.domain.dto.AppUser
import hu.bme.aut.android.socialcommunitythread.domain.interactors.AuthInteractor
import hu.bme.aut.android.socialcommunitythread.domain.interactors.FriendInteractor
import hu.bme.aut.android.socialcommunitythread.ui.mainthread.MainThreadViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendListViewModel @Inject constructor(
    val authInteractor: AuthInteractor,
    val friendInteractor: FriendInteractor
) : ViewModel() {
    private val coroutineScope = MainScope()

    private val viewState: MutableStateFlow<FriendListViewState> = MutableStateFlow(FriendListViewState())
    val uiState = viewState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        viewState.value
    )

    private val _oneShotEvents = Channel<FriendListOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    init {
        coroutineScope.launch(Dispatchers.IO) {
            async { getFriendList() }.await()
            async { getIncomingFriendRequest() }.await()
            async { getOutgoingFriendRequest() }.await()
            async { getAllUsers() }.await()
        }
    }

    fun getAllUsers(){
        coroutineScope.launch(Dispatchers.IO) {
            authInteractor.getAllUser { errorMessage, appUsers ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        viewState.update { it.copy(usersList = appUsers?.toMutableStateList() ?: mutableStateListOf<AppUser>()) }
                    } else {
                        _oneShotEvents.send(FriendListOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun getFriendList() {
        coroutineScope.launch(Dispatchers.IO) {
            viewState.update { it.copy(isLoading = true) }
            friendInteractor.getAllFriends() { errorMessage, friends ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        viewState.update { it.copy(friendList = friends?.toMutableStateList() ?: mutableStateListOf<AppUser>())
                        }
                    } else {
                        _oneShotEvents.send(FriendListOneShotEvent.ShowToastMessage(errorMessage))
                    }
                    viewState.update { it.copy(isLoading = false) }}
                }
            }
        }

    fun getIncomingFriendRequest() {
        coroutineScope.launch(Dispatchers.IO) {
            viewState.update { it.copy(isLoading = true) }
            friendInteractor.getIncomingFriendRequest() { errorMessage, friends ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        viewState.update { it.copy(incomingList = friends?.toMutableStateList() ?: mutableStateListOf<AppUser>())}
                    } else {
                        _oneShotEvents.send(FriendListOneShotEvent.ShowToastMessage(errorMessage))
                    }
                    viewState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun getOutgoingFriendRequest() {
        coroutineScope.launch(Dispatchers.IO) {
            friendInteractor.getOutgoingFriendRequest() { errorMessage, friends ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        viewState.update { it.copy(outgoingList = friends?.toMutableStateList() ?: mutableStateListOf<AppUser>())}
                    } else {
                        _oneShotEvents.send(FriendListOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun addFriend(user: AppUser) {
        coroutineScope.launch(Dispatchers.IO) {
            friendInteractor.sendFriendRequest(user.userId) { errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        getOutgoingFriendRequest()
                    } else {
                        _oneShotEvents.send(FriendListOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun acceptFriend(user: AppUser) {
        coroutineScope.launch(Dispatchers.IO) {
            friendInteractor.acceptRequest(user.userId) { errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        getFriendList()
                    } else {
                        _oneShotEvents.send(FriendListOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun declineFriend(user: AppUser) {
        coroutineScope.launch(Dispatchers.IO) {
            friendInteractor.declineRequest(user.userId) { errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        getIncomingFriendRequest()
                    } else {
                        _oneShotEvents.send(FriendListOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun revokeFriend(user: AppUser) {
        coroutineScope.launch(Dispatchers.IO) {
            friendInteractor.revokeRequest(user.userId) { errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        getOutgoingFriendRequest()
                    } else {
                        _oneShotEvents.send(FriendListOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun deleteFriend(user: AppUser) {
        coroutineScope.launch(Dispatchers.IO) {
            friendInteractor.deleteFriend(user.userId) { errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        getFriendList()
                    } else {
                        _oneShotEvents.send(FriendListOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun filterUserList() {
        var userList = viewState.value.usersList
        userList = userList.filter{ it.userId !in viewState.value.friendList.map { it.userId } &&
                it.userId !in viewState.value.incomingList.map { it.userId } &&
                it.userId !in viewState.value.outgoingList.map { it.userId } &&
                it.userId != AuthInteractor.currentLoggedInUser?.userId
        }.toMutableStateList()
        viewState.update { it.copy(
            usersList = userList
        ) }
    }
}