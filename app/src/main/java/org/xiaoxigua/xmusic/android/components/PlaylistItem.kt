package org.xiaoxigua.xmusic.android.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
fun PlaylistItem(
    title: String,
    description: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onSelected: () -> Unit
) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val defaultActionSize = configuration.screenWidthDp.dp
    val startActionSizePx = with(density) { -defaultActionSize.toPx() }
    val anchors = DraggableAnchors {
        DragAnchors.Normal at 0f
        DragAnchors.RightMenu at startActionSizePx / 2
        DragAnchors.Deleted at startActionSizePx
    }
    val state = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Normal,
            anchors = anchors,
            positionalThreshold = { totalDistance ->
                totalDistance * 0.8f
            },
            velocityThreshold = {
                80.dp.value
            },
            snapAnimationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
            decayAnimationSpec = exponentialDecay(),
            confirmValueChange = { true }
        )
    }
    val isDeleted = remember { mutableStateOf(false) }

    DraggableItem(
        state,
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelected() }
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MinContainerColor), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.LibraryMusic,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(modifier = Modifier
                    .weight(1f)
                    .padding(start = 24.dp)) {
                    Text(
                        text = title,
                        color = HighLightGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = description,
                        color = MediumLightGray,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        endActions = arrayOf({ height ->
            val width by animateFloatAsState(
                when (state.currentValue) {
                    DragAnchors.Deleted -> 0f
                    else -> -state.requireOffset() / 2
                }, label = "WidthAnimate"
            )

            Box(
                modifier = Modifier
                    .size(with(density) { width.toDp() }, height)
                    .background(Color.Gray)
                    .clickable { onEdit() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    tint = Color.White,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }, { height ->
            val width by animateFloatAsState(
                when (state.currentValue) {
                    DragAnchors.Deleted -> -state.requireOffset()
                    else -> -state.requireOffset() / 2
                }, label = "WidthAnimate"
            )

            Box(
                modifier = Modifier
                    .size(with(density) { width.toDp() }, height)
                    .background(Color.Red)
                    .clickable { isDeleted.value = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    tint = Color.White,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }),
        onDelete = onDelete,
        isDeleted = isDeleted
    )
}

@Preview
@Composable
fun PlaylistItemPreview() {
    XMusicTheme {
        PlaylistItem("Playlist title", "Description", {}, {}, {})
    }
}