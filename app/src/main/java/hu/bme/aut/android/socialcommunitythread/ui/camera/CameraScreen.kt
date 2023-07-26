package hu.bme.aut.android.socialcommunitythread.ui.camera

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Icon
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AutoDelete
import androidx.compose.material.icons.sharp.Check
import androidx.compose.material.icons.sharp.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CameraScreen(navController: NavController){
    val bitmap =  remember {
        mutableStateOf<Bitmap?>(null)
    }
    val context = LocalContext.current
    Surface(modifier = Modifier.fillMaxSize()) {
        if(bitmap.value == null) {
            CameraView(onImageCaptured = { uri, fromGallery ->
                uri?.let {
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap.value = MediaStore.Images
                            .Media.getBitmap(context.contentResolver, it)

                    } else {
                        val source = ImageDecoder
                            .createSource(context.contentResolver, it)
                        bitmap.value = ImageDecoder.decodeBitmap(source)
                    }
                }

            }, onError = { imageCaptureException ->
                // scope.launch {
                //   scaffoldState.snackbarHostState.showSnackbar("An error occurred while trying to take a picture")
                // }
            })
        }else {
            bitmap.value?.let { it1 ->
                    Column(verticalArrangement = Arrangement.SpaceEvenly) {
                        Image(bitmap = it1.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.wrapContentSize())
                        Row() {

                        }
                        Row() {
                            IconButton(onClick = { navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("bitmap", it1)
                                navController.navigateUp() }) {
                                Icon(imageVector = Icons.Sharp.Check, tint = Color.Magenta, contentDescription = "check")
                            }
                            IconButton(onClick = { bitmap.value = null }) {
                                Icon(imageVector = Icons.Sharp.Delete, tint = Color.Magenta, contentDescription = "check")
                            }
                        }
                }
            }
        }
    }
}