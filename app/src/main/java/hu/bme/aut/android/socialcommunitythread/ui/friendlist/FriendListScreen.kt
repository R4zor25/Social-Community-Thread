package hu.bme.aut.android.socialcommunitythread.ui.friendlist

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.domain.interactors.AuthInteractor
import hu.bme.aut.android.socialcommunitythread.ui.theme.PrimaryLight
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultIconColor
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultTextColor
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.friends.AddFriendDialog
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.friends.FriendList
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.friends.FriendType
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.navigationdrawer.NavigationDrawer
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.topbar.TopBar

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FriendListScreen(navController: NavController){
    val viewModel = hiltViewModel<FriendListViewModel>()
    val viewState  = viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val pagerState = rememberPagerState(pageCount = {
        3
    })
    var friendDialogVisibility by rememberSaveable { mutableStateOf(false) }


    Scaffold(
        modifier = Modifier.background(MaterialTheme.colors.primary),
        scaffoldState = scaffoldState,
        topBar = { TopBar("Friends", leftIconImage = Icons.Filled.ArrowBack, rightIconImage = {
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
        } , scope, scaffoldState, { navController.navigateUp() }, {

        }) },
        drawerBackgroundColor = MaterialTheme.colors.primary,
        drawerContent = {
            NavigationDrawer(navController = navController,
                scaffoldState = scaffoldState, scope)
        },
        floatingActionButton = {
            Button(onClick = {
                viewModel.filterUserList()
                friendDialogVisibility = true
                             }, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)) {
                Icon(imageVector = Icons.Filled.Add, tint = defaultIconColor(),  contentDescription = "")
            }
        }
    ) {
        HorizontalPager(state = pagerState) { page ->
            if(friendDialogVisibility) {
                AddFriendDialog(
                    onDismiss = {
                        friendDialogVisibility = false
                    },
                    onNegativeClick = {
                        friendDialogVisibility = false
                    },
                    onPositiveClick = {
                        viewModel.addFriend(it)
                        friendDialogVisibility = false
                    },
                    userList = viewState.value.usersList
                )
            }
            when (page) {
                0 -> {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.primary)
                        .padding(horizontal = 16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Text(text = "Friends", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = defaultTextColor(), modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))
                            Icon(imageVector = Icons.Filled.ChevronRight, tint = defaultIconColor(), contentDescription = "", modifier = Modifier.padding(start = 12.dp, top = 4.dp))
                        }
                        FriendList(items = viewState.value.friendList, friendType = FriendType.ALREADY_FRIEND, onAcceptPending = {
                            viewModel.acceptFriend(it)
                        }, onRefusePending = {
                            viewModel.declineFriend(it)
                        }, onDeleteFriend = {
                            viewModel.deleteFriend(it)
                        })
                    }
                }
                1 -> {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.primary)
                        .padding(horizontal = 16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Icon(imageVector = Icons.Filled.ChevronLeft, tint = defaultIconColor(), contentDescription = "",  modifier = Modifier.padding(end = 12.dp, top = 4.dp))
                            Text(text = "Incoming Friend Requests", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = defaultTextColor(), modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))
                            Icon(imageVector = Icons.Filled.ChevronRight, tint = defaultIconColor(), contentDescription = "", modifier = Modifier.padding(start = 12.dp, top = 4.dp))
                        }
                        FriendList(items = viewState.value.incomingList, friendType = FriendType.INCOMING, onAcceptPending = {
                            viewModel.acceptFriend(it)
                        }, onRefusePending = {
                            viewModel.declineFriend(it)
                        }, onDeleteFriend = {
                            viewModel.deleteFriend(it)
                        })
                    }
                }
                2 -> {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.primary)
                        .padding(horizontal = 16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Icon(imageVector = Icons.Filled.ChevronLeft, tint = defaultIconColor(), contentDescription = "",  modifier = Modifier.padding(end = 12.dp, top = 4.dp))
                            Text(text = "Outgoing Friend Requests", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = defaultTextColor(), modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))
                        }
                        FriendList(items = viewState.value.outgoingList, friendType = FriendType.OUTGOING, onAcceptPending = {
                            viewModel.acceptFriend(it)
                        }, onRefusePending = {
                            viewModel.declineFriend(it)
                        }, onDeleteFriend = {
                            viewModel.deleteFriend(it)
                        })
                    }
                }
            }
        }
    }
}