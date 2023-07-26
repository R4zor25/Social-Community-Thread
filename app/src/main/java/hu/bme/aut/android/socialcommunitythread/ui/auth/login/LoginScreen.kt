package hu.bme.aut.android.socialcommunitythread.ui.auth.login

import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.analytics.FirebaseAnalytics
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.navigation.ThreadScreenNav
import hu.bme.aut.android.socialcommunitythread.ui.theme.Beige
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.CircularIndeterminateProgressBar
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.CustomTextField
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.gradient
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

//TODO Runtime notification for android 13 and above

@OptIn(ExperimentalComposeUiApi::class)
@InternalCoroutinesApi
@Composable
fun LoginScreen(navController: NavController) {
        Box {
            BgCard()
            MainCardLogin(navController)
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

@ExperimentalComposeUiApi
@InternalCoroutinesApi
@Composable
fun MainCardLogin(navController: NavController) {
    val viewModel = hiltViewModel<LoginScreenViewModel>()
    val context = LocalContext.current
    var firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    LaunchedEffect("key") {
        viewModel.oneShotEvent
            .onEach {
                when (it) {
                    LoginOneShotEvent.NavigateToMainThread -> navController.navigate(ThreadScreenNav.MainThreadScreenNav.route) {
                        navController.popBackStack()
                    }
                    is LoginOneShotEvent.ShowToastMessage -> Toast.makeText(context, it.errorText, Toast.LENGTH_LONG).show()
                }
            }
            .collect()
    }


    Surface(
        color = Color.White, modifier = Modifier.requiredHeight(700.dp),
        shape = RoundedCornerShape(60.dp).copy(ZeroCornerSize, ZeroCornerSize)
    ) {
        Column(
            modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.login_logo), contentDescription = "",
                modifier = Modifier
                    .height(300.dp)
                    .width(300.dp)
            )
            Spacer(modifier = Modifier.padding(16.dp))
            if (!viewModel.viewState.isLoading) {
                ClickableText(
                    text = AnnotatedString(stringResource(R.string.sign_in)), onClick = {
                        viewModel.viewState = viewModel.viewState.copy(
                            email = "demetermate@gmail.com",
                            password = "testtest"
                        )

                        val bundle = Bundle()
                        bundle.putString("email", viewModel.viewState.email)
                        bundle.putString("password", viewModel.viewState.password)
                        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle)
                        viewModel.onAction(LoginUiAction.OnLogin(viewModel.viewState.email, viewModel.viewState.password))
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 32.sp,
                        fontWeight = FontWeight(700),
                        //textDecoration = TextDecoration.Underline
                    )
                )

                /*Text(
                    "Sign In", modifier = Modifier.align(Alignment.CenterHorizontally), style = TextStyle(
                        color = Color.Black,
                        fontSize = 32.sp,
                        fontWeight = FontWeight(700)
                    ))

                 */
            } else
                CircularIndeterminateProgressBar(isDisplayed = viewModel.viewState.isLoading)
            Spacer(modifier = Modifier.padding(16.dp))

            CustomTextField(
                modifier = Modifier
                    .background(
                        brush = gradient,
                        shape = RoundedCornerShape(40.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 0.dp),
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "", tint = Color.Black) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),
                onTextChange = { viewModel.viewState = viewModel.viewState.copy(email = it) },
                text = viewModel.viewState.email,
                hint = stringResource(R.string.email),
                passwordVisibility = true,
                getPasswordVisibility = { true }
            )

            Spacer(modifier = Modifier.padding(12.dp))

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
                        Icon(image, contentDescription =  "", tint = Color.Black)
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Text),
                onTextChange = { viewModel.viewState = viewModel.viewState.copy(password = it) },
                text = viewModel.viewState.password,
                hint = stringResource(R.string.password),
                passwordVisibility = false,
                getPasswordVisibility = { viewModel.viewState.passwordVisibility }
            )
            ClickableText(
                text = AnnotatedString(stringResource(R.string.registration)), onClick = { navController.navigate(ThreadScreenNav.RegistrationScreenNav.route) },

                modifier = Modifier
                    .align(Alignment.End)
                    .padding(8.dp, 4.dp),
                style = TextStyle(
                    color = Color.Blue,
                    fontSize = 16.sp,
                    textDecoration = TextDecoration.Underline
                )
            )
            Spacer(modifier = Modifier.padding(vertical = 12.dp))
            Button(
                onClick = {
                    viewModel.onAction(LoginUiAction.OnLogin(viewModel.viewState.email, viewModel.viewState.password))
                },
                shape = CircleShape,
                modifier = Modifier
                    .width(130.dp)
                    .height(50.dp),
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = Beige
                ),
                contentPadding = PaddingValues(4.dp)
            ) {
                Text(text = stringResource(R.string.login), fontSize = 24.sp, color = Color.Black)
            }
            Spacer(modifier = Modifier.padding(vertical = 12.dp))
        }
    }
}