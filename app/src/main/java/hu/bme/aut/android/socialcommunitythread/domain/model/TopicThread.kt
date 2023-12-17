package hu.bme.aut.android.socialcommunitythread.domain.model

import hu.bme.aut.android.socialcommunitythread.domain.dto.ThreadPost

data class TopicThread(
    var topicThreadId : Long = 0,
    var name : String = "",
    var threadImage: ByteArray = byteArrayOf(),
    var description: String = "",
   // var threadposts: List<ThreadPost> = listOf(),
    //var publicity: ThreadPublicity = ThreadPublicity.PUBLIC
)

enum class ThreadPublicity{
    PUBLIC, PRIVATE
}