package hu.bme.aut.android.socialcommunitythread.ui.mainthread

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.domain.interactors.AuthInteractor
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.BottomNavigationBar
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.threadrowitem.ThreadRowItem
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.navigationdrawer.NavigationDrawer
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.topbar.TopBar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.util.UUID

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainThreadScreen(navController: NavController, viewModel: MainThreadViewModel) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val viewState = viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()


    LaunchedEffect("key") {
        viewModel.oneShotEvent
            .onEach {
                when (it) {
                    is MainThreadOneShotEvent.ShowToastMessage -> {
                        }
                }
            }.collect()
    }

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colors.primary),
        scaffoldState = scaffoldState,
        topBar = { TopBar(stringResource(R.string.main_thread), leftIconImage = null, rightIconImage = {
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
        } , scope, scaffoldState, {}, {

        }) },
        drawerBackgroundColor = MaterialTheme.colors.primary,
        drawerContent = {
            NavigationDrawer(navController = navController,
                scaffoldState = scaffoldState, scope)
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
    ) {
        LazyColumn(state = listState, modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
            .padding(bottom = 56.dp)) {
            itemsIndexed(viewState.value.items, key = {_, post -> UUID.randomUUID().toString()}) { i, item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    ThreadRowItem(item, onItemClick = {
                        navController.navigate("post_details/${item.topicThread.topicThreadId}/${item.postId}")
                    },
                        onSaveLaterClick = {
                            if(it.isSavedByUser){
                                viewModel.unsavePost(it)
                            } else{
                                viewModel.savePost(it)
                            }
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