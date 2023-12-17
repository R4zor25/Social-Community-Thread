package hu.bme.aut.android.socialcommunitythread.ui.uicomponent.taglist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultTextColor

@Composable
fun TagList(tags : List<String>) {
    LazyRow() {
        items(tags.size) {
            Surface(
                modifier = Modifier
                    .padding(horizontal = 6.dp, vertical = 8.dp),
                elevation = 4.dp,
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = tags[it], color = defaultTextColor(), fontSize = 14.sp, modifier = Modifier.padding(6.dp), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
@Preview
fun TagListPreview(){
    TagList(tags = listOf("TAfghG1", "TAdfghG2", "TAG3", "TAG4", "TAG5"))
}