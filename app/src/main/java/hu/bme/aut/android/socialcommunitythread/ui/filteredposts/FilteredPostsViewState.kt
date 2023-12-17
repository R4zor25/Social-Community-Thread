package hu.bme.aut.android.socialcommunitythread.ui.filteredposts

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import hu.bme.aut.android.socialcommunitythread.domain.dto.PersonalThreadPost

data class FilteredPostsViewState(
    val isLoading: Boolean = true,
    val filteredPostList: SnapshotStateList<PersonalThreadPost> = mutableStateListOf()
)

sealed class FilteredPostsOneShotEvent {
    data class ShowToastMessage(val errorText: String) : FilteredPostsOneShotEvent()
}