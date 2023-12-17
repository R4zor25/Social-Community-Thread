package hu.bme.aut.android.socialcommunitythread.ui.postdetails

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import hu.bme.aut.android.socialcommunitythread.domain.dto.PersonalThreadPost
import hu.bme.aut.android.socialcommunitythread.domain.model.PersonalCommentModel

data class PostDetailsViewState(
    val isLoading: Boolean = true,
    var post: PersonalThreadPost = PersonalThreadPost(),
    var comments : SnapshotStateList<PersonalCommentModel> = mutableStateListOf(),
)

sealed class PostDetailsOneShotEvent{
    data class ShowToastMessage(val errorText: String): PostDetailsOneShotEvent()
}