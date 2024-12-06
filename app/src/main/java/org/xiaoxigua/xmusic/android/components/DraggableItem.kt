package org.xiaoxigua.xmusic.android.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class DragAnchors {
    Normal,
    End
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableItem(
    state: AnchoredDraggableState<DragAnchors>,
    content: @Composable BoxScope.() -> Unit,
    endAction: @Composable (BoxScope.() -> Unit)? = {},
    onDelete: (() -> Unit)?
) {
    val density = LocalDensity.current
    var boxSize by remember { mutableStateOf(IntSize.Zero) }
    var isDeleted by remember { mutableStateOf(false) }
    val deletedScale = remember { Animatable(1f) }
    val deletedOffset = remember { Animatable(0f) }

    // Deleted animate
    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            launch {
                deletedScale.animateTo(0f, animationSpec = tween(durationMillis = 300))
            }
            launch {
                deletedOffset.animateTo(
                    -boxSize.height.toFloat() / 2,
                    animationSpec = tween(durationMillis = 300)
                )
            }

            delay(300)

            if (onDelete != null) {
                onDelete()
            }
        }
    }

    // Right Swipe event
    LaunchedEffect(state) {
        snapshotFlow { state.settledValue }.collect { newValue ->
            if (newValue == DragAnchors.End)
                isDeleted = true
        }
    }

    Box(
        modifier = Modifier
            .clip(RectangleShape)
            .wrapContentSize()
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                layout(placeable.width, (placeable.height * deletedScale.value).toInt()) {
                    placeable.place(0, 0)
                }
            }
            .graphicsLayer(
                scaleY = deletedScale.value,
                translationY = deletedOffset.value
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .size(with(density) {
                    -state
                        .requireOffset()
                        .toDp()
                },
                    with(density) { boxSize.height.toDp() })
                .background(Color.Red)
                .align(Alignment.TopEnd),
            contentAlignment = Alignment.Center
        ) {
            endAction?.let {
                endAction()
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart)
                .offset {
                    IntOffset(
                        x = state
                            .requireOffset()
                            .roundToInt(),
                        y = 0,
                    )
                }
                .onSizeChanged { boxSize = it }
                .anchoredDraggable(state, Orientation.Horizontal),
            content = content
        )
    }
}