package hu.bme.aut.android.socialcommunitythread.ui.uicomponent.chatrowitem

import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.di.DateFormatter
import hu.bme.aut.android.socialcommunitythread.domain.interactors.AuthInteractor
import hu.bme.aut.android.socialcommunitythread.domain.model.PersonalChatConversation
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultTextColor
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.circleimage.CircleImage

@Composable
fun ChatRowItem(personalChatConversation: PersonalChatConversation, onClick : (Long) -> Unit) {
    val fontWeight = if(true)
        FontWeight.Normal
    else
        FontWeight.Bold

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.secondary)
        .clickable { onClick.invoke(personalChatConversation.id!!) }
        .padding(horizontal = 2.dp, vertical = 6.dp)
        ) {
        Column(
            Modifier
                .fillMaxWidth(0.15f)
                .padding(4.dp)) {
            CircleImage(imageSize = 40, BitmapFactory.decodeByteArray(personalChatConversation.conversationImage, 0, personalChatConversation.conversationImage?.size ?: 0))
        }
        Column(Modifier.fillMaxWidth()) {
            Spacer(Modifier.padding(top = 5.dp))
            Text(text = personalChatConversation.conversationName, fontSize = 14.sp, fontWeight = fontWeight, color = defaultTextColor())
            Spacer(Modifier.padding(top = 4.dp))
            Row() {
                Text(modifier = Modifier.widthIn(0.dp, 240.dp), text = if(personalChatConversation.messageList.isNotEmpty())personalChatConversation.messageList.last().messageText else "",
                    fontSize = 12.sp, fontWeight = fontWeight, maxLines = 1, overflow = TextOverflow.Ellipsis,
                    color = defaultTextColor())
                Text(text = " - " + DateFormatter.format(personalChatConversation.lastMessageDate), fontSize = 12.sp, fontWeight = fontWeight, maxLines = 1,
                    color = defaultTextColor())
            }
            }
    }
}

@Composable
@Preview
fun ChatRowItemPreview() {
    //ChatRowItem(personalChatConversation = PersonalChatConversation(id = 0, conversationName = "Sample User 2", date = "2 hours ago",
        //imageResource = R.drawable.capybara, messagePreview =  "Thank you!")){}
}