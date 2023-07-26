package hu.bme.aut.android.socialcommunitythread.ui.uicomponent.chatrowitem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.domain.model.ChatPreview
import hu.bme.aut.android.socialcommunitythread.ui.theme.Beige
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.circleimage.CircleImage

@Composable
fun ChatRowItem(chatPreview: ChatPreview) {
    val fontWeight = if(chatPreview.seen)
        FontWeight.Normal
    else
        FontWeight.Bold

    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp, vertical = 2.dp).background(Beige)) {
        Column(Modifier.fillMaxWidth(0.15f).padding(4.dp)) {
            CircleImage(imageSize = 40, imageUrl = chatPreview.imageUrl, imageResource = chatPreview.imageResource, cameraImage = chatPreview.imageBitmap)
        }
        Column(Modifier.fillMaxWidth(0.8f)) {
            Text(text = chatPreview.name, fontSize = 14.sp, fontWeight = fontWeight)
            Spacer(Modifier.padding(top = 4.dp))
            Text(text = chatPreview.messagePreview + " - " + chatPreview.date, fontSize = 12.sp, fontWeight = fontWeight)
        }
    }
}

@Composable
@Preview
fun ChatRowItemPreview() {
    ChatRowItem(chatPreview = ChatPreview(id = 0, name = "Sample User 2", date = "2 hours ago",
        imageResource = R.drawable.capybara, messagePreview =  "Thank you!"))
}