package hu.bme.aut.android.socialcommunitythread.ui.threaddetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.domain.model.Post
import hu.bme.aut.android.socialcommunitythread.domain.model.PostType
import hu.bme.aut.android.socialcommunitythread.domain.model.TopicThread
import hu.bme.aut.android.socialcommunitythread.domain.model.VoteType
import hu.bme.aut.android.socialcommunitythread.ui.theme.Beige
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.CircularIndeterminateProgressBar
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.threadrowitem.ThreadRowItem
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.circleimage.CircleImage
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.topbar.TopBar
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.comment.CommentRowItem
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.navigationdrawer.NavigationDrawer
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@ExperimentalPermissionsApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ThreadDetailsScreen(navController: NavController, postId: Int) {
    val viewModel = hiltViewModel<ThreadDetailsViewModel>()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val localFocusManager = LocalFocusManager.current
    var message by rememberSaveable { mutableStateOf("") }
    var post: Post by remember {
        mutableStateOf(Post(id = 0, TopicThread(0, "BME_AUT", ""), "Alfred", "2 hours ago", 0, 0, "",
            false, PostType.VIDEO, VoteType.CLEAR, "", "", videoUrl = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"))
    }
    val listState = rememberLazyListState()

    LaunchedEffect("key") {
        viewModel.oneShotEvent
            .onEach {

            }.collect {
                when (it) {
                    ThreadDetailsOneShotEvent.AcquireId -> {
                        viewModel.id = postId
                        viewModel.onAction(ThreadDetailsUiAction.OnInit())
                    }
                    is ThreadDetailsOneShotEvent.ThreadItemAcquired -> {
                        post = it.post
                    }
                    is ThreadDetailsOneShotEvent.ShowToastMessage -> TODO()
                    else -> {}
                }
            }
    }
    Scaffold(
        modifier = Modifier.background(Color.White),
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(post.title, leftIconImage = Icons.Filled.ArrowBack, rightIconImage = {
                CircleImage(imageSize = 48, imageResource = R.drawable.capybara)
            }, scope, scaffoldState, {
                navController.navigateUp()
            }, {})
        },
        drawerContent = {
            NavigationDrawer(navController = navController, scaffoldState = scaffoldState, scope)
        },
        drawerBackgroundColor = Color.White,
    ) {
        BottomSheetScaffold(
            scaffoldState = bottomSheetScaffoldState,
            sheetContent = {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Beige)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = if (bottomSheetScaffoldState.bottomSheetState.isCollapsed)
                        Alignment.Top
                    else
                        Alignment.CenterVertically) {
                        OutlinedTextField(modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp)
                            .fillMaxWidth(0.8f), value = message, onValueChange = { message = it },
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = Color.Black,
                                cursorColor = Color.Black,
                                focusedIndicatorColor = Beige,
                                unfocusedIndicatorColor = Beige,
                                focusedLabelColor = Color.Black,
                                unfocusedLabelColor = Color.Black,
                                trailingIconColor = Color.Black
                            ),
                            label = { Text(text = "Comment", color = Color.Black) },
                            trailingIcon = {
                                if (message.isNotBlank()) {
                                    IconButton(onClick = {
                                        if (message.isNotBlank()) {
                                            viewModel.onAction(ThreadDetailsUiAction.SendComment(message))
                                            message = ""
                                            localFocusManager.clearFocus()
                                            scope.launch {
                                                    bottomSheetScaffoldState.bottomSheetState.collapse()
                                            }
                                        }
                                    })
                                    {
                                        Icon(imageVector = Icons.Filled.Send, contentDescription = stringResource(R.string.send))
                                    }
                                }
                            })
                        IconButton(modifier = Modifier.semantics { contentDescription = "expand" },
                            onClick = {
                                scope.launch {
                                    if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                        bottomSheetScaffoldState.bottomSheetState.expand()
                                    } else {
                                        bottomSheetScaffoldState.bottomSheetState.collapse()
                                    }
                                }
                            }
                        ) {
                            Icon(imageVector = if (bottomSheetScaffoldState.bottomSheetState.isCollapsed)
                                Icons.Filled.KeyboardArrowUp
                            else
                                Icons.Filled.KeyboardArrowDown, modifier = Modifier.size(30.dp), contentDescription = "")
                        }
                    }
                }
            }, sheetPeekHeight = 60.dp
        ) {
            if (!viewModel.viewState.isLoading) {
                LazyColumn(state = listState) {
                    item {
                        ThreadRowItem(post, onItemClick = {},
                            onSaveLaterClick = {
                                if (it.isSaved)
                                    viewModel.onAction(ThreadDetailsUiAction.SavePostUiAction(it))
                                else
                                    viewModel.onAction(ThreadDetailsUiAction.UnsavePostUiAction(it))
                            })
                    }
                    items(viewModel.viewState.commentList.size) { i ->
                        val item = viewModel.viewState.commentList[i]
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            CommentRowItem(item)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.padding(bottom = 60.dp))
                    }
                }
            } else {
                CircularIndeterminateProgressBar(isDisplayed = viewModel.viewState.isLoading)
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun CheckCameraPermission() {
    val mediaPermissionState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
        )
    )

    if (mediaPermissionState.allPermissionsGranted) {
        Text("Thanks! I can access your exact location :D")
    } else {
        Column {
            val allPermissionsRevoked =
                mediaPermissionState.permissions.size ==
                        mediaPermissionState.revokedPermissions.size

            val textToShow = if (!allPermissionsRevoked) {
                // If not all the permissions are revoked, it's because the user accepted the COARSE
                // location permission, but not the FINE one.
                "Yay! Thanks for letting me access your approximate location. " +
                        "But you know what would be great? If you allow me to know where you " +
                        "exactly are. Thank you!"
            } else if (mediaPermissionState.shouldShowRationale) {
                // Both location permissions have been denied
                "Getting your exact location is important for this app. " +
                        "Please grant us fine location. Thank you :D"
            } else {
                // First time the user sees this feature or the user doesn't want to be asked again
                "This feature requires location permission"
            }

            val buttonText = if (!allPermissionsRevoked) {
                "Allow precise location"
            } else {
                "Request permissions"
            }

            Text(text = textToShow)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { mediaPermissionState.launchMultiplePermissionRequest() }) {
                Text(buttonText)
            }
        }
    }
}
