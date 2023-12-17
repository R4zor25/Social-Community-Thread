package hu.bme.aut.android.socialcommunitythread.ui.uicomponent.topbar

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.domain.interactors.AuthInteractor
import hu.bme.aut.android.socialcommunitythread.ui.theme.PrimaryLight
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultIconColor
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultTextColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopBar(title: String = "", leftIconImage: ImageVector?, rightIconImage: @Composable (() -> Unit)?, scope: CoroutineScope, scaffoldState: ScaffoldState, onLeftIconClick: (() -> Unit)? = null, onRightIconClick: (() -> Unit)? = null) {
    TopAppBar(
        title = {
            Text(
                text = title, fontSize = 18.sp, color = defaultTextColor()
            )
        },
        navigationIcon = {
            if (onLeftIconClick != null && leftIconImage != null) {
                IconButton(modifier = Modifier.semantics { contentDescription = "TestLeftIcon" },
                    onClick = {
                        //scope.launch { scaffoldState.drawerState.open() }
                        onLeftIconClick()
                    }
                ) {
                    Icon(leftIconImage, contentDescription = "", tint = defaultIconColor())
                }
            }
        },
        actions = {
            if (onRightIconClick != null && rightIconImage != null) {
                IconButton(modifier = Modifier.semantics { contentDescription = "TestRightIcon" },
                    onClick = {
                        scope.launch { scaffoldState.drawerState.open() }
                        onRightIconClick()
                    }
                ) {
                    //rightIconImage?.let { Icon(it, contentDescription = "") }
                    rightIconImage()
                }
            }
        },
        backgroundColor = MaterialTheme.colors.secondary,
        contentColor = Color.Black,
    )
}

@Preview
@Composable
fun TopBarPreview() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val navController = rememberNavController()
    TopBar("Navigation bar", Icons.Filled.ArrowBack , {
        Image(
         bitmap = BitmapFactory.decodeByteArray(AuthInteractor.currentLoggedInUser!!.profileImage, 0, AuthInteractor.currentLoggedInUser!!.profileImage!!.size).asImageBitmap(),
        contentDescription = "avatar",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
    )}
        , scope, scaffoldState, {}, {})
}