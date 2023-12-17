package hu.bme.aut.android.socialcommunitythread.ui.uicomponent.comment

import android.graphics.BitmapFactory
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.android.socialcommunitythread.di.DateFormatter
import hu.bme.aut.android.socialcommunitythread.domain.dto.VoteType
import hu.bme.aut.android.socialcommunitythread.domain.model.PersonalCommentModel
import hu.bme.aut.android.socialcommunitythread.ui.theme.PrimaryLight
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultTextColor
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.circleimage.CircleImage

@Composable
fun CommentRowItem(cm: PersonalCommentModel, upvote : (PersonalCommentModel) -> Unit, downvote : (PersonalCommentModel) -> Unit){

    Surface(modifier = Modifier
        .clickable { }
        .padding(horizontal = 8.dp)
        .fillMaxWidth(),
        color = MaterialTheme.colors.secondary,
        shape = RoundedCornerShape(10.dp))
    {
        Column(modifier = Modifier.padding(bottom = 8.dp, top = 4.dp, end = 8.dp, start = 8.dp)) {
            Row() {
                CircleImage(imageSize = 30, BitmapFactory.decodeByteArray(cm.author.profileImage, 0, cm.author.profileImage.size))
                Column(modifier = Modifier.padding(start = 8.dp), verticalArrangement = Arrangement.Center) {
                    Text(modifier = Modifier.padding(top = 4.dp), text = "${cm.author.userName} - ${DateFormatter.format(cm.commentTime)}", color = defaultTextColor())
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 8.dp, horizontal = 4.dp)) {
                Text(text = cm.commentText, color = defaultTextColor(), fontSize = 16.sp,
                    textAlign = TextAlign.Justify)
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        upvote.invoke(cm)
                    }) {
                        Icon(Icons.Filled.ArrowUpward, tint = when (cm.voteType) {
                            VoteType.UPVOTED -> Color.Green
                            else -> Color.Gray
                        }, contentDescription = "", modifier = Modifier.size(30.dp))
                    }
                    Text(text = cm.voteNumber.toString(), textAlign = TextAlign.Center, color = defaultTextColor())
                    IconButton(onClick = {
                        downvote.invoke(cm)
                    }) {
                        Icon(Icons.Filled.ArrowDownward, tint = when (cm.voteType) {
                            VoteType.DOWNVOTED -> Color.Red
                            else -> Color.Gray
                        }, contentDescription = "", modifier = Modifier.size(30.dp))
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun CommentPreview(){
   // CommentRowItem(cm = PersonalCommentModel(0,"Alfred", "2 h", 666, "https://picsum.photos/200/300", voteType = VoteType.CLEAR, "This is my thoughtful comment! This is my thoughtful comment! This is my thoughtful comment!"))
}