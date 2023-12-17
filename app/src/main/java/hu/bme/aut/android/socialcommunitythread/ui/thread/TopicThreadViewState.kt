package hu.bme.aut.android.socialcommunitythread.ui.thread

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import hu.bme.aut.android.socialcommunitythread.domain.dto.PersonalThreadPost
import hu.bme.aut.android.socialcommunitythread.domain.dto.PersonalTopicThread


data class TopicThreadViewState(
    val isLoading: Boolean = false,
    val items: SnapshotStateList<PersonalThreadPost> = mutableStateListOf(),
    val error: String? = null,
    val endReached: Boolean = false,
    val page: Int = 0,
    var personalTopicThread: PersonalTopicThread = PersonalTopicThread()
)

sealed class TopicThreadOneShotEvent{
    data class ShowToastMessage(val errorText: String): TopicThreadOneShotEvent()
    object AcquireId: TopicThreadOneShotEvent()
}