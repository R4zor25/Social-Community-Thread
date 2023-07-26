package hu.bme.aut.android.socialcommunitythread.ui.chatdetails

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class ChatDetailsViewState(
    val isLoading: Boolean = false)

sealed class ChatDetailsOneShotEvent{
    data class ShowToastMessage(val errorText: String): ChatDetailsOneShotEvent()
}

sealed class ChatDetailsUiAction{

}