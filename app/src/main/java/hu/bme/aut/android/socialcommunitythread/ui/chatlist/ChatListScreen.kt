package hu.bme.aut.android.socialcommunitythread.ui.chatlist

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.domain.interactors.AuthInteractor
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultIconColor
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.BottomNavigationBar
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.chat.CreateConversationDialog
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.chatrowitem.ChatRowItem
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.navigationdrawer.NavigationDrawer
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.topbar.TopBar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChatListScreen(navController: NavController, viewModel: ChatListViewModel){
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val viewState = viewModel.uiState.collectAsState().value
    var createChatDialogVisible by remember { mutableStateOf(false) }


    LaunchedEffect("key") {
        viewModel.oneShotEvent
            .onEach {
                when (it) {
                    is ChatListOneShotEvent.ShowToastMessage -> Toast.makeText(context, it.errorText, Toast.LENGTH_LONG).show()
                }
            }.collect()
    }

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colors.primary),
        scaffoldState = scaffoldState,
        topBar = { TopBar(stringResource(R.string.chat), leftIconImage = null, rightIconImage = {
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
        } , scope, scaffoldState, {}, {}) },
        drawerBackgroundColor = MaterialTheme.colors.primary,
        drawerContent = {
            NavigationDrawer(navController = navController, scaffoldState = scaffoldState, scope)
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        floatingActionButton = {
            Button(onClick = {
                createChatDialogVisible = true
            }, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)) {
                Icon(imageVector = Icons.Filled.Add, tint  = defaultIconColor(), contentDescription = "")
            }
        }
    ) {

        if(createChatDialogVisible){
            CreateConversationDialog(onDismiss = { createChatDialogVisible = false },
                onNegativeClick = { createChatDialogVisible = false },
                onPositiveClick = {
                    createChatDialogVisible = false
                    viewModel.createChatConversation(chatConversation = it)
                }
            )
        }

        LazyColumn(state = listState, modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
            .padding(bottom = 60.dp)) {
            items(viewState.items.size) { i ->
                val item = viewState.items[i]
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ChatRowItem(personalChatConversation = item){
                        navController.navigate("chat_details/${it}")
                    }
                    Divider()
                }
            }
        }
    }
}