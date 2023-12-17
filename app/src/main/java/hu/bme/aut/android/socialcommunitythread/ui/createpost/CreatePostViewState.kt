package hu.bme.aut.android.socialcommunitythread.ui.createpost

import hu.bme.aut.android.socialcommunitythread.domain.dto.PostType

data class CreatePostViewState(
    val isLoading: Boolean = false,
    val postTitle: String = "",
    val description: String = "",
    val tags: List<String> = listOf(),
    val file: ByteArray = byteArrayOf(),
    val postType : PostType = PostType.TEXT
)

sealed class CreatePostOneShotEvent{
    data class ShowToastMessage(val errorText: String): CreatePostOneShotEvent()
    class PostCreated() : CreatePostOneShotEvent()
}