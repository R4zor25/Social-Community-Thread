package hu.bme.aut.android.socialcommunitythread.domain.model

import android.graphics.Bitmap

data class ChatPreview(
    var id: Int,
    var name: String,
    var date: String,
    var imageUrl: String? = null,
    var imageResource: Int? = null,
    var imageBitmap: Bitmap? = null,
    var seen: Boolean = false,
    var messagePreview: String = ""
    )