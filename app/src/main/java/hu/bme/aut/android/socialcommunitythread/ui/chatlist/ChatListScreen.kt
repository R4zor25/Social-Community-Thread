package hu.bme.aut.android.socialcommunitythread.ui.chatlist

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
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
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.BottomNavigationBar
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.chatrowitem.ChatRowItem
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.navigationdrawer.NavigationDrawer
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.topbar.TopBar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChatListScreen(navController: NavController){
    val viewModel = hiltViewModel<ChatListViewModel>()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LaunchedEffect("key") {
        viewModel.oneShotEvent
            .onEach {
                when (it) {

                    is ChatListOneShotEvent.ShowToastMessage -> {}
                }
            }.collect()
    }

    Scaffold(
        modifier = Modifier.background(Color.White),
        scaffoldState = scaffoldState,
        topBar = { TopBar(stringResource(R.string.chat), leftIconImage = null, rightIconImage = {
            Image(
                painter = painterResource(R.drawable.capybara),
                contentDescription = "avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
        } , scope, scaffoldState, {}, {}) },
        drawerBackgroundColor = Color.White,
        drawerContent = {
            NavigationDrawer(navController = navController, scaffoldState = scaffoldState, scope)
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
    ) {
        LazyColumn(state = listState, modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 60.dp)) {
            items(viewModel.viewState.items.size) { i ->
                val item = viewModel.viewState.items[i]
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ChatRowItem(chatPreview = item)
                    Divider()
                }
            }
        }
    }
}