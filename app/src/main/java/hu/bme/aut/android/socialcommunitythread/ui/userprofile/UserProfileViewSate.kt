package hu.bme.aut.android.socialcommunitythread.ui.userprofile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class UserProfileViewState(
    val isLoading: Boolean = false,
    var username: String = "UserName",
    var email: MutableState<String> = mutableStateOf(""),
    var file: ByteArray = byteArrayOf())

sealed class UserProfileOneShotEvent{
    data class ShowToastMessage(val errorText: String): UserProfileOneShotEvent()
    class UserUpdateSuccess(): UserProfileOneShotEvent()
}