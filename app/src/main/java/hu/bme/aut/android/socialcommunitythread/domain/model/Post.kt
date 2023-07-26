package hu.bme.aut.android.socialcommunitythread.domain.model

data class Post(
    var id: Int,
    var topicThread: TopicThread,
    var postedBy: String,
    var postTime: String = "2 hours ago",
    var voteNumber: Int,
    var commentNumber: Int,
    var title: String,
    var isSaved: Boolean = false,
    var postType: PostType,
    var voteType: VoteType = VoteType.CLEAR,
    var imageUrl: String? = "",
    var gifUrl: String? = "",
    var videoUrl: String? = "",
)

enum class PostType {
    TEXT, IMAGE, GIF, VIDEO, AUDIO
}

enum class VoteType {
    CLEAR, UPVOTED, DOWNVOTED
}