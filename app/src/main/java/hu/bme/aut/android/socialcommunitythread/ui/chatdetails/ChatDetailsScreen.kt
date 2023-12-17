package hu.bme.aut.android.socialcommunitythread.ui.chatdetails

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
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
import androidx.compose.ui.graphics.Color
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
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.ChatDetailsBottomBar
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.RemoveChatUserDialog
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.chatmessage.ChatMessageRowItem
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.friends.AddFriendDialog
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.navigationdrawer.NavigationDrawer
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.topbar.TopBar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChatDetailsScreen(navController: NavController, viewModel: ChatDetailsViewModel, chatId: Long) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val viewState = viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var showRemoveDialog by remember { mutableStateOf(false) }


    LaunchedEffect("key") {
        viewModel.oneShotEvent
            .onEach {
                when (it) {
                    is ChatDetailsOneShotEvent.ShowToastMessage -> {
                        Toast.makeText(context, it.errorText, Toast.LENGTH_LONG).show()
                    }

                    ChatDetailsOneShotEvent.AcquireId -> {
                        viewModel.initChatConversationId(chatId)
                        viewModel.getChatConversationDetails()
                        viewModel.getFriendList()
                    }
                }
            }.collect()
    }

    Scaffold(
        modifier = Modifier.background(Color.White),
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(stringResource(R.string.chat), leftIconImage = Icons.Filled.ArrowBack, rightIconImage = {
                if (AuthInteractor.currentLoggedInUser != null
                    && AuthInteractor.currentLoggedInUser!!.profileImage.size != null
                    && AuthInteractor.currentLoggedInUser!!.profileImage.size > 0
                ) {
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
            }, scope, scaffoldState, { navController.navigateUp() }, {})
        },
        drawerBackgroundColor = Color.White,
        drawerContent = {
            NavigationDrawer(navController = navController, scaffoldState = scaffoldState, scope)
        },
        bottomBar = {
            ChatDetailsBottomBar(viewState.value.currentText, {
                viewModel.onTextChange(it)
            }, {
                viewModel.sendMessage(it)
            })
        },
        floatingActionButton = {
            if(viewState.value.chatConversation.chatCreator.userId == AuthInteractor.currentLoggedInUser!!.userId) {
                Column {
                    Button(onClick = {
                        showRemoveDialog = true
                    }, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)) {
                        Icon(imageVector = Icons.Filled.Delete, tint = defaultIconColor(), contentDescription = "")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        showAddDialog = true
                    }, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)) {
                        Icon(imageVector = Icons.Filled.Add, tint = defaultIconColor(), contentDescription = "")
                    }
                }
            }
        },
    ) {
        if(showRemoveDialog){
            RemoveChatUserDialog(onDismiss = { showRemoveDialog = false }, onNegativeClick = { showRemoveDialog = false }, onPositiveClick = {
                viewModel.removeParticipant(it.userId)
                showRemoveDialog = false
            }, userList = viewState.value.chatConversation.chatParticipants)
        }
        if(showAddDialog){
            AddFriendDialog(onDismiss = { showAddDialog = false }, onNegativeClick = { showAddDialog = false }, onPositiveClick = {
                viewModel.addParticipant(it.userId)
            }, userList = viewState.value.friendList)
        }
        LazyColumn(
            state = listState, modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primary)
                .padding(bottom = 60.dp)
        ) {
            items(viewState.value.chatConversation.messageList.size) { i ->
                val item = viewState.value.chatConversation.messageList[i]
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ChatMessageRowItem(message = item, currentUserId = AuthInteractor.currentLoggedInUser!!.userId)
                    Divider()
                }
            }
        }
    }
}