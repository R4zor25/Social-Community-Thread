package hu.bme.aut.android.socialcommunitythread.ui.thread

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.domain.interactors.AuthInteractor
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultIconColor
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.navigationdrawer.NavigationDrawer
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.threadrowitem.ThreadRowItem
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.topbar.TopBar
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.topicthreadheader.TopicThreadHeader
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TopicThreadScreen(navController: NavController, threadId: Long) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<TopicThreadViewModel>()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val viewState = viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect("key") {
        viewModel.oneShotEvent
            .onEach {
                when (it) {
                    is TopicThreadOneShotEvent.ShowToastMessage -> {
                        Toast.makeText(context, it.errorText, Toast.LENGTH_LONG).show()
                    }

                    TopicThreadOneShotEvent.AcquireId -> {
                        viewModel.passThreadId(threadId)
                        viewModel.onInit()
                    }
                }
            }.collect()
    }

    Scaffold(
        modifier = Modifier.background(Color.White),
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(viewState.value.personalTopicThread.name, leftIconImage = Icons.Filled.ArrowBack, rightIconImage = {
                if(AuthInteractor.currentLoggedInUser != null
                    && AuthInteractor.currentLoggedInUser!!.profileImage.size != null
                    && AuthInteractor.currentLoggedInUser!!.profileImage.size > 0){
                    Image(
                        bitmap = BitmapFactory.decodeByteArray(AuthInteractor.currentLoggedInUser!!.profileImage, 0, AuthInteractor.currentLoggedInUser!!.profileImage!!.size).asImageBitmap(),
                        contentDescription = "avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.capybara),
                        contentDescription = "avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                }
            }, scope, scaffoldState, onLeftIconClick = {
                navController.navigateUp()
            }, {

            })
        },
        drawerBackgroundColor = MaterialTheme.colors.secondary,
        drawerContent = {
            NavigationDrawer(navController = navController, scaffoldState = scaffoldState, scope)
        },
        floatingActionButton = {
            Button(onClick = { navController.navigate("create_post/${threadId}") }, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)) {
                Icon(imageVector = Icons.Filled.Add, tint = defaultIconColor(), contentDescription = "")
            }
        }
    ) {
        if (!viewState.value.isLoading) {
            LazyColumn(
                state = listState, modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.primary)
                    .padding(bottom = 60.dp)
            ) {
                item {
                    TopicThreadHeader(thread = viewState.value.personalTopicThread, follow = {
                        viewModel.followThread()
                    }, unfollow = {
                        viewModel.unfollowThread()
                    })
                }
                items(viewState.value.items.size) { i ->
                    val item = viewState.value.items[i]
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        ThreadRowItem(item, onItemClick = {
                            navController.navigate("post_details/${item.topicThread.topicThreadId}/${item.postId}")
                        },
                            onSaveLaterClick = {
                                if (it.isSavedByUser)
                                    viewModel.savePost(it)
                                else
                                    viewModel.unsavePost(it)
                            },
                            onUpVote = {
                                viewModel.upvotePost(it)
                            },
                            onDownVote = {
                                viewModel.downVotePost(it)
                            })
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                item {
                    if (viewState.value.isLoading) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 60.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}