package hu.bme.aut.android.socialcommunitythread.domain.model

import android.graphics.Bitmap

data class TopicThread(
    var id : Int,
    val name : String,
    val threadImageUrl: String? = null,
    val threadImageResource: Int? = null,
    val threadImageBitmap: Bitmap? = null,
    val description: String? = "",
    val publicity: ThreadPublicity = ThreadPublicity.PUBLIC
)

enum class ThreadPublicity{
    PUBLIC, PRIVATE
}