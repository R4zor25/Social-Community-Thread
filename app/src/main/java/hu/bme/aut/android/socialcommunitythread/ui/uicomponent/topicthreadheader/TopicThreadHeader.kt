package hu.bme.aut.android.socialcommunitythread.ui.uicomponent.topicthreadheader

import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
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
import hu.bme.aut.android.socialcommunitythread.domain.dto.PersonalTopicThread
import hu.bme.aut.android.socialcommunitythread.ui.theme.PrimaryLight
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultTextColor
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.circleimage.CircleImage

@Composable
fun TopicThreadHeader(thread: PersonalTopicThread, follow : (PersonalTopicThread) -> Unit, unfollow: (PersonalTopicThread) -> Unit){
    Surface(shape = RoundedCornerShape(15.dp).copy(ZeroCornerSize, ZeroCornerSize)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colors.secondary)
                .padding(start = 8.dp, bottom = 8.dp, end = 8.dp)) {
            Column(Modifier.fillMaxWidth(0.75f)) {
                Text(text = thread.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = defaultTextColor())
                Spacer(Modifier.padding(top = 8.dp))
                Text(text = thread.description, textAlign = TextAlign.Justify, fontSize = 14.sp, color = defaultTextColor())

            }
            Column() {
                CircleImage(imageSize = 80, kotlin.run{
                    BitmapFactory.decodeByteArray(thread.threadImage, 0, thread.threadImage.size);
                })
                Button(
                    modifier = Modifier
                        .width(150.dp)
                        .wrapContentHeight(),
                    onClick = {
                        if(thread.isFollowedByUser){
                            unfollow.invoke(thread)
                        } else {
                            follow.invoke(thread)
                        }
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = MaterialTheme.colors.primary
                    ),
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
                ) {
                    Text(text = if(thread.isFollowedByUser) "Unfollow" else  "Follow", fontSize = 16.sp, color = defaultTextColor())
                }
            }
            }
    }

}

@Composable
@Preview
fun TopicThreadHeaderPreview(){
    TopicThreadHeader(thread = PersonalTopicThread(
        name = "BME_AUT_0001",
        description = "ajnaeruierfujikrfdeujlikrfdegiehunjfrshieuljnéfgrshujegrfisohioujegfrsnoierhfgsjnuegorfihs",

    ), follow = {},
        unfollow = {})
}
