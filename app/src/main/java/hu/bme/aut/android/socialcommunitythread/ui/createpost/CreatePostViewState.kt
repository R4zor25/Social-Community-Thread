package hu.bme.aut.android.socialcommunitythread.ui.createpost

import hu.bme.aut.android.socialcommunitythread.domain.model.Post
import hu.bme.aut.android.socialcommunitythread.domain.model.TopicThread
import hu.bme.aut.android.socialcommunitythread.ui.createthread.CreateThreadOneShotEvent
import hu.bme.aut.android.socialcommunitythread.ui.createthread.CreateThreadUiAction

data class CreatePostViewState(
    val isLoading: Boolean = false
)

sealed class CreatePostOneShotEvent{
    data class ShowToastMessage(val errorText: String): CreatePostOneShotEvent()
    object ThreadCreated : CreatePostOneShotEvent()
}

sealed class CreatePostUiAction{
    class CreateThread(val post : Post): CreatePostUiAction()
}