package hu.bme.aut.android.socialcommunitythread.ui.uicomponent.chat

import android.Manifest
import android.app.Dialog
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import hu.bme.aut.android.socialcommunitythread.domain.model.PersonalChatConversation
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultIconColor
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultTextColor
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.circleimage.CircleImage
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
@ExperimentalComposeUiApi
@Composable
fun CreateConversationDialog(
    onDismiss: () -> Unit,
    onNegativeClick: () -> Unit,
    onPositiveClick: (PersonalChatConversation) -> Unit,
) {
    var conversationName by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val mediaPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )
    )
    var expanded by remember { mutableStateOf(false) }
    val contentResolver: ContentResolver = context.contentResolver

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            if (it != null) {
                bitmap = it
            }
        }
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                bitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(contentResolver, uri)
                } else {
                    val source: ImageDecoder.Source = ImageDecoder.createSource(contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {

        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(12.dp),
            backgroundColor = MaterialTheme.colors.primary
        ) {
            Column(modifier = Modifier.padding(12.dp)) {

                Text(
                    text = "Add Friend",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(8.dp),
                    color = defaultTextColor()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Color Selection
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        readOnly = true,
                        value = conversationName,
                        onValueChange = { conversationName = it },
                        label = { Text("Username") },
                        leadingIcon = { Icon(Icons.Filled.Person, "") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colors.secondary,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.Black
                        )
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(start = 16.dp)
                ) {
                    Box() {
                        CircleImage(imageSize = 120, cameraImage = bitmap)
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
                                DropdownMenuItem(modifier = Modifier.background(MaterialTheme.colors.primary), onClick = {
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
                                DropdownMenuItem(modifier = Modifier.background(MaterialTheme.colors.primary), onClick = {
                                    expanded = false
                                    galleryLauncher.launch("image/*")
                                }) {
                                    Text("Gallery")
                                }
                            }
                        }
                    }
                }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        TextButton(onClick = onNegativeClick) {
                            Text(text = "Cancel", color = defaultTextColor())
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        TextButton(onClick = {
                            if (conversationName.isNullOrBlank()) {
                                Toast.makeText(context, "Name cannot be empty", Toast.LENGTH_LONG).show()
                            } else {
                                val stream = ByteArrayOutputStream()
                                bitmap?.compress(Bitmap.CompressFormat.PNG, 90, stream)
                                val image = stream.toByteArray()
                                onPositiveClick(
                                    PersonalChatConversation().apply {
                                        this.conversationName = conversationName
                                        this.conversationImage = image
                                    }
                                )
                            }
                        }) {
                            Text(text = "Ok", color = defaultTextColor())
                        }
                    }
                }
            }
        }
    }