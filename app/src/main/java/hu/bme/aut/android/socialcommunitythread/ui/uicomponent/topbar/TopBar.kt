package hu.bme.aut.android.socialcommunitythread.ui.uicomponent.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import hu.bme.aut.android.socialcommunitythread.ui.theme.Beige
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopBar(title: String = "", leftIconImage: ImageVector?, rightIconImage: @Composable (() -> Unit)?, scope: CoroutineScope, scaffoldState: ScaffoldState, onLeftIconClick: (() -> Unit)? = null, onRightIconClick: (() -> Unit)? = null) {
    TopAppBar(
        title = {
            Text(
                text = title, fontSize = 18.sp
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
                    Icon(leftIconImage, contentDescription = "")
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
        backgroundColor = Beige,
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
        painter = painterResource(R.drawable.capybara),
        contentDescription = "avatar",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
    )}
        , scope, scaffoldState, {}, {})
}