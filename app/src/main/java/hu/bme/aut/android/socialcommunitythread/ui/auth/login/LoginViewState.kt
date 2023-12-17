package hu.bme.aut.android.socialcommunitythread.ui.auth.login

data class LoginViewState(
    val isLoading: Boolean = false,
    val username: String = "",
    val password: String = ""
)

sealed class LoginOneShotEvent {
    object NavigateToMainThread : LoginOneShotEvent()
    data class ShowToastMessage(val errorText: String) : LoginOneShotEvent()
}