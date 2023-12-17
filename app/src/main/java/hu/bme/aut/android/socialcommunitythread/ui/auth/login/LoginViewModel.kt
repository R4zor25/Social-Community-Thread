package hu.bme.aut.android.socialcommunitythread.ui.auth.login

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialcommunitythread.di.AppModule
import hu.bme.aut.android.socialcommunitythread.domain.dto.RefreshTokenRequest
import hu.bme.aut.android.socialcommunitythread.domain.interactors.AuthInteractor
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val coroutineScope = MainScope()
    private val IoDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val viewState: MutableStateFlow<LoginViewState> = MutableStateFlow(LoginViewState())
    val uiState = viewState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        viewState.value
    )


    private val _oneShotEvents = Channel<LoginOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val jwtString = sharedPreferences.getString("access", "")
            if(!jwtString.isNullOrBlank()){
                val jwt = JWT(jwtString)
                if(jwt.expiresAt?.after(Calendar.getInstance().time) == true){
                    authInteractor.getAccessToken(
                        RefreshTokenRequest().apply {
                            token = sharedPreferences.getString("refresh", "").toString()
                        }
                    ){
                        coroutineScope.launch(Dispatchers.IO) {
                            _oneShotEvents.send(LoginOneShotEvent.NavigateToMainThread)
                        }
                    }
                }
            }
        }
    }

    fun login(username: String, password: String){
        coroutineScope.launch(Dispatchers.IO) {
            viewState.value = viewState.value.copy(isLoading = true)
            if (username.isNotBlank() && password.isNotBlank()) {
                authInteractor.login(username, password){
                    coroutineScope.launch(Dispatchers.IO) {
                        viewState.value = viewState.value.copy(isLoading = false)
                        if(it.isBlank()){
                            _oneShotEvents.send(LoginOneShotEvent.NavigateToMainThread)
                        } else {
                            _oneShotEvents.send(LoginOneShotEvent.ShowToastMessage(it))
                        }

                    }
                }
            } else {
                _oneShotEvents.send(LoginOneShotEvent.ShowToastMessage("One or more text fields are empty!"))
            }
            viewState.value = viewState.value.copy(isLoading = false)
        }
    }

    fun changeMockLoginData(){
        viewState.update { it ->
            it.copy(
                username = "string",
                password = "string"
            )
        }
    }

    fun onUsernameTextChange(username : String) {
        viewState.update { it.copy(username = username) }
    }

    fun onPasswordTextChange(password : String) {
        viewState.update { it.copy(password = password) }
    }


}