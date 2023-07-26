package hu.bme.aut.android.socialcommunitythread.ui.uicomponent.topicthreadheader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.domain.model.TopicThread
import hu.bme.aut.android.socialcommunitythread.ui.createthread.CreateThreadUiAction
import hu.bme.aut.android.socialcommunitythread.ui.theme.Beige
import hu.bme.aut.android.socialcommunitythread.ui.theme.SecondaryColor
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.circleimage.CircleImage

@Composable
fun TopicThreadHeader(thread: TopicThread){
    Surface(shape = RoundedCornerShape(30.dp).copy(ZeroCornerSize, ZeroCornerSize)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Beige)
                .padding(start = 8.dp, bottom = 8.dp, end = 8.dp)) {
            Column(Modifier.fillMaxWidth(0.75f)) {
                Text(text = thread.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(Modifier.padding(top = 8.dp))
                if (!thread.description.isNullOrBlank()) {
                    Text(text = thread.description, textAlign = TextAlign.Justify, fontSize = 14.sp, color = Color.Black)
                }
            }
            Column() {
                CircleImage(imageSize = 80, imageResource = thread.threadImageResource, imageUrl = thread.threadImageUrl, cameraImage = thread.threadImageBitmap)
                Button(
                    onClick = {
                        //TODO
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = SecondaryColor
                    ),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    Text(text = "Followed", fontSize = 16.sp, color = Color.Black)
                }
            }
            }
    }

}

@Composable
@Preview
fun TopicThreadHeaderPreview(){
    TopicThreadHeader(thread = TopicThread(id = 1, name = "BME_AUT_0001", threadImageResource = R.drawable.capybara, description = "This is a long Description to test123123111111111111111111111132222224324123423 \n Another lIne \n \n two other"))
}
