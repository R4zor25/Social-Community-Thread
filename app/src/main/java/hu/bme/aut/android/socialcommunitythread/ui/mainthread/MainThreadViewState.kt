package hu.bme.aut.android.socialcommunitythread.ui.mainthread

import hu.bme.aut.android.socialcommunitythread.domain.model.Post

data class MainThreadViewState(
    val isLoading: Boolean = false,
    val items: List<Post> = emptyList(),
    val error: String? = null,
    val endReached: Boolean = false,
    val page: Int = 0)

sealed class MainThreadOneShotEvent{
    data class ShowToastMessage(val errorText: String): MainThreadOneShotEvent()
}

sealed class MainThreadUiAction{
    class SavePostUiAction(val post: Post): MainThreadUiAction()
    class UnsavePostUiAction(val post: Post): MainThreadUiAction()
}