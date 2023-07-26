package hu.bme.aut.android.socialcommunitythread.ui.thread

import hu.bme.aut.android.socialcommunitythread.domain.model.Post
import hu.bme.aut.android.socialcommunitythread.domain.model.TopicThread
import hu.bme.aut.android.socialcommunitythread.ui.mainthread.MainThreadUiAction
import hu.bme.aut.android.socialcommunitythread.ui.threaddetails.ThreadDetailsOneShotEvent
import hu.bme.aut.android.socialcommunitythread.ui.threaddetails.ThreadDetailsUiAction


data class TopicThreadViewState(
    val isLoading: Boolean = false,
    val items: List<Post> = emptyList(),
    val error: String? = null,
    val endReached: Boolean = false,
    val page: Int = 0)

sealed class TopicThreadOneShotEvent{
    data class ShowToastMessage(val errorText: String): TopicThreadOneShotEvent()
    object AcquireId: TopicThreadOneShotEvent()
    data class ThreadItemAcquired(val topicThread: TopicThread): TopicThreadOneShotEvent()
}

sealed class TopicThreadUiAction{
    class OnInit(): TopicThreadUiAction()
    class FollowThread(threadId: Int): TopicThreadUiAction()
    class UnfollowThread(threadId: Int): TopicThreadUiAction()
    class SavePostUiAction(val post: Post): TopicThreadUiAction() //TODO WITH ID
    class UnsavePostUiAction(val post: Post): TopicThreadUiAction() //TODO WITH ID
}