package hu.bme.aut.android.socialcommunitythread.ui.savedposts

import hu.bme.aut.android.socialcommunitythread.domain.model.Post
import hu.bme.aut.android.socialcommunitythread.ui.mainthread.MainThreadUiAction

data class SavedPostsViewState(
    val isLoading: Boolean = true,
    val savedPostList: MutableList<Post> = mutableListOf()
)

sealed class SavedPostsOneShotEvent {
    data class ShowToastMessage(val errorText: String) : SavedPostsOneShotEvent()
}

sealed class SavedPostsUiAction() {
    class SavePostUiAction(val post: Post): SavedPostsUiAction()
    class UnsavePostUiAction(val post: Post): SavedPostsUiAction()
}