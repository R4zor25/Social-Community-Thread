package hu.bme.aut.android.socialcommunitythread.ui.auth.registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialcommunitythread.domain.interactors.AuthInteractor
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authInteractor: AuthInteractor
) : ViewModel() {

    private val coroutineScope = MainScope()
    private val IoDispatcher: CoroutineDispatcher = Dispatchers.IO

    var viewState by mutableStateOf(RegistrationScreenViewState())

    private val _oneShotEvents = Channel<RegistrationOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    fun onAction(registrationUiAction: RegistrationUiAction) {
        when (registrationUiAction) {
            is RegistrationUiAction.OnRegistration -> {
                coroutineScope.launch {
                    viewState = viewState.copy(isLoading = true)
                    if (registrationUiAction.email.isNotBlank() && registrationUiAction.password.isNotBlank() && registrationUiAction.userName.isNotBlank()) {
                        val message = withContext(IoDispatcher) { authInteractor.createUser(registrationUiAction.email, registrationUiAction.userName, registrationUiAction.password) }
                        if (message == "Registration successful!") {
                            _oneShotEvents.send(RegistrationOneShotEvent.NavigateToMainThread)
                        } else {
                            _oneShotEvents.send(RegistrationOneShotEvent.ShowToastMessage(message))
                        }
                    } else {
                        _oneShotEvents.send(RegistrationOneShotEvent.ShowToastMessage("One or more text fields are empty!"))
                    }

                    viewState = viewState.copy(isLoading = false)
                }
            }
        }
    }
}