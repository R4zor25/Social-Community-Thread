package hu.bme.aut.android.socialcommunitythread.ui.auth.registration

data class RegistrationScreenViewState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val password: String = "",
    val email: String = "",
    val passwordVisibility: Boolean = false
)

sealed class RegistrationOneShotEvent{
    object NavigateToMainThread: RegistrationOneShotEvent()
    data class ShowToastMessage(val errorText: String): RegistrationOneShotEvent()
}

sealed class RegistrationUiAction{
    class OnRegistration(val email: String, val userName: String, val password: String): RegistrationUiAction()
}