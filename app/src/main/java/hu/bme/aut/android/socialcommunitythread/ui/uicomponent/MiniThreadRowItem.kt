package hu.bme.aut.android.socialcommunitythread.ui.uicomponent

import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import hu.bme.aut.android.socialcommunitythread.domain.model.TopicThread
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultTextColor
import hu.bme.aut.android.socialcommunitythread.ui.theme.secondaryTextColor
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.circleimage.CircleImage

@Composable
fun MiniThreadRowItem(topicThread: TopicThread, navController: NavController, showDescription : Boolean = false){
    Spacer(modifier = Modifier.height(2.dp))
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.secondary)
        .clickable { navController.navigate("topic_thread/${topicThread.topicThreadId}") }
        .padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {

        CircleImage(imageSize = 30, BitmapFactory.decodeByteArray(topicThread.threadImage, 0, topicThread.threadImage.size))

        Spacer(modifier = Modifier.padding(start = 12.dp))
        Column {
            Text(text = "t/${topicThread.name}", fontSize = 14.sp, color = defaultTextColor(), overflow = TextOverflow.Ellipsis)

            if (showDescription) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = topicThread.description.toString(), fontSize = 12.sp, color = secondaryTextColor(), maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
@Preview
fun MiniThreadRowItemPreview(){
    //MiniThreadRowItem(TopicThread(0, name = "RickRollers", threadImageUrl = "https://picsum.photos/100"), rememberNavController())
}