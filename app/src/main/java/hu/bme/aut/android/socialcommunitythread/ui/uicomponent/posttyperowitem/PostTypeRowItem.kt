package hu.bme.aut.android.socialcommunitythread.ui.uicomponent.posttyperowitem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.android.socialcommunitythread.ui.theme.Beige

@Composable
fun PostTypeRowItem(imageVector: ImageVector, title: String, onClick: () -> Unit, selected: Boolean = false) {
    val tintBackground = if (selected)
        Color.Black
    else
        Color.LightGray

    val fontWeight = if(selected)
        FontWeight.Bold
    else
        FontWeight.Normal

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().background(Beige).clickable { onClick.invoke() } ) {
        Icon(imageVector = imageVector, contentDescription = "", tint = tintBackground, modifier = Modifier.padding(end = 8.dp, start = 6.dp).size(48.dp))
        Text(text = title, fontWeight = fontWeight, color = tintBackground, fontSize = 16.sp)
        if(selected){
            Column(horizontalAlignment = Alignment.End) {
                Icon(imageVector = Icons.Filled.Check, contentDescription = "", tint = tintBackground, modifier = Modifier.size(48.dp))
            }

    }
    }
}

@Composable
@Preview
fun PostTypeRowItem(){
    PostTypeRowItem(imageVector = Icons.Filled.Image, title = "Gallery", onClick = {}, selected = true)
}