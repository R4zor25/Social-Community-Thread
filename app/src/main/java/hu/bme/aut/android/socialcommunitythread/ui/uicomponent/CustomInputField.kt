package hu.bme.aut.android.socialcommunitythread.ui.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.sharp.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.android.socialcommunitythread.ui.theme.Beige

val gradient = Brush.horizontalGradient(
    colors = listOf(
        Beige, Beige
    )
)

@ExperimentalComposeUiApi
@Preview
@Composable
fun InputPreview(){
    CustomTextField(
        modifier = Modifier
            .background(
                brush = gradient,
                shape = CircleShape
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        leadingIcon = { Icon(Icons.Filled.Email, "contentDescription") },
        trailingIcon =  { Icon(Icons.Filled.Clear, "contentDescription") },
        onTextChange = {},
        text = "",
        hint = "Email",
        getPasswordVisibility = {true},
        passwordVisibility = true
    )
}


@ExperimentalComposeUiApi
@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    onTextChange: (String) -> Unit,
    text: String? = "",
    hint: String?,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
    getPasswordVisibility: () -> Boolean,
    passwordVisibility : Boolean
) {
    val localFocusManager = LocalFocusManager.current
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {

        Box(
            modifier = Modifier
                .weight(1f)
                .background(Color.Transparent),
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                value = text.toString(),
                onValueChange = onTextChange,
                shape = RoundedCornerShape(30.dp),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    backgroundColor =  Color.Transparent,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    placeholderColor = Color.Gray
                ),
                keyboardOptions = keyboardOptions,
                keyboardActions = KeyboardActions(
                    onDone = {
                        localFocusManager.clearFocus()
                    },
                    onNext = {localFocusManager.moveFocus(FocusDirection.Down)}
                ),
                visualTransformation = if (getPasswordVisibility()) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 20.sp,
                    textDecoration = TextDecoration.Underline ),
                placeholder = { Text(text = hint.toString(), color = Color.Black, fontSize = 20.sp)},

            )
        }
    }
}