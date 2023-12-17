package hu.bme.aut.android.socialcommunitythread.ui.userprofile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialcommunitythread.domain.dto.AppUser
import hu.bme.aut.android.socialcommunitythread.domain.interactors.AuthInteractor
import hu.bme.aut.android.socialcommunitythread.ui.mainthread.MainThreadViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val authInteractor: AuthInteractor
) : ViewModel() {
    private val coroutineScope = MainScope()

    private val viewState: MutableStateFlow<UserProfileViewState> = MutableStateFlow(UserProfileViewState())
    val uiState = viewState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        viewState.value
    )

    private val _oneShotEvents = Channel<UserProfileOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    fun updateUserProfile(){
        coroutineScope.launch(Dispatchers.IO) {
            authInteractor.updateUser(AppUser().apply {
                this.profileImage = viewState.value.file
            }) { errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        _oneShotEvents.send(UserProfileOneShotEvent.UserUpdateSuccess())
                    } else {
                        _oneShotEvents.send(UserProfileOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun updateProfileImage(file: ByteArray){
        viewState.value = viewState.value.copy(
            file = file
        )
    }
}