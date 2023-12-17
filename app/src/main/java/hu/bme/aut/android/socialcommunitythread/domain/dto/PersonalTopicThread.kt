package hu.bme.aut.android.socialcommunitythread.domain.dto

data class PersonalTopicThread(
    var topicThreadId: Long? = 0,
    var name: String  = "",
    var threadImage : ByteArray = byteArrayOf(),
    var description: String = "",
    var isPublic: Boolean = true,
    //var threadposts: MutableCollection<ThreadPost> = mutableListOf(),
    var isFollowedByUser : Boolean = false,
)