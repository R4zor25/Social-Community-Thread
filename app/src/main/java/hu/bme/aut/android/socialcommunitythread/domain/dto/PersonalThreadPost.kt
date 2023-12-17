package hu.bme.aut.android.socialcommunitythread.domain.dto

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import hu.bme.aut.android.socialcommunitythread.domain.model.PersonalCommentModel
import hu.bme.aut.android.socialcommunitythread.domain.model.TopicThread
import java.util.Date

data class PersonalThreadPost(
    var postId: Long? = 0,
    var topicThread: TopicThread = TopicThread(),
    var postTime: Date = Date(),
    var comments: SnapshotStateList<PersonalCommentModel> = mutableStateListOf(),
    var author : AppUser = AppUser(),
    var title: String = "",
    var description : String = "",
    var tags : List<String> = listOf(),
    var voteNumber: Int = 0,
    var postType: PostType = PostType.TEXT,
    var file: ByteArray = byteArrayOf(),
    var userVoteType : VoteType = VoteType.CLEAR,
    var isSavedByUser : Boolean = false
)