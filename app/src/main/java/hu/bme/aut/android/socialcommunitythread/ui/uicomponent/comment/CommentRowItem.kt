package hu.bme.aut.android.socialcommunitythread.ui.uicomponent.comment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.domain.model.CommentModel
import hu.bme.aut.android.socialcommunitythread.domain.model.VoteType
import hu.bme.aut.android.socialcommunitythread.ui.theme.Beige
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.circleimage.CircleImage

@Composable
fun CommentRowItem(cm: CommentModel){
    val commentModel by remember { mutableStateOf(cm) }
    var upvote by remember { mutableStateOf(cm.voteNumber) }
    var voteType by remember { mutableStateOf(cm.voteType) }

    Surface(modifier = Modifier
        .clickable { }
        .padding(horizontal = 8.dp)
        .fillMaxWidth(),
        color = Beige) {
        Column(modifier = Modifier.padding(bottom = 8.dp, top = 4.dp, end = 8.dp, start = 8.dp)) {
            Row() {
                CircleImage(imageSize = 30, imageUrl = commentModel.profileImageUrl)
                Column(modifier = Modifier.padding(start = 8.dp), verticalArrangement = Arrangement.Center) {
                    //Text(modifier = Modifier.padding(top = 2.dp), text = "t/${threadItemModel.threadName}", color = Color.Black, fontSize = 14.sp)
                    Text(modifier = Modifier.padding(top = 4.dp), text = "Posted by ${commentModel.userName} ${commentModel.commentTime}", color = Color.Black)
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 8.dp, horizontal = 4.dp)) {
                Text(text = commentModel.commentText, color = Color.Black, fontSize = 16.sp,
                    textAlign = TextAlign.Justify)
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        when (voteType) {
                            VoteType.CLEAR -> {
                                commentModel.voteNumber += 1
                                upvote += 1
                                voteType = VoteType.UPVOTED
                                commentModel.voteType = VoteType.UPVOTED
                            }
                            VoteType.UPVOTED -> {
                                commentModel.voteNumber -= 1
                                upvote -= 1
                                voteType = VoteType.CLEAR
                                commentModel.voteType = VoteType.CLEAR
                            }
                            VoteType.DOWNVOTED -> {
                                upvote += 2
                                commentModel.voteNumber += 2
                                commentModel.voteType = VoteType.UPVOTED
                                voteType = VoteType.UPVOTED
                            }
                        }
                    }) {
                        Icon(Icons.Filled.ArrowUpward, tint = when (voteType) {
                            VoteType.UPVOTED -> Color.Green
                            else -> Color.Gray
                        }, contentDescription = "", modifier = Modifier.size(30.dp))
                    }
                    Text(text = upvote.toString(), textAlign = TextAlign.Center)
                    IconButton(onClick = {
                        when (voteType) {
                            VoteType.CLEAR -> {
                                commentModel.voteNumber -= 1
                                upvote -= 1
                                voteType = VoteType.DOWNVOTED
                                commentModel.voteType = VoteType.DOWNVOTED
                            }
                            VoteType.UPVOTED -> {
                                commentModel.voteNumber -= 2
                                upvote -= 2
                                voteType = VoteType.DOWNVOTED
                                commentModel.voteType = VoteType.DOWNVOTED
                            }
                            VoteType.DOWNVOTED -> {
                                upvote += 1
                                commentModel.voteNumber += 1
                                commentModel.voteType = VoteType.CLEAR
                                voteType = VoteType.CLEAR
                            }
                        }
                    }) {
                        Icon(Icons.Filled.ArrowDownward, tint = when (voteType) {
                            VoteType.DOWNVOTED -> Color.Red
                            else -> Color.Gray
                        }, contentDescription = "", modifier = Modifier.size(30.dp))
                    }
                }
                /*Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.padding(start = 16.dp))
                    IconButton(onClick = {
                        //onSaveLaterClick("Test")
                    }) {
                        Icon(Icons.Filled.WatchLater, tint = Color.Black, contentDescription = "", modifier = Modifier.size(30.dp))
                    }
                    Text(text = stringResource(R.string.save), textAlign = TextAlign.Center)
                }*/
            }
        }
    }
}

@Composable
@Preview
fun CommentPreview(){
    CommentRowItem(cm = CommentModel(0,"Alfred", "2 h", 666, "https://picsum.photos/200/300", voteType = VoteType.CLEAR, "This is my thoughtful comment! This is my thoughtful comment! This is my thoughtful comment!"))
}