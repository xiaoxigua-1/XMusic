package org.xiaoxigua.xmusic.android.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xiaoxigua.xmusic.android.ui.theme.HighLightGray
import org.xiaoxigua.xmusic.android.ui.theme.MediumLightGray
import org.xiaoxigua.xmusic.android.ui.theme.XMusicTheme

@Composable
fun PlaylistItem(title: String, description: String, onEdit: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.LibraryMusic,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(48.dp)
        )

        Column(modifier = Modifier.padding(start = 30.dp)) {
            Text(
                text = title,
                color = HighLightGray,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Text(text = description, color = MediumLightGray, fontWeight = FontWeight.Medium)
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onEdit) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Preview
@Composable
fun PlaylistItemPreview() {
    XMusicTheme {
        PlaylistItem("Playlist title", "Description") {

        }
    }
}