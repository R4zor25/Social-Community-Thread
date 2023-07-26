package hu.bme.aut.android.socialcommunitythread.ui.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.android.socialcommunitythread.domain.model.TopicThread
import hu.bme.aut.android.socialcommunitythread.navigation.ThreadScreenNav
import hu.bme.aut.android.socialcommunitythread.ui.theme.Beige
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.circleimage.CircleImage

@Composable
fun MiniThreadRowItem(topicThread: TopicThread, navController: NavController){
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Beige)
        .clickable { navController.navigate("topic_thread/${topicThread.id}") }
        .padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        CircleImage(imageSize = 36, imageUrl = topicThread.threadImageUrl, imageResource = topicThread.threadImageResource, cameraImage = topicThread.threadImageBitmap)
        Spacer(modifier = Modifier.padding(start = 6.dp))
        Text(text = "t/${topicThread.name}", fontSize = 18.sp, color = Color.Black)
    }
}

@Composable
@Preview
fun MiniThreadRowItemPreview(){
    MiniThreadRowItem(TopicThread(0, name = "RickRollers", threadImageUrl = "https://picsum.photos/100"), rememberNavController())
}