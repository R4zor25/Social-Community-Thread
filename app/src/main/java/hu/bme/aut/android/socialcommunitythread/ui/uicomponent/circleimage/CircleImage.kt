package hu.bme.aut.android.socialcommunitythread.ui.uicomponent.circleimage

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

@Composable
fun CircleImage(imageSize: Int, cameraImage: Bitmap? = null) {
    val context = LocalContext.current
    /*if(imageResource != null) {
        Image(
            painter = painterResource(imageResource),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(imageSize.dp)
                .clip(CircleShape)
        )
    }else if (imageUrl != null){
        var bitmap by remember { mutableStateOf<Bitmap?>(null)}

        Glide.with(context).asBitmap()
            .load(imageUrl)
            .override(10)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    bitmap = resource
                }
                override fun onLoadCleared(placeholder: Drawable?) {}
            })
        if(bitmap?.asImageBitmap() != null) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl, filterQuality = FilterQuality.None),
                contentDescription = "avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(imageSize.dp)
                    .clip(CircleShape)
            )
        }
    }else if (cameraImage != null){*/
    Image(
        painter = rememberAsyncImagePainter(cameraImage, filterQuality = FilterQuality.None),
        contentDescription = "avatar",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(imageSize.dp)
            .clip(CircleShape)
    )
}