package hu.bme.aut.android.socialcommunitythread.ui.savedposts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.threadrowitem.ThreadRowItem
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.navigationdrawer.NavigationDrawer
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.topbar.TopBar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun SavedPostsScreen(navController: NavController) {
    val viewModel = hiltViewModel<SavedPostsViewModel>()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val viewState = viewModel.viewState
    val listState = rememberLazyListState()

    LaunchedEffect("key") {
        viewModel.oneShotEvent
            .onEach {
                when (it) {
                    is SavedPostsOneShotEvent.ShowToastMessage -> TODO()
                }
            }.collect()
    }

    Scaffold(
        modifier = Modifier.background(Color.White),
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(stringResource(R.string.saved_posts), leftIconImage = Icons.Filled.ArrowBack, rightIconImage = {
                Image(
                    painter = painterResource(R.drawable.capybara),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
            }, scope, scaffoldState, {
                navController.navigateUp()
            }, {})
        },
        drawerBackgroundColor = Color.White,
        drawerContent = {
            NavigationDrawer(navController = navController, scaffoldState = scaffoldState, scope)
        },
    ) {
        LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
            items(viewState.savedPostList.size) { i ->
                val item = viewState.savedPostList[i]
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
                                viewModel.onAction(SavedPostsUiAction.SavePostUiAction(it))
                            else
                                viewModel.onAction(SavedPostsUiAction.UnsavePostUiAction(it))
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