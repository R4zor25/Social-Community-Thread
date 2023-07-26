package hu.bme.aut.android.socialcommunitythread.ui.auth.registration

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.navigation.ThreadScreenNav
import hu.bme.aut.android.socialcommunitythread.ui.theme.Beige
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.CustomTextField
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.gradient
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun RegistrationScreen(navController: NavController) {
    Box {
        BgCard()
        MainCardRegistration(navController)
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    Box {
        BgCard()
        MainCardRegistration(navController = NavController(LocalContext.current))
    }

}

@Composable
fun BgCard() {
    Surface(color = Beige, modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = (-30).dp)
        ) {
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainCardRegistration(navController: NavController) {
    val viewModel = hiltViewModel<RegistrationViewModel>()
    val context = LocalContext.current

    LaunchedEffect("key") {
        viewModel.oneShotEvent
            .onEach {
                when (it) {
                    RegistrationOneShotEvent.NavigateToMainThread -> navController.navigate(ThreadScreenNav.MainThreadScreenNav.route) {
                        navController.popBackStack()
                    }
                    is RegistrationOneShotEvent.ShowToastMessage -> Toast.makeText(context, it.errorText, Toast.LENGTH_LONG).show()
                }
            }
            .collect()
    }
    Surface(
        color = Color.White, modifier = Modifier.requiredHeight(720.dp),
        shape = RoundedCornerShape(60.dp).copy(ZeroCornerSize, ZeroCornerSize)
    ) {
        Column(
            modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.login_logo), contentDescription = "",
                modifier = Modifier
                    .height(280.dp)
                    .width(280.dp)
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Text(text = stringResource(R.string.sign_up), fontWeight = FontWeight(700), fontSize = 32.sp, color = Color.Black)
            Spacer(modifier = Modifier.padding(16.dp))
            CustomTextField(
                modifier = Modifier
                    .background(
                        brush = gradient,
                        shape = RoundedCornerShape(40.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 0.dp),
                leadingIcon = { Icon(Icons.Filled.Person, tint = Color.Black, contentDescription = "") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Text),
                onTextChange = { viewModel.viewState = viewModel.viewState.copy(userName = it)  },
                text = viewModel.viewState.userName,
                hint = stringResource(R.string.user_name),
                passwordVisibility = true,
                getPasswordVisibility = { true }
            )

            Spacer(modifier = Modifier.padding(8.dp))

            CustomTextField(
                modifier = Modifier
                    .background(
                        brush = gradient,
                        shape = CircleShape
                    )
                    .padding(horizontal = 12.dp, vertical = 0.dp),
                leadingIcon = { Icon(Icons.Filled.Email, tint = Color.Black, contentDescription = "") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),
                onTextChange = { viewModel.viewState = viewModel.viewState.copy(email = it) },
                text = viewModel.viewState.email,
                hint = stringResource(R.string.email),
                passwordVisibility = true,
                getPasswordVisibility = { true }
            )
            Spacer(modifier = Modifier.padding(8.dp))
            CustomTextField(
                modifier = Modifier
                    .background(
                        brush = gradient,
                        shape = CircleShape
                    )
                    .padding(horizontal = 12.dp, vertical = 0.dp),
                leadingIcon = {
                    val image = if (viewModel.viewState.passwordVisibility)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                    IconButton(onClick = { viewModel.viewState = viewModel.viewState.copy(passwordVisibility = !viewModel.viewState.passwordVisibility) }) {
                        Icon(image, tint = Color.Black, contentDescription = "")
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Text),
                onTextChange = { viewModel.viewState = viewModel.viewState.copy(password = it) },
                text = viewModel.viewState.password,
                hint = stringResource(R.string.password),
                passwordVisibility = false,
                getPasswordVisibility = { viewModel.viewState.passwordVisibility }
            )
            Spacer(modifier = Modifier.padding(vertical = 12.dp))
            Button(
                onClick = { viewModel.onAction(RegistrationUiAction.OnRegistration(viewModel.viewState.email, viewModel.viewState.userName, viewModel.viewState.password)) }, shape = CircleShape,
                modifier = Modifier
                    .width(130.dp)
                    .height(50.dp),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = Beige
                ),
                contentPadding = PaddingValues(4.dp)
            ) {
                Text(text = stringResource(R.string.register), fontSize = 24.sp, color = Color.Black)
            }
            Spacer(modifier = Modifier.padding(vertical = 12.dp))
        }
    }
}
