package hu.bme.aut.android.socialcommunitythread.ui.postdetails

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
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.domain.dto.PersonalThreadPost
import hu.bme.aut.android.socialcommunitythread.domain.interactors.AuthInteractor
import hu.bme.aut.android.socialcommunitythread.ui.theme.PrimaryLight
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultIconColor
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.CircularIndeterminateProgressBar
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.threadrowitem.ThreadRowItem
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.circleimage.CircleImage
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.topbar.TopBar
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.comment.CommentRowItem
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.createcommentdialog.CreateCommentDialog
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.navigationdrawer.NavigationDrawer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalPermissionsApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostDetailsScreen(navController: NavController, viewModel: PostDetailsViewModel) {
    val viewState = viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val localFocusManager = LocalFocusManager.current
    val listState = rememberLazyListState()
    var showWriteCommentDialog by remember { mutableStateOf(false) }

    LaunchedEffect("key") {
        viewModel.oneShotEvent
            .onEach {
                    when (it) {
                        is PostDetailsOneShotEvent.ShowToastMessage -> Toast.makeText(context, it.errorText, Toast.LENGTH_LONG).show()
                    }
            }.collect()
    }
    Scaffold(
        modifier = Modifier.background(Color.White),
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(viewState.value.post.title, leftIconImage = Icons.Filled.ArrowBack, rightIconImage = {
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
            }, scope, scaffoldState, {
                navController.navigateUp()
            }, {})
        },
        drawerContent = {
            NavigationDrawer(navController = navController, scaffoldState = scaffoldState, scope)
        },
        drawerBackgroundColor = Color.White,
        floatingActionButton = {
            Button(onClick = {
                showWriteCommentDialog = true
            }, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)) {
                Icon(imageVector = Icons.Filled.Add, tint = defaultIconColor(), contentDescription = "")
            }
        }
    ) {
        if (showWriteCommentDialog) {
            CreateCommentDialog(onDismiss = { showWriteCommentDialog = false },
                onNegativeClick = { showWriteCommentDialog = false },
                onPositiveClick = {
                    showWriteCommentDialog = false
                    viewModel.sendComment(it)
                })
        }
        if (!viewState.value.isLoading) {
            LazyColumn(state = listState, modifier = Modifier.background(MaterialTheme.colors.primary)) {
                item {
                    ThreadRowItem(viewState.value.post, onItemClick = {},
                        onSaveLaterClick = {
                            if (it.isSavedByUser)
                                viewModel.unsavePost()
                            else
                                viewModel.savePost()
                        },
                        onUpVote = {
                            viewModel.upvotePost()
                        },
                        onDownVote = {
                            viewModel.downvotePost()
                        })
                }
                items(viewState.value.post.comments.size) { i ->
                    val item = viewState.value.post.comments[i]
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        CommentRowItem(item, upvote = {
                            viewModel.upvoteComment(it)
                        }, downvote = {
                            viewModel.downvoteComment(it)
                        })
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                item {
                    Spacer(modifier = Modifier.padding(bottom = 60.dp))
                }
            }
        } else {
            CircularIndeterminateProgressBar(isDisplayed = viewState.value.isLoading)
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
