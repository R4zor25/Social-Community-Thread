package hu.bme.aut.android.socialcommunitythread.domain.dto

import hu.bme.aut.android.socialcommunitythread.domain.model.PersonalCommentModel
import hu.bme.aut.android.socialcommunitythread.domain.model.TopicThread
import java.util.Date

data class ThreadPost(
    var postId : Long = 0,
    var topicThread: TopicThread = TopicThread(),
    var postTime : Date = Date(),
    var comments : MutableCollection<PersonalCommentModel> = mutableListOf(),
    var author : AppUser = AppUser(),
    var title : String = "",
    var description : String = "",
    var tags : List<String> = listOf(),
    var voteNumber : Int  = 0,
    var file : ByteArray = byteArrayOf(),
    var postType: PostType = PostType.TEXT,
)

enum class PostType {
    TEXT, IMAGE, GIF, VIDEO
}

enum class VoteType {
    CLEAR, UPVOTED, DOWNVOTED
}