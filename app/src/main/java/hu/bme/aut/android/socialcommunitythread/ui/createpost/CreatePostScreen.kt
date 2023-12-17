package hu.bme.aut.android.socialcommunitythread.ui.createpost

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
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
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.domain.dto.PostType
import hu.bme.aut.android.socialcommunitythread.domain.interactors.AuthInteractor
import hu.bme.aut.android.socialcommunitythread.domain.model.Post
import hu.bme.aut.android.socialcommunitythread.domain.model.PostMediaType
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultIconColor
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultTextColor
import hu.bme.aut.android.socialcommunitythread.ui.theme.disabledIconColor
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.CustomTextField
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.videoplayer.VideoPlayer
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.gifimage.GifImage
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.navigationdrawer.NavigationDrawer
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.topbar.TopBar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.io.ByteArrayOutputStream


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun CreatePostScreen(navController: NavController, viewModel: CreatePostViewModel) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val localFocusManager = LocalFocusManager.current
    val contentResolver: ContentResolver = context.contentResolver
    var ContentImage: Bitmap? by remember { mutableStateOf(null) }
    var contentUri: Uri? by rememberSaveable { mutableStateOf(null) }
    val viewState = viewModel.uiState.collectAsState()
    var isEnabled: Boolean by rememberSaveable { mutableStateOf(true) }

    val mediaPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )
    )

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            if (it != null) {
               // isEnabled = false
                val stream = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.PNG, 90, stream)
                val image = stream.toByteArray()
                viewModel.onPostFileChange(image)
                ContentImage = it
            }
        }

    LaunchedEffect("key") {
        viewModel.oneShotEvent
            .onEach {
                when (it) {
                    is CreatePostOneShotEvent.PostCreated -> {
                        navController.navigateUp()
                    }
                    is CreatePostOneShotEvent.ShowToastMessage -> {
                        Toast.makeText(context, it.errorText, Toast.LENGTH_LONG).show()
                    }
                }
            }.collect()
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            contentUri = uri
            if(viewState.value.postType == PostType.IMAGE){
            try {
                ContentImage = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(contentResolver, uri)
                } else {
                    val source: ImageDecoder.Source = ImageDecoder.createSource(contentResolver, uri)
                    ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.RGBA_F16, true)
                }
                if(ContentImage != null){
                    val stream = ByteArrayOutputStream()
                    ContentImage!!.compress(Bitmap.CompressFormat.PNG, 90, stream)
                    val image = stream.toByteArray()
                    viewModel.onPostFileChange(image)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }} else if (viewState.value.postType == PostType.GIF){
                readBytes(context, uri)?.let { viewModel.onPostFileChange(it) }
            } else {
                readBytes(context, uri)?.let { viewModel.onPostFileChange(it) }
                viewModel.setPostType(PostType.VIDEO)
            }
        }
    }

    Scaffold(
        modifier = Modifier.background(Color.White),
        scaffoldState = scaffoldState,
        topBar = {
            TopBar("Create Post", leftIconImage = Icons.Filled.ArrowBack, rightIconImage = {
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
        drawerBackgroundColor = Color.White,
        drawerContent = {
            NavigationDrawer(navController = navController, scaffoldState = scaffoldState, scope)
        },
        bottomBar = {
            CreatePostBottomBar(onClick = { type ->
                when(type){
                    PostMediaType.TEXT -> {
                        viewModel.setPostType(PostType.TEXT)
                    }
                    PostMediaType.CAMERA -> {
                        viewModel.setPostType(PostType.IMAGE)
                        if (mediaPermissionState.allPermissionsGranted) {
                                cameraLauncher.launch()
                        }else{
                            mediaPermissionState.launchMultiplePermissionRequest()
                        }
                    }
                    PostMediaType.GALLERY -> {
                        viewModel.setPostType(PostType.IMAGE)
                        if (mediaPermissionState.allPermissionsGranted) {
                                galleryLauncher.launch("image/*")
                        }else{
                            mediaPermissionState.launchMultiplePermissionRequest()
                        }
                    }
                    PostMediaType.GIF -> {
                        viewModel.setPostType(PostType.GIF)
                        if (mediaPermissionState.allPermissionsGranted) {
                            galleryLauncher.launch("image/gif")
                        }else{
                            mediaPermissionState.launchMultiplePermissionRequest()
                        }
                    }
                    PostMediaType.VIDEO -> {
                        viewModel.setPostType(PostType.VIDEO)
                        if (mediaPermissionState.allPermissionsGranted) {
                            galleryLauncher.launch("video/*")
                        }else{
                            mediaPermissionState.launchMultiplePermissionRequest()
                        }
                    }


                }                          }
                , isEnabled = isEnabled)
        }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            CustomTextField(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 16.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colors.secondary, MaterialTheme.colors.secondary
                            )
                        ),
                        shape = CircleShape
                    )
                    .padding(horizontal = 12.dp, vertical = 0.dp),
                trailingIcon = {
                    IconButton(onClick = {
                        viewModel.onTitleTextChange("")
                        localFocusManager.clearFocus()
                    }) {
                        Icon(imageVector = Icons.Filled.Clear, tint = defaultIconColor(), contentDescription = "Clear")
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Text),
                onTextChange = { viewModel.onTitleTextChange(it) },
                text = viewState.value.postTitle,
                hint = "Post Title",
                getPasswordVisibility = { true }
            )
            Spacer(modifier = Modifier.padding(top = 12.dp))

            CustomTextField(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, bottom = 16.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colors.secondary, MaterialTheme.colors.secondary
                            )
                        ),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 0.dp),
                trailingIcon = {
                    IconButton(onClick = {
                        viewModel.onDescriptionTextChange("")
                        localFocusManager.clearFocus()
                    }) {
                        Icon(imageVector = Icons.Filled.Clear, tint = defaultIconColor(), contentDescription = "Clear")
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Text),
                onTextChange = { viewModel.onDescriptionTextChange(it) },
                text = viewState.value.description,
                hint = "Description",
                getPasswordVisibility = { true }
            )

            Spacer(Modifier.height(12.dp))
            when (viewState.value.postType) {
                PostType.IMAGE -> {
                    ContentImage?.let { it1 ->
                        Image(
                            it1.asImageBitmap(),
                            modifier = Modifier.fillMaxWidth(0.8f),
                            contentDescription = "Picture",
                        )
                        Spacer(modifier = Modifier.padding(top = 12.dp))
                    }
                }
                PostType.GIF -> {
                    GifImage(Modifier.padding(top = 8.dp), contentUri)
                    Spacer(modifier = Modifier.padding(top = 12.dp))
                }
                PostType.VIDEO -> {
                    contentUri?.let { it1 -> VideoPlayer(uri = it1, modifier = Modifier.fillMaxWidth(0.8f)) }
                    Spacer(modifier = Modifier.padding(top = 12.dp))
                }
                PostType.TEXT -> {}
            }



            Spacer(modifier = Modifier.padding(top = 12.dp))
            Button(
                onClick = {
                          viewModel.createPost()
                }, shape = CircleShape,
                modifier = Modifier
                    .width(130.dp)
                    .height(50.dp),
                    //.padding(start = 16.dp, bottom = 100.dp),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = MaterialTheme.colors.secondary
                ),
                contentPadding = PaddingValues(4.dp)
            ) {
                Text(text = "Post", fontSize = 24.sp, color = defaultTextColor())
            }
        }
    }
}




@Composable
fun CreatePostBottomBar(onClick: (type: PostMediaType) -> Unit, isEnabled : Boolean) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.secondary)
        .padding(0.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { onClick(PostMediaType.TEXT) }, Modifier.weight(1f)) {
            Icon(Icons.Filled.ShortText, "", tint = if(isEnabled) defaultIconColor() else disabledIconColor(),
                modifier = Modifier.padding(12.dp))
        }
        Divider(color = Color(0xAA555555), modifier = Modifier
            .width(2.dp)
            .height(40.dp))
        IconButton(onClick = { onClick(PostMediaType.CAMERA) }, Modifier.weight(1f)) {
            Icon(Icons.Filled.AddAPhoto, "", tint = if(isEnabled) defaultIconColor() else disabledIconColor(),
                modifier = Modifier.padding(12.dp))
        }
        Divider(color = Color(0xAA555555), modifier = Modifier
            .width(2.dp)
            .height(40.dp))

        IconButton(onClick = { onClick(PostMediaType.GALLERY) }, Modifier.weight(1f)) {
            Icon(Icons.Filled.Image, "", tint = if(isEnabled) defaultIconColor() else disabledIconColor(),
                modifier = Modifier.padding(12.dp))
        }

        Divider(color = Color(0xAA555555), modifier = Modifier
            .width(2.dp)
            .height(40.dp))

        IconButton(onClick = { onClick(PostMediaType.GIF) }, Modifier.weight(1f)) {
            Icon(Icons.Filled.Gif, "", tint = if(isEnabled) defaultIconColor() else disabledIconColor(),
                modifier = Modifier.padding(12.dp))
        }

        Divider(color = Color(0xAA555555), modifier = Modifier
            .width(2.dp)
            .height(40.dp))

        IconButton(onClick = { onClick(PostMediaType.VIDEO) }, Modifier.weight(1f)) {
            Icon(Icons.Filled.VideoLibrary, "", tint = if(isEnabled) defaultIconColor() else disabledIconColor(),
                modifier = Modifier.padding(12.dp))
        }
    }
}



private fun readBytes(context: Context, uri: Uri): ByteArray? =
    context.contentResolver.openInputStream(uri)?.use { it.buffered().readBytes() }


@Preview
@Composable
fun BottomBarPreview(){
    CreatePostBottomBar(onClick = {}, true)
}