package hu.bme.aut.android.socialcommunitythread.ui.uicomponent.autocomplete

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ValueAutoCompleteString(text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.subtitle2, color = Color.Black)
        Row(verticalAlignment = Alignment.Bottom) {
            Divider(color = Color.Black, thickness = 1.dp)
        }
    }
}