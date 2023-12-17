package hu.bme.aut.android.socialcommunitythread.ui.createthread

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
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.domain.interactors.AuthInteractor
import hu.bme.aut.android.socialcommunitythread.domain.model.TopicThread
import hu.bme.aut.android.socialcommunitythread.ui.camera.getOutputDirectory
import hu.bme.aut.android.socialcommunitythread.ui.theme.PrimaryLight
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultIconColor
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultTextColor
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.CustomTextField
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.circleimage.CircleImage
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.gradient
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.navigationdrawer.NavigationDrawer
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.topbar.TopBar
import hu.bme.aut.android.socialcommunitythread.util.tagrecommender.ImageAnalyzer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalGetImage
@OptIn(ExperimentalPermissionsApi::class)
@ExperimentalComposeUiApi
@Composable
fun CreateThreadScreen(navController: NavController) {
    val viewModel = hiltViewModel<CreateThreadViewModel>()
    val viewState = viewModel.uiState.collectAsState()
    val analyzer = ImageAnalyzer()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val contentResolver: ContentResolver = context.contentResolver
    val localFocusManager = LocalFocusManager.current
    var cameraImage: Bitmap? by remember { mutableStateOf(null) }
    val mediaPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )
    )
    var expanded by remember { mutableStateOf(false) }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            if (it != null) {
                //anal.analyze(it!!)
                viewModel.onThreadImageChange(it)
                cameraImage = it
            }
        }

    LaunchedEffect("key") {
        viewModel.oneShotEvent
            .onEach {
                when (it) {
                    is CreateThreadOneShotEvent.ThreadCreated -> {
                        navController.navigateUp()
                    }

                    is CreateThreadOneShotEvent.ShowToastMessage -> Toast.makeText(context, it.errorText, Toast.LENGTH_LONG).show()
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
                if (cameraImage != null) {
                    viewModel.onThreadImageChange(cameraImage!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    Scaffold(
        modifier = Modifier.background(MaterialTheme.colors.primary),
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(stringResource(R.string.create_thread), leftIconImage = Icons.Filled.ArrowBack, rightIconImage = {
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
        drawerBackgroundColor = MaterialTheme.colors.primary,
        drawerContent = {
            NavigationDrawer(
                navController = navController,
                scaffoldState = scaffoldState, scope
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primary)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "Thread name", color = defaultTextColor(), fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 16.dp, top = 16.dp))
            CustomTextField(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colors.secondary, MaterialTheme.colors.secondary
                            )
                        ),
                        shape = RoundedCornerShape(40.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 0.dp),
                trailingIcon = {
                    IconButton(onClick = {
                        viewModel.onThreadNameTextChange("")
                        localFocusManager.clearFocus()
                    }) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Text),
                onTextChange = { viewModel.onThreadNameTextChange(it) },
                text = viewState.value.threadName,
                hint = "Thread Name",
                getPasswordVisibility = { true }
            )
            Spacer(modifier = Modifier.padding(top = 12.dp))
            Text(text = "Description", color = defaultTextColor(), fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 16.dp))
            CustomTextField(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colors.secondary, MaterialTheme.colors.secondary
                            )
                        ),
                        shape = RoundedCornerShape(40.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 0.dp),
                trailingIcon = {
                    IconButton(onClick = {
                        viewModel.onDescriptionTextChange("")
                        localFocusManager.clearFocus()
                    }) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Text),
                onTextChange = { viewModel.onDescriptionTextChange(it) },
                text = viewState.value.description,
                hint = "Description",
                getPasswordVisibility = { true }
            )
            Spacer(modifier = Modifier.padding(top = 12.dp))
            Text(text = "TopicThread Image", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 16.dp))
            Row(
                modifier = Modifier
                    .padding(start = 16.dp)
            ) {
                Box() {
                    CircleImage(imageSize = 120, cameraImage = cameraImage)
                    Box(Modifier.size(120.dp)) {
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
                                Text("Camera", color = defaultIconColor())
                            }
                            Divider()
                            DropdownMenuItem(modifier = Modifier.background(MaterialTheme.colors.secondary), onClick = {
                                expanded = false
                                galleryLauncher.launch("image/*")
                            }) {
                                Text("Gallery", color = defaultTextColor())
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    if (viewState.value.threadName.isNotBlank() && viewState.value.description.isNotBlank()) {
                        viewModel.createThread()
                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                shape = CircleShape,
                modifier = Modifier
                    .width(300.dp)
                    .height(70.dp)
                    .padding(start = 16.dp, top = 16.dp),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = MaterialTheme.colors.secondary
                ),
                contentPadding = PaddingValues(4.dp)
            ) {
                Text(text = "Create Thread", fontSize = 24.sp, color = Color.Black)
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
@Preview
fun CreateThreadScreenPreview() {
    //CreateThreadScreen(navController = rememberNavController())
} 
