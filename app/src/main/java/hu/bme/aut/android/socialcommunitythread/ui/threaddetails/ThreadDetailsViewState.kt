package hu.bme.aut.android.socialcommunitythread.ui.threaddetails

import hu.bme.aut.android.socialcommunitythread.domain.model.CommentModel
import hu.bme.aut.android.socialcommunitythread.domain.model.Post
import hu.bme.aut.android.socialcommunitythread.ui.mainthread.MainThreadUiAction

data class ThreadDetailsViewState(val isLoading: Boolean = true, var post: Post?, var commentList: List<CommentModel> = emptyList())

sealed class ThreadDetailsOneShotEvent{
    data class ShowToastMessage(val errorText: String): ThreadDetailsOneShotEvent()
    object AcquireId: ThreadDetailsOneShotEvent()
    data class ThreadItemAcquired(val post: Post): ThreadDetailsOneShotEvent()
}

sealed class ThreadDetailsUiAction{
    class OnInit(): ThreadDetailsUiAction()
    class SavePostUiAction(val post: Post): ThreadDetailsUiAction()
    class UnsavePostUiAction(val post: Post): ThreadDetailsUiAction()
    class SendComment(val message: String) : ThreadDetailsUiAction()
}