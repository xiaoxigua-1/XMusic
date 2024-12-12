package org.xiaoxigua.xmusic.android.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xiaoxigua.xmusic.android.LocalMusicPlayer
import org.xiaoxigua.xmusic.android.ui.theme.BottomContainerColor
import org.xiaoxigua.xmusic.android.ui.theme.BottomCoverContainerColor
import org.xiaoxigua.xmusic.android.ui.theme.HighLightGray
import org.xiaoxigua.xmusic.android.ui.theme.MediumLightGray
import java.io.File
import java.net.URI

@Composable
fun PlayerScreen(paddingValues: PaddingValues) {
    val musicPlayer = LocalMusicPlayer.current

    val nowPlaying by musicPlayer.nowPlaylist.observeAsState()
    val nowPlaylistIndex = nowPlaying?.index?.observeAsState()
    val nowPlayingSong = nowPlaying?.songs?.get(nowPlaylistIndex?.value ?: 0)

    val isPlaying by musicPlayer.isPlaying.observeAsState()
    val progress by musicPlayer.progress.observeAsState()

    val painter = if (nowPlayingSong?.artworkURL != null) {
        val artworkImageFile = File(URI.create(nowPlayingSong.artworkURL))
        val bitmap = BitmapFactory.decodeFile(artworkImageFile.absolutePath)

        BitmapPainter(bitmap.asImageBitmap())
    } else null

    Column(modifier = Modifier.padding(paddingValues)) {
        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(BottomContainerColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress?.position ?: 0f)
                    .height(1.dp)
                    .background(Color.White)
                    .align(Alignment.BottomStart)
            )

            Row(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(BottomCoverContainerColor), contentAlignment = Alignment.Center
                ) {
                    if (painter != null)
                        Image(
                            painter = painter,
                            contentDescription = "Cover",
                            modifier = Modifier.fillMaxSize()
                        )
                    else
                        Icon(
                            imageVector = Icons.Filled.MusicNote,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                }

                Column(
                    modifier = Modifier
                        .padding(start = 18.dp)
                        .weight(1f),
                ) {
                    Text(
                        text = nowPlayingSong?.title ?: "Unknown Title",
                        color = HighLightGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 14.sp
                    )
                    Text(
                        text = nowPlayingSong?.artist ?: "Unknown Artist",
                        color = MediumLightGray,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 12.sp
                    )
                }

                Row {
                    IconButton({
                        if (isPlaying == true) musicPlayer.pause() else musicPlayer.play()
                    }) {
                        Icon(
                            imageVector = if (isPlaying == true) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = null
                        )
                    }
                    IconButton({
                        musicPlayer.next()
                    }) {
                        Icon(imageVector = Icons.Filled.SkipNext, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewPlayerScreen() {
    PlayerScreen(PaddingValues())
}