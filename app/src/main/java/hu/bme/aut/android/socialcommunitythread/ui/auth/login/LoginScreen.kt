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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.analytics.FirebaseAnalytics
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.navigation.ThreadScreenNav
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultTextColor
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.CircularIndeterminateProgressBar
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.CustomTextField
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

//TODO Runtime notification for android 13 and above

@OptIn(ExperimentalComposeUiApi::class)
@InternalCoroutinesApi
@Composable
fun LoginScreen(navController: NavController, viewModel: LoginScreenViewModel) {
        Box {
            BgCard()
            MainCardLogin(navController, viewModel)
        }
}

@Composable
fun BgCard() {
    Surface(color = MaterialTheme.colors.secondary, modifier = Modifier.fillMaxSize()) {
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
fun MainCardLogin(navController: NavController, viewModel: LoginScreenViewModel) {
    val viewState = viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var passwordVisibility by remember { mutableStateOf(false)}
    val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

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


    Box(
         modifier = Modifier
             .requiredHeight(700.dp)
             .clip(RoundedCornerShape(60.dp).copy(ZeroCornerSize, ZeroCornerSize))
             .background(MaterialTheme.colors.primary)

    //.copy(topEnd =  ZeroCornerSize, topStart = ZeroCornerSize)),
       // shape = RoundedCornerShape(60.dp).copy(ZeroCornerSize, ZeroCornerSize)
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
            if (!viewState.value.isLoading) {
                ClickableText(
                    text = AnnotatedString(stringResource(R.string.sign_in)), onClick = {
                        viewModel.changeMockLoginData()
                        val bundle = Bundle()
                        bundle.putString("username", viewState.value.username)
                        bundle.putString("password", viewState.value.password)
                        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle)
                        viewModel.login(viewState.value.username, viewState.value.password)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    style = TextStyle(
                        color = defaultTextColor(),
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
                CircularIndeterminateProgressBar(isDisplayed = viewState.value.isLoading)
            Spacer(modifier = Modifier.padding(16.dp))

            CustomTextField(
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colors.secondary, MaterialTheme.colors.secondary
                            )
                        ),
                        shape = RoundedCornerShape(40.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 0.dp),
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Text),
                onTextChange = { viewModel.onUsernameTextChange(it) },
                text = viewState.value.username,
                hint = "Username",
                getPasswordVisibility = { true }
            )

            Spacer(modifier = Modifier.padding(12.dp))

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
                    val image = if (passwordVisibility)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(image, contentDescription =  "")
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Text),
                onTextChange = { viewModel.onPasswordTextChange(it) },
                text = viewState.value.password,
                hint = stringResource(R.string.password),
                getPasswordVisibility = { passwordVisibility }
            )
            ClickableText(
                text = AnnotatedString(stringResource(R.string.registration)), onClick = { navController.navigate(ThreadScreenNav.RegistrationScreenNav.route) },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(8.dp, 4.dp),
                style = TextStyle(
                    color = MaterialTheme.colors.secondary,
                    fontSize = 16.sp,
                    textDecoration = TextDecoration.Underline
                )
            )
            Spacer(modifier = Modifier.padding(vertical = 12.dp))
            Button(
                onClick = {
                    viewModel.login(viewState.value.username, viewState.value.password)
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
                Text(text = stringResource(R.string.login), fontSize = 24.sp, color = defaultTextColor())
            }
            Spacer(modifier = Modifier.padding(vertical = 12.dp))
        }
    }
}

@OptIn(InternalCoroutinesApi::class)
@Composable
@Preview
private fun LoginScreenPreview(){
    val loginScreenViewModel = hiltViewModel<LoginScreenViewModel>()
    LoginScreen(navController = NavController(LocalContext.current), loginScreenViewModel)
}