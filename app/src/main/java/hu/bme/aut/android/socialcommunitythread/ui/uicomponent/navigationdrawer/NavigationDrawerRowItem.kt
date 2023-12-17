package hu.bme.aut.android.socialcommunitythread.ui.uicomponent.navigationdrawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.android.socialcommunitythread.navigation.NavigationDrawerItems
import hu.bme.aut.android.socialcommunitythread.ui.theme.PrimaryLight
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultIconColor
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultTextColor
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.BottomNavItem

@Composable
fun NavigationDrawerRowItem(item: BottomNavItem, onItemClick: (BottomNavItem) -> Unit) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onItemClick(item) })
            .height(60.dp)
            .background(MaterialTheme.colors.secondary)
            .padding(start = 8.dp)
    ) {
        Image(
            imageVector = item.icon,
            contentDescription = item.label,
            colorFilter = ColorFilter.tint(defaultIconColor()),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(30.dp)
                .width(30.dp)
        )
        Spacer(modifier = Modifier.width(15.dp))
        Text(
            text = item.label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = defaultTextColor()
        )
    }
}

@Preview(showBackground = false)
@Composable
fun NavigationDrawerRowItemPreview() {
    NavigationDrawerRowItem(item = NavigationDrawerItems.NavigationDrawerItems[0], onItemClick = {})
}