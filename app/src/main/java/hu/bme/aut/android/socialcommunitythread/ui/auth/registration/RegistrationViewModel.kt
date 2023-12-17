package hu.bme.aut.android.socialcommunitythread.ui.auth.registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialcommunitythread.domain.interactors.AuthInteractor
import hu.bme.aut.android.socialcommunitythread.ui.auth.login.LoginOneShotEvent
import hu.bme.aut.android.socialcommunitythread.ui.auth.login.LoginViewState
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
) : ViewModel() {

    private val coroutineScope = MainScope()
    private val IoDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val viewState: MutableStateFlow<RegistrationScreenViewState> = MutableStateFlow(RegistrationScreenViewState())
    val uiState = viewState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        viewState.value
    )

    private val _oneShotEvents = Channel<RegistrationOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    fun register(email: String, userName: String, password: String) {
        coroutineScope.launch(Dispatchers.IO) {
            if (email.isNotBlank() && password.isNotBlank() && userName.isNotBlank()) {
                viewState.update { it.copy(isLoading = true) }
                authInteractor.register(userName, password, email) {
                    coroutineScope.launch(Dispatchers.IO) {
                        if (it.isBlank()) {
                            authInteractor.login(email, password) {
                                coroutineScope.launch(Dispatchers.IO) {
                                    viewState.update { it.copy(isLoading = false) }
                                    if (it.isBlank()) {
                                        _oneShotEvents.send(RegistrationOneShotEvent.NavigateToMainThread)
                                    } else {
                                        _oneShotEvents.send(RegistrationOneShotEvent.ShowToastMessage(it))
                                    }
                                }
                            }
                        } else {
                            _oneShotEvents.send(RegistrationOneShotEvent.ShowToastMessage(it))
                        }
                    }
                }
            } else {
                _oneShotEvents.send(RegistrationOneShotEvent.ShowToastMessage("One or more text fields are empty!"))
            }
            viewState.update { it.copy(isLoading = false) }
        }
    }

    fun onUsernameTextChange(userName: String) {
        viewState.update { it.copy(userName = userName) }
    }

    fun onEmailTextChange(email: String) {
        viewState.update { it.copy(email = email) }
    }

    fun onPasswordTextChange(password: String) {
        viewState.update { it.copy(password = password) }
    }

}