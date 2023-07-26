package hu.bme.aut.android.socialcommunitythread.ui.thread

import android.inputmethodservice.Keyboard
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.domain.model.TopicThread
import hu.bme.aut.android.socialcommunitythread.ui.theme.SecondaryColor
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.navigationdrawer.NavigationDrawer
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.threadrowitem.ThreadRowItem
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.topbar.TopBar
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.topicthreadheader.TopicThreadHeader
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun TopicThreadScreen(navController: NavController, threadId: Int) {
    val viewModel = hiltViewModel<TopicThreadViewModel>()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val viewState = viewModel.viewState
    val listState = rememberLazyListState()
    var thread: TopicThread by remember {
        mutableStateOf(TopicThread(
            id = 0, name = ""
        ))
    }

    LaunchedEffect("key") {
        viewModel.oneShotEvent
            .onEach {
                when (it) {
                    is TopicThreadOneShotEvent.ShowToastMessage -> TODO()
                    TopicThreadOneShotEvent.AcquireId -> {
                        viewModel.threadId = threadId
                        viewModel.onAction(TopicThreadUiAction.OnInit())
                    }
                    is TopicThreadOneShotEvent.ThreadItemAcquired -> {
                        thread = it.topicThread
                    }
                }
            }.collect()
    }

    Scaffold(
        modifier = Modifier.background(Color.White),
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(thread.name, leftIconImage = Icons.Filled.ArrowBack, rightIconImage = {
                Image(
                    painter = painterResource(R.drawable.capybara),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
            }, scope, scaffoldState, onLeftIconClick = {
                navController.navigateUp()
            }, {

            })
        },
        drawerBackgroundColor = Color.White,
        drawerContent = {
            NavigationDrawer(navController = navController, scaffoldState = scaffoldState, scope)
        },
        floatingActionButton = {
            Button(onClick = { navController.navigate("create_post") }, colors = ButtonDefaults.buttonColors(backgroundColor = SecondaryColor)) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "")
            }
        }
    ) {
        if (!viewModel.viewState.isLoading) {
            LazyColumn(state = listState, modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)) {
                item {
                    TopicThreadHeader(thread = thread)
                }
                items(viewState.items.size) { i ->
                    val item = viewState.items[i]
                    if (i >= viewState.items.size - 1 && !viewState.endReached && !viewState.isLoading) {
                        viewModel.loadNextItems()
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        ThreadRowItem(item, onItemClick = {
                            navController.navigate("thread_details/${item.id}")
                        },
                            onSaveLaterClick = {
                                if (it.isSaved)
                                    viewModel.onAction(TopicThreadUiAction.SavePostUiAction(it))
                                else
                                    viewModel.onAction(TopicThreadUiAction.UnsavePostUiAction(it))
                            })
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                item {
                    if (viewState.isLoading) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 60.dp),
                            horizontalArrangement = Arrangement.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}