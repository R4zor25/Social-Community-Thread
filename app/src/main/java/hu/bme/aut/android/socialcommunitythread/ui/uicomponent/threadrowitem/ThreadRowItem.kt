package hu.bme.aut.android.socialcommunitythread.ui.uicomponent.threadrowitem

import android.graphics.BitmapFactory
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.load.resource.gif.GifDrawable
import hu.bme.aut.android.socialcommunitythread.di.DateFormatter
import hu.bme.aut.android.socialcommunitythread.domain.dto.PersonalThreadPost
import hu.bme.aut.android.socialcommunitythread.domain.dto.PostType
import hu.bme.aut.android.socialcommunitythread.domain.dto.VoteType
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultIconColor
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultTextColor
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.circleimage.CircleImage
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.gifimage.GifImage
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.taglist.TagList
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.videoplayer.LinkVideoPlayer


@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun ThreadRowItem(
    post: PersonalThreadPost,
    onItemClick: (String) -> Unit,
    onSaveLaterClick: (PersonalThreadPost) -> Unit,
    onUpVote: (PersonalThreadPost) -> Unit,
    onDownVote: (PersonalThreadPost) -> Unit,
) {

    Surface(modifier = Modifier
        .clickable { onItemClick("Test") }
        .padding(horizontal = 8.dp, vertical = 8.dp)
        .fillMaxWidth(),
        elevation = 4.dp,
        color = MaterialTheme.colors.secondary,
        shape = RoundedCornerShape(10.dp)) {

        Column(modifier = Modifier.padding(bottom = 16.dp, top = 8.dp, end = 12.dp, start = 12.dp)) {
            Row() {
                Row(modifier = Modifier.padding(top = 6.dp)) {
                    CircleImage(imageSize = 36, BitmapFactory.decodeByteArray(post.topicThread.threadImage, 0, post.topicThread.threadImage.size))
                }
                Spacer(Modifier.width(8.dp))
                Column {
                    Text("t/${post.topicThread.name}", color = defaultTextColor(), fontSize = 11.sp)
                    Text(
                        post.author.userName, color = defaultTextColor(), fontSize = 14.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        DateFormatter.format(post.postTime) , color = defaultTextColor(), fontSize = 11.sp,
                    )

                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = {
                            onSaveLaterClick.invoke(post)
                    }) {
                        Icon(
                            if (post.isSavedByUser) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                            "",
                            modifier = Modifier.size(30.dp),
                            tint = defaultIconColor()
                        )
                    }
                }
            }
            Column() {
                Text(
                    post.title, color = defaultTextColor(), fontSize = 20.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 9.dp)
                )
                TagList(post.tags)
                Text(post.description, color = defaultTextColor(), fontSize = 13.sp, maxLines = 3, overflow = TextOverflow.Ellipsis, lineHeight = 14.sp)
                Spacer(Modifier.padding(bottom = 8.dp))
            }
            when (post.postType) {
                PostType.TEXT -> {}
                PostType.IMAGE -> {
                    Image(
                        bitmap = BitmapFactory.decodeByteArray(post.file, 0, post.file.size).asImageBitmap(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        contentDescription = "content description",
                        contentScale = ContentScale.FillWidth,
                        alignment = Alignment.Center
                    )
                }

                PostType.GIF -> {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        GifImage(Modifier.padding(top = 8.dp), GifDrawable.createFromStream(post.file.inputStream(), null))
                    }
                }

                PostType.VIDEO -> {
                    //TODO VIdeo lejátszó működése
                     LinkVideoPlayer(post.file)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    onUpVote.invoke(post)
                }) {
                    Icon(
                        Icons.Filled.ArrowUpward, tint = when (post.userVoteType) {
                            VoteType.UPVOTED -> Color.Green
                            else -> Color.Gray
                        }, contentDescription = "", modifier = Modifier.size(30.dp)
                    )
                }
                Text(text = post.voteNumber.toString(), color= defaultTextColor(), textAlign = TextAlign.Center)
                IconButton(onClick = {
                    onDownVote.invoke(post)
                }) {
                    Icon(
                        Icons.Filled.ArrowDownward, tint = when (post.userVoteType) {
                            VoteType.DOWNVOTED -> Color.Red
                            else -> Color.Gray
                        }, contentDescription = "", modifier = Modifier.size(30.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(start = 16.dp))
                IconButton(onClick = {
                    onItemClick("Test")
                }) {
                    Icon(Icons.Filled.ChatBubbleOutline, tint = Color.Black, contentDescription = "", modifier = Modifier.size(30.dp))
                }
                Text(text = post.comments.size.toString(), color= defaultTextColor(), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.padding(start = 16.dp))
            }
        }
    }
}
/*
@Composable
@Preview
fun StockNewsRowItemPreview() {
    ThreadRowItem(
        Post(
            0, TopicThread(0, "BME_AUT", ""),
            "Alfred", "2 hours ago", 6500, 6600,
            "A postnak a címe, ami jó hosszú is lehet asduihfoipehfaiuhejn",
            "Descriptionasddddddddddddddddddddddddddddddddddddddddsmovjelrgjseoimgrvpjsimopegvrvpsogjeimrvmpsgjeorimvjpsieorgvpjosgreimvjposiegrjosimegrvvsjoipmrgsprgvoijmeoprgvjeimdddddddddddddddddddddddddddddddddd",
            tags = listOf("TAG1", "TAG2", "TAG3", "TAG4"), false,
            PostType.IMAGE, VoteType.CLEAR, "", "", videoUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
        ), {}, {}, {}, {})
}*/


