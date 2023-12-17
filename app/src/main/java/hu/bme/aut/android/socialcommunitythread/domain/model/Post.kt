package hu.bme.aut.android.socialcommunitythread.domain.model

import hu.bme.aut.android.socialcommunitythread.domain.dto.PostType
import hu.bme.aut.android.socialcommunitythread.domain.dto.VoteType

data class Post(
    var id: Int = 0,
    var topicThread: TopicThread = TopicThread(),
    var postedBy: String = "",
    var postTime: String = "2 hours ago",
    var voteNumber: Int = 0,
    var commentNumber: Int = 0,
    var title: String = "",
    var description : String = "",
    var tags : List<String> = listOf(),
    var isSaved: Boolean = false,
    var postType: PostType = PostType.TEXT,
    var voteType: VoteType = VoteType.CLEAR,
    var imageUrl: String? = "",
    var gifUrl: String? = "",
    var videoUrl: String? = "",
)

