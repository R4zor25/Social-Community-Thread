package hu.bme.aut.android.socialcommunitythread.ui.userprofile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class UserProfileViewState(
    val isLoading: Boolean = false,
    var username: MutableState<String> = mutableStateOf(""),
    var email: MutableState<String> = mutableStateOf(""),
    var editMode: Boolean = false)

sealed class UserProfileOneShotEvent{
    data class ShowToastMessage(val errorText: String): UserProfileOneShotEvent()
}

sealed class UserProfileUiAction{

}