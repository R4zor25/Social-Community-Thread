package hu.bme.aut.android.socialcommunitythread.ui.userprofile

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.domain.interactors.AuthInteractor
import hu.bme.aut.android.socialcommunitythread.ui.camera.getOutputDirectory
import hu.bme.aut.android.socialcommunitythread.ui.theme.PrimaryLight
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultIconColor
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultTextColor
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.circleimage.CircleImage
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.navigationdrawer.NavigationDrawer
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.topbar.TopBar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.io.ByteArrayOutputStream

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun UserProfileScreen(navController: NavController, viewModel: UserProfileViewModel) {
    val viewState = viewModel.uiState.collectAsState().value
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
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
                val stream = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.PNG, 90, stream)
                val image = stream.toByteArray()
                viewModel.updateProfileImage(image)
                cameraImage = it
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
                val stream = ByteArrayOutputStream()
                cameraImage?.compress(Bitmap.CompressFormat.PNG, 90, stream)
                val image = stream.toByteArray()
                viewModel.updateProfileImage(image)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    LaunchedEffect("key") {
        viewModel.oneShotEvent
            .onEach {
                when (it) {
                    is UserProfileOneShotEvent.ShowToastMessage -> {
                        Toast.makeText(context, it.errorText, Toast.LENGTH_LONG).show()
                    }

                    is UserProfileOneShotEvent.UserUpdateSuccess -> {
                        navController.navigateUp()
                    }
                }
            }.collect()
    }

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colors.primary),
        scaffoldState = scaffoldState,
        topBar = {
            TopBar("Profile", leftIconImage = Icons.Filled.ArrowBack, rightIconImage = {
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
            }, scope, scaffoldState, {
                navController.navigateUp()
            }, {})
        },
        drawerBackgroundColor = MaterialTheme.colors.primary,
        drawerContent = {
            NavigationDrawer(navController = navController, scaffoldState = scaffoldState, scope)
        },
    ) {
        Surface(color = MaterialTheme.colors.primary, shape = RoundedCornerShape(40.dp).copy(ZeroCornerSize, ZeroCornerSize)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth(0.8f)
                        .padding(top = 8.dp)
                ) {
                    Row() {
                        Text("Username: " + AuthInteractor.currentLoggedInUser!!.userName, color = defaultTextColor())
                        Spacer(Modifier.height(8.dp))
                        Text("Email: " + AuthInteractor.currentLoggedInUser!!.email, color = defaultTextColor())
                    }
                    Spacer(modifier = Modifier.padding(top = 8.dp))
                    Column() {
                        Box() {
                            CircleImage(
                                imageSize = 80, cameraImage = if (cameraImage == null
                                    && AuthInteractor.currentLoggedInUser?.profileImage != null
                                    && AuthInteractor.currentLoggedInUser?.profileImage?.size!! > 0) {
                                    BitmapFactory.decodeByteArray(AuthInteractor.currentLoggedInUser!!.profileImage, 0, AuthInteractor.currentLoggedInUser!!.profileImage!!.size)
                                } else cameraImage
                            )
                            Box(Modifier.size(95.dp)) {
                                IconButton(modifier = Modifier
                                    .size(40.dp)
                                    .align(Alignment.BottomEnd)
                                    .semantics { contentDescription = "TestLeftIcon" },
                                    onClick = {
                                        expanded = !expanded
                                    }
                                ) {
                                    Icon(Icons.Filled.Camera, contentDescription = "", tint = defaultIconColor())
                                }
                                DropdownMenu(
                                    expanded = expanded,
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .background(MaterialTheme.colors.secondary),
                                    onDismissRequest = { expanded = false }
                                ) {
                                    DropdownMenuItem(modifier = Modifier.background(MaterialTheme.colors.secondary), onClick = {
                                        expanded = false
                                        if (mediaPermissionState.allPermissionsGranted) {
                                            launcher.launch()
                                        } else {
                                            mediaPermissionState.launchMultiplePermissionRequest()
                                        }
                                    }) {
                                        Text("Camera", color = defaultTextColor())
                                    }
                                    Divider()
                                    DropdownMenuItem(modifier = Modifier.background(MaterialTheme.colors.secondary), onClick = {
                                        expanded = false
                                        if (mediaPermissionState.allPermissionsGranted) {
                                            galleryLauncher.launch("image/*")
                                        } else {
                                            mediaPermissionState.launchMultiplePermissionRequest()
                                        }
                                    }) {
                                        Text("Gallery", color = defaultTextColor())
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = {
                                viewModel.updateUserProfile()
                            },
                            shape = CircleShape,
                            modifier = Modifier
                                .width(130.dp)
                                .height(50.dp),
                            colors = ButtonDefaults.textButtonColors(
                                backgroundColor = MaterialTheme.colors.secondary
                            ),
                            contentPadding = PaddingValues(4.dp)
                        ) {
                            Text(text = "Save", fontSize = 24.sp, color = defaultTextColor())
                        }
                    }
                }
            }
        }
    }
}