package hu.bme.aut.android.socialcommunitythread.ui.auth.login

data class LoginViewState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val passwordVisibility: Boolean = false
)

sealed class LoginOneShotEvent {
    object NavigateToMainThread : LoginOneShotEvent()
    data class ShowToastMessage(val errorText: String) : LoginOneShotEvent()
}

sealed class LoginUiAction {
    class OnLogin(val email: String, val password: String) : LoginUiAction()
}