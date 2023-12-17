package hu.bme.aut.android.socialcommunitythread.ui.mainthread

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import hu.bme.aut.android.socialcommunitythread.domain.dto.PersonalThreadPost
import hu.bme.aut.android.socialcommunitythread.domain.dto.ThreadPost
import hu.bme.aut.android.socialcommunitythread.domain.model.Post

data class MainThreadViewState(
    val isLoading: Boolean = false,
    val items: SnapshotStateList<PersonalThreadPost> = mutableStateListOf(),
    val error: String? = null,
    val endReached: Boolean = false,
    val page: Int = 0)

sealed class MainThreadOneShotEvent{
    data class ShowToastMessage(val errorText: String): MainThreadOneShotEvent()
}