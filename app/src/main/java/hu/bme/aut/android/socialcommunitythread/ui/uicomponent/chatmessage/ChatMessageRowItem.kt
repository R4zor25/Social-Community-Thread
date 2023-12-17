package hu.bme.aut.android.socialcommunitythread.ui.uicomponent.chatmessage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.android.socialcommunitythread.di.DateFormatter
import hu.bme.aut.android.socialcommunitythread.domain.dto.AppUser
import hu.bme.aut.android.socialcommunitythread.domain.dto.ChatMessage
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultTextColor
import hu.bme.aut.android.socialcommunitythread.ui.theme.secondaryTextColor
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.circleimage.CircleImage
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.util.Date

@Composable
fun ChatMessageRowItem(message : ChatMessage, currentUserId: Long) {
    val backgroundColor = if(message.author.userId == currentUserId){
        Color(0xFFADD8E6)
    } else {
        MaterialTheme.colors.secondary
    }

    val alignment = if(message.author.userId == currentUserId){
        Alignment.End
    } else {
        Alignment.Start
    }

    Spacer(modifier = Modifier.height(12.dp))
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalAlignment = alignment
    ) {
        Text(text = message.author.userName + " - " + DateFormatter.format(message.sentDate) , color = defaultTextColor(), fontSize = 10.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .background(backgroundColor, shape = RoundedCornerShape(40.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(text = message.messageText, color = secondaryTextColor())
        }
    }
}

@Preview
@Composable
fun ChatMessageRowItemPreview(){
    ChatMessageRowItem(message = ChatMessage(
        author = AppUser().apply { this.name = "Test User" },
        messageText = "Ez egy próbaüzenet",
        sentDate =  Date()
    ), currentUserId = 1)
}