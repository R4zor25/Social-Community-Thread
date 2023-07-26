package hu.bme.aut.android.socialcommunitythread.ui.createthread

import hu.bme.aut.android.socialcommunitythread.domain.model.TopicThread
import hu.bme.aut.android.socialcommunitythread.ui.auth.login.LoginUiAction

data class CreateThreadViewState(val isLoading: Boolean = false)

sealed class CreateThreadOneShotEvent{
    data class ShowToastMessage(val errorText: String): CreateThreadOneShotEvent()
    class ThreadCreated: CreateThreadOneShotEvent()
}

sealed class CreateThreadUiAction{
    class CreateThread(val thread : TopicThread): CreateThreadUiAction()
}


