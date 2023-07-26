package hu.bme.aut.android.socialcommunitythread.ui.uicomponent.threadrowitem

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.domain.model.Post
import hu.bme.aut.android.socialcommunitythread.domain.model.PostType
import hu.bme.aut.android.socialcommunitythread.domain.model.TopicThread
import hu.bme.aut.android.socialcommunitythread.domain.model.VoteType
import hu.bme.aut.android.socialcommunitythread.ui.theme.Beige
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.GifImage
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.VideoPlayer
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.circleimage.CircleImage

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun ThreadRowItem(post: Post, onItemClick: (String) -> Unit, onSaveLaterClick: (Post) -> Unit) {
    val threadItemModel by remember { mutableStateOf(post) }
    var upvote by rememberSaveable { mutableStateOf(post.voteNumber) }
    var voteType by rememberSaveable { mutableStateOf(post.voteType) }
    var isSaved by rememberSaveable { mutableStateOf(post.isSaved) }


    Surface(modifier = Modifier
        .clickable { onItemClick("Test") }
        .padding(horizontal = 8.dp, vertical = 8.dp)
        .fillMaxWidth(),
        color = Beige,
        shape = RoundedCornerShape(topEnd = 30.dp, topStart = 30.dp)) {
        Column(modifier = Modifier.padding(bottom = 8.dp, top = 4.dp, end = 8.dp, start = 8.dp)) {
            Row() {
                CircleImage(imageSize = 48, imageUrl = post.topicThread.threadImageUrl)
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(modifier = Modifier.padding(top = 2.dp), text = "t/${threadItemModel.topicThread.name}", color = Color.Black, fontSize = 14.sp)
                    Text(modifier = Modifier.padding(top = 4.dp), text = "Posted by ${threadItemModel.postedBy} ${threadItemModel.postTime}", color = Color.Black)
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = threadItemModel.title, color = Color.Black, fontSize = 18.sp,
                        textAlign = TextAlign.Justify)
                    when (threadItemModel.postType) {
                        PostType.IMAGE -> {
                            Image(painterResource(R.drawable.imagetest),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(6.dp),
                                contentDescription = "content description",
                                contentScale = ContentScale.FillWidth,
                                alignment = Alignment.Center)
                        }
                        PostType.GIF -> {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                                GifImage(Modifier, R.drawable.giphy)
                            }
                        }
                        PostType.VIDEO -> {
                            VideoPlayer(threadItemModel.videoUrl.toString())
                        }
                        PostType.AUDIO -> {

                        }
                        PostType.TEXT -> {

                        }
                    }
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    when (voteType) {
                        VoteType.CLEAR -> {
                            threadItemModel.voteNumber += 1
                            upvote += 1
                            voteType = VoteType.UPVOTED
                            post.voteType = VoteType.UPVOTED
                        }
                        VoteType.UPVOTED -> {
                            threadItemModel.voteNumber -= 1
                            upvote -= 1
                            voteType = VoteType.CLEAR
                            post.voteType = VoteType.CLEAR
                        }
                        VoteType.DOWNVOTED -> {
                            upvote += 2
                            threadItemModel.voteNumber += 2
                            post.voteType = VoteType.UPVOTED
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
                            threadItemModel.voteNumber -= 1
                            upvote -= 1
                            voteType = VoteType.DOWNVOTED
                            post.voteType = VoteType.DOWNVOTED
                        }
                        VoteType.UPVOTED -> {
                            threadItemModel.voteNumber -= 2
                            upvote -= 2
                            voteType = VoteType.DOWNVOTED
                            post.voteType = VoteType.DOWNVOTED
                        }
                        VoteType.DOWNVOTED -> {
                            upvote += 1
                            threadItemModel.voteNumber += 1
                            post.voteType = VoteType.CLEAR
                            voteType = VoteType.CLEAR
                        }
                    }
                }) {
                    Icon(Icons.Filled.ArrowDownward, tint = when (voteType) {
                        VoteType.DOWNVOTED -> Color.Red
                        else -> Color.Gray
                    }, contentDescription = "", modifier = Modifier.size(30.dp))
                }
                Spacer(modifier = Modifier.padding(start = 16.dp))
                IconButton(onClick = {
                    onItemClick("Test")
                }) {
                    Icon(Icons.Filled.ChatBubbleOutline, tint = Color.Black, contentDescription = "", modifier = Modifier.size(30.dp))
                }
                Text(text = threadItemModel.commentNumber.toString(), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.padding(start = 16.dp))
                IconButton(onClick = {
                    post.isSaved = !post.isSaved
                    isSaved = !isSaved
                    onSaveLaterClick(post)
                }) {
                    Icon(imageVector = if (isSaved)
                        Icons.Filled.Check
                    else
                        Icons.Filled.WatchLater, tint = Color.Black, contentDescription = "", modifier = Modifier.size(30.dp))
                }
                Text(text = if (isSaved) {
                    stringResource(R.string.saved)
                } else
                    stringResource(R.string.save), textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
@Preview
fun StockNewsRowItemPreview() {
    ThreadRowItem(Post(0, TopicThread(0, "BME_AUT", ""), "Alfred", "2 hours ago", 6500, 6600,
        "A postnak a címe, ami jó hosszú is lehet asduihfoipehfaiuhejn", false,
        PostType.VIDEO, VoteType.CLEAR, "", "", videoUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"), {}, {})
}


