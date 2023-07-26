package hu.bme.aut.android.socialcommunitythread.ui.createpost

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.domain.model.PostType
import hu.bme.aut.android.socialcommunitythread.ui.camera.getOutputDirectory
import hu.bme.aut.android.socialcommunitythread.ui.theme.Beige
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.CustomTextField
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.gradient
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.navigationdrawer.NavigationDrawer
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.posttyperowitem.PostTypeRowItem
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.topbar.TopBar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun CreatePostScreen(navController: NavController) {
    val viewModel = hiltViewModel<CreatePostViewModel>()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val localFocusManager = LocalFocusManager.current
    val contentResolver: ContentResolver = context.contentResolver
    var postTitle by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var postType by rememberSaveable { mutableStateOf(PostType.TEXT) }
    var capybara: Int? by rememberSaveable { mutableStateOf(R.drawable.capybara) }
    var cameraImage: Bitmap? by remember { mutableStateOf(null) }
    var expanded by remember { mutableStateOf(false) }

    val mediaPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )
    )
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            if (it != null) {
                cameraImage = it
                capybara = null
            }
        }

    LaunchedEffect("key") {
        viewModel.oneShotEvent
            .onEach {
                when (it) {
                    is CreatePostOneShotEvent.ShowToastMessage -> TODO()
                    is CreatePostOneShotEvent.ThreadCreated -> TODO()
                }
            }.collect()
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                cameraImage = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(contentResolver, uri)
                } else {
                    val source: ImageDecoder.Source = ImageDecoder.createSource(contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            capybara = null
        }
    }

    Scaffold(
        modifier = Modifier.background(Color.White),
        scaffoldState = scaffoldState,
        topBar = {
            TopBar("Create Post", leftIconImage = Icons.Filled.ArrowBack, rightIconImage = {
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
        bottomBar = {
            CreatePostBottomBar(postType = postType, onClick = { type -> postType = type })
        }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {
            Text(text = "Post Title", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 16.dp, top = 16.dp))
            CustomTextField(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .background(
                        brush = gradient,
                        shape = RoundedCornerShape(40.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 0.dp),
                trailingIcon = {
                    IconButton(onClick = {
                        postTitle = ""
                        localFocusManager.clearFocus()
                    }) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Text),
                onTextChange = { postTitle = it },
                text = postTitle,
                hint = "Post Title",
                passwordVisibility = true,
                getPasswordVisibility = { true }
            )
            Spacer(modifier = Modifier.padding(top = 12.dp))
            when (postType) {
                PostType.TEXT -> {
                    CustomTextField(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .background(
                                brush = gradient,
                                shape = RoundedCornerShape(40.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 0.dp),
                        trailingIcon = {
                            IconButton(onClick = {
                                description = ""
                                localFocusManager.clearFocus()
                            }) {
                                Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Text),
                        onTextChange = { description = it },
                        text = description,
                        hint = "Description",
                        passwordVisibility = true,
                        getPasswordVisibility = { true }
                    )
                }
                PostType.IMAGE -> {
                    IconButton(onClick = {
                        if (mediaPermissionState.allPermissionsGranted) {
                            if (context.getOutputDirectory().listFiles()?.isNotEmpty() == true) {
                                galleryLauncher.launch("image/*")
                            }
                        }else{
                            mediaPermissionState.launchMultiplePermissionRequest()
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.AddToPhotos,
                            contentDescription = "",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(start = 16.dp)
                                .border(width = 2.dp, brush = Brush.linearGradient(colors = listOf(
                                    Color.Black, Color.Black
                                )), shape = RoundedCornerShape(15.dp)),
                            tint = Color.Black)
                    }
                }
                PostType.GIF -> {
                    IconButton(onClick = {
                        if (mediaPermissionState.allPermissionsGranted) {
                            if (context.getOutputDirectory().listFiles()?.isNotEmpty() == true) {
                                galleryLauncher.launch("image/gif")
                            }
                        }else{
                            mediaPermissionState.launchMultiplePermissionRequest()
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.Gif,
                            contentDescription = "",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(start = 16.dp)
                                .border(width = 2.dp, brush = Brush.linearGradient(colors = listOf(
                                    Color.Black, Color.Black
                                )), shape = RoundedCornerShape(15.dp)),
                            tint = Color.Black)
                    }
                }
                PostType.VIDEO -> {
                    IconButton(onClick = {
                        if (mediaPermissionState.allPermissionsGranted) {
                            if (context.getOutputDirectory().listFiles()?.isNotEmpty() == true) {
                                galleryLauncher.launch("video/*")
                            }
                        }else{
                            mediaPermissionState.launchMultiplePermissionRequest()
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.VideoCall,
                            contentDescription = "",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(start = 16.dp)
                                .border(width = 2.dp, brush = Brush.linearGradient(colors = listOf(
                                    Color.Black, Color.Black
                                )), shape = RoundedCornerShape(15.dp)),
                            tint = Color.Black)
                    }
                }
                PostType.AUDIO -> {}
            }

            Spacer(modifier = Modifier.padding(top = 12.dp))
            Button(
                onClick = { }, shape = CircleShape,
                modifier = Modifier
                    .width(130.dp)
                    .height(50.dp)
                    .padding(start = 16.dp),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = Beige
                ),
                contentPadding = PaddingValues(4.dp)
            ) {
                Text(text = "Post", fontSize = 24.sp, color = Color.Black)
            }
        }
    }


}


@Composable
fun CreatePostBottomBar(postType: PostType, onClick: (type: PostType) -> Unit) {
    Column(modifier = Modifier.padding(bottom = 0.dp)) {
        PostTypeRowItem(imageVector = Icons.Filled.ShortText, title = "Text", onClick = {
            onClick.invoke(PostType.TEXT)
        }, selected = postType == PostType.TEXT)
        Divider()
        PostTypeRowItem(imageVector = Icons.Filled.Image, title = "Image", onClick = {
            onClick.invoke(PostType.IMAGE)
        }, selected = postType == PostType.IMAGE)
        Divider()
        PostTypeRowItem(imageVector = Icons.Filled.Gif, title = "Gif", onClick = {
            onClick.invoke(PostType.GIF)
        }, selected = postType == PostType.GIF)
        Divider()
        PostTypeRowItem(imageVector = Icons.Filled.Videocam, title = "Video", onClick = {
            onClick.invoke(PostType.VIDEO)
        }, selected = postType == PostType.VIDEO)
    }
}