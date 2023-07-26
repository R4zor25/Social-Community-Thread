package hu.bme.aut.android.socialcommunitythread.ui.userprofile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor() : ViewModel() {
    private val coroutineScope = MainScope()

    val viewState by mutableStateOf(UserProfileViewState())

    private val _oneShotEvents = Channel<UserProfileOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    fun onAction(userProfileUiAction: UserProfileUiAction){
        when (userProfileUiAction){

            else -> {}
        }
    }
}