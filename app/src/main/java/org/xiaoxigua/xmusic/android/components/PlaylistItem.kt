package org.xiaoxigua.xmusic.android.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.xiaoxigua.xmusic.android.ui.theme.HighLightGray
import org.xiaoxigua.xmusic.android.ui.theme.MediumLightGray
import org.xiaoxigua.xmusic.android.ui.theme.MinContainerColor
import org.xiaoxigua.xmusic.android.ui.theme.XMusicTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistItem(title: String, description: String, onEdit: () -> Unit, onDelete: () -> Unit) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val defaultActionSize = configuration.screenWidthDp.dp
    val startActionSizePx = with(density) { -defaultActionSize.toPx() }
    val anchors = DraggableAnchors {
        DragAnchors.Normal at 0f
        DragAnchors.End at startActionSizePx
    }
    val state = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Normal,
            anchors = anchors,
            positionalThreshold = { totalDistance ->
                totalDistance * 0.8f
            },
            velocityThreshold = {
                56.dp.value
            },
            snapAnimationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
            decayAnimationSpec = exponentialDecay(),
            confirmValueChange = { true }
        )
    }

    LaunchedEffect(state) {
        snapshotFlow { state.settledValue }.collect { newValue ->
            if (newValue == DragAnchors.End)
                onDelete()
        }
    }

    DraggableItem(
        state,
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MinContainerColor), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.LibraryMusic,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Column(modifier = Modifier.padding(start = 30.dp)) {
                    Text(
                        text = title,
                        color = HighLightGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    Text(
                        text = description,
                        color = MediumLightGray,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onEdit) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        },
        endAction = {
            Text(
                "Delete",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                overflow = TextOverflow.Clip,
                maxLines = 1
            )
        }
    )
}

@Preview
@Composable
fun PlaylistItemPreview() {
    XMusicTheme {
        PlaylistItem("Playlist title", "Description", {}) {

        }
    }
}