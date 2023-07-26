package hu.bme.aut.android.socialcommunitythread.ui.userprofile

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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.ui.camera.getOutputDirectory
import hu.bme.aut.android.socialcommunitythread.ui.theme.Beige
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.circleimage.CircleImage
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.navigationdrawer.NavigationDrawer
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.topbar.TopBar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun UserProfileScreen(navController: NavController) {
    val viewModel = hiltViewModel<UserProfileViewModel>()
    //val viewState by remember{ mutableStateOf(viewModel.viewState)}
    val viewState = viewModel.viewState
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var capybara: Int? by rememberSaveable { mutableStateOf(R.drawable.capybara) }
    var cameraImage: Bitmap? by remember { mutableStateOf(null) }
    val mediaPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )
    )
    val contentResolver: ContentResolver = context.contentResolver
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            if (it != null) {
                cameraImage = it
                capybara = null
            }
        }
    var expanded by remember { mutableStateOf(false) }

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

    LaunchedEffect("key") {
        viewModel.oneShotEvent
            .onEach {
                when (it) {
                    is UserProfileOneShotEvent.ShowToastMessage -> TODO()
                }
            }.collect()
    }

    Scaffold(
        modifier = Modifier.background(Color.White),
        scaffoldState = scaffoldState,
        topBar = {
            TopBar("Profile", leftIconImage = Icons.Filled.ArrowBack, rightIconImage = {
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
        Surface(color = Beige, shape = RoundedCornerShape(40.dp).copy(ZeroCornerSize, ZeroCornerSize)) {
            Row(Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                Column(Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 8.dp)) {
                    if(!viewState.editMode) {
                        Row() {
                            Text("Username: " + viewState.username.value)
                            Spacer(Modifier.padding(end = 4.dp))
                            Icon(Icons.Filled.Edit, contentDescription = "", tint = Color.Black)
                        }
                        Spacer(modifier = Modifier.padding(top = 8.dp))
                        Row() {
                            Text("E-mail: " + viewState.email.value)
                            Spacer(Modifier.padding(end = 4.dp))
                            IconButton(onClick = {
                                //viewState.editMode = !viewState.value.editMode
                                viewState.username.value = viewState.username.value + "j"
                                viewState.email.value = viewState.email.value + "i"
                            }) {
                                Icon(Icons.Filled.Edit, contentDescription = "", tint = Color.Black)
                            }
                        }
                    }else{
                        //TODO edit mode
                    }
                }
                Column() {
                    Box() {
                        CircleImage(imageSize = 80, imageResource = capybara, cameraImage = cameraImage)
                        Box(Modifier.size(95.dp)) {
                            IconButton(modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.BottomEnd)
                                .semantics { contentDescription = "TestLeftIcon" },
                                onClick = {
                                    expanded = !expanded
                                }
                            ) {
                                Icon(Icons.Filled.Camera, contentDescription = "", tint = Color.Black)
                            }
                            DropdownMenu(
                                expanded = expanded,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .background(Beige),
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(modifier = Modifier.background(Beige), onClick = {
                                    expanded = false
                                    if (mediaPermissionState.allPermissionsGranted) {
                                        launcher.launch()
                                    } else {
                                        mediaPermissionState.launchMultiplePermissionRequest()
                                    }
                                }) {
                                    Text("Camera")
                                }
                                Divider()
                                DropdownMenuItem(modifier = Modifier.background(Beige), onClick = {
                                    expanded = false
                                    if (mediaPermissionState.allPermissionsGranted) {
                                        if (context.getOutputDirectory().listFiles()?.isNotEmpty() == true) {
                                            galleryLauncher.launch("image/*")
                                        }
                                    }else{
                                        mediaPermissionState.launchMultiplePermissionRequest()
                                    }
                                }) {
                                    Text("Gallery")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}