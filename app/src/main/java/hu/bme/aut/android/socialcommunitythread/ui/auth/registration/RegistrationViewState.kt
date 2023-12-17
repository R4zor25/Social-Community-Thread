package hu.bme.aut.android.socialcommunitythread.ui.auth.registration

data class RegistrationScreenViewState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val password: String = "",
    val email: String = "",
)

sealed class RegistrationOneShotEvent{
    object NavigateToMainThread: RegistrationOneShotEvent()
    data class ShowToastMessage(val errorText: String): RegistrationOneShotEvent()
}