package hu.bme.aut.android.socialcommunitythread.ui.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultIconColor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatDetailsBottomBar(messageText: String, onTextChange : (String) -> Unit,  sendMessage : (String) -> Unit, ) {
    Row(){
        CustomTextField(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colors.secondary, MaterialTheme.colors.secondary
                        )
                    ),
                    shape = CircleShape
                )
                .padding(horizontal = 12.dp, vertical = 0.dp),
            leadingIcon = {
                IconButton(onClick = { sendMessage(messageText) }) {
                    Icon(Icons.Filled.Send, tint = defaultIconColor(), contentDescription =  "")
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Text),
            onTextChange = { onTextChange(it) },
            text = messageText,
            hint = "Message",
            getPasswordVisibility = { true }
        )

    }
}

@Preview
@Composable
fun ChatDetailsBottomBarPreview(){
    ChatDetailsBottomBar(messageText = "", {}, {})
}