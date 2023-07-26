package hu.bme.aut.android.socialcommunitythread.ui.auth.login

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.bme.aut.android.socialcommunitythread.domain.auth.AuthRepository
import hu.bme.aut.android.socialcommunitythread.domain.interactors.AuthInteractor
import hu.bme.aut.android.socialcommunitythread.ui.auth.registration.RegistrationScreenViewState
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val repository: AuthRepository,
) : ViewModel() {

    private val coroutineScope = MainScope()
    private val IoDispatcher: CoroutineDispatcher = Dispatchers.IO

    var viewState by mutableStateOf(LoginViewState())

    private val _oneShotEvents = Channel<LoginOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    init {
        viewModelScope.launch {
            repository.signUp(
                username = "Test_user",
                email = "a@a.hu",
                password = "testPassword1"
            )
        }
    }

    fun onAction(loginUiAction: LoginUiAction) {
        when (loginUiAction) {
            is LoginUiAction.OnLogin -> {
                coroutineScope.launch {
                    viewState = viewState.copy(isLoading = true)
                    if (loginUiAction.email.isNotBlank() && loginUiAction.password.isNotBlank()) {
                        val message = withContext(IoDispatcher) { authInteractor.login(loginUiAction.email, loginUiAction.password) }
                        if (message == "Login successful!") {
                            _oneShotEvents.send(LoginOneShotEvent.NavigateToMainThread)
                        } else {
                            _oneShotEvents.send(LoginOneShotEvent.ShowToastMessage(message))
                        }
                    } else {
                        _oneShotEvents.send(LoginOneShotEvent.ShowToastMessage("One or more text fields are empty!"))
                    }
                    viewState = viewState.copy(isLoading = false)
                }
            }
        }
    }


}