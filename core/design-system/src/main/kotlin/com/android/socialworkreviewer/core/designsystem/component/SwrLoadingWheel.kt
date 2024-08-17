/*
 *
 *   Copyright 2023 Einstein Blanco
 *
 *   Licensed under the GNU General Public License v3.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.gnu.org/licenses/gpl-3.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package com.android.socialworkreviewer.core.designsystem.component

import androidx.annotation.IntRange
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.android.socialworkreviewer.core.designsystem.theme.SwrTheme
import kotlinx.coroutines.launch

@Composable
fun SwrLoadingWheel(
    contentDescription: String,
    modifier: Modifier = Modifier,
    @IntRange(from = 5, to = 12) numberOfLines: Int = 10,
    @IntRange(from = 5000, to = 20000) rotationTime: Int = 12000,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wheel transition")

    // Specifies the float animation for slowly drawing out the lines on entering
    val startValue = if (LocalInspectionMode.current) 0F else 1F
    val floatAnimValues = (0 until numberOfLines).map { remember { Animatable(startValue) } }
    LaunchedEffect(floatAnimValues) {
        (0 until numberOfLines).map { index ->
            launch {
                floatAnimValues[index].animateTo(
                    targetValue = 0F,
                    animationSpec = tween(
                        durationMillis = 100,
                        easing = FastOutSlowInEasing,
                        delayMillis = 40 * index,
                    ),
                )
            }
        }
    }

    // Specifies the rotation animation of the entire Canvas composable
    val rotationAnim by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = rotationTime, easing = LinearEasing),
        ),
        label = "wheel rotation animation",
    )

    // Specifies the color animation for the base-to-progress line color change
    val baseLineColor = MaterialTheme.colorScheme.onBackground
    val progressLineColor = MaterialTheme.colorScheme.inversePrimary

    val colorAnimValues = (0 until numberOfLines).map { index ->
        infiniteTransition.animateColor(
            initialValue = baseLineColor,
            targetValue = baseLineColor,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = rotationTime / 2
                    progressLineColor at rotationTime / numberOfLines / 2 using LinearEasing
                    baseLineColor at rotationTime / numberOfLines using LinearEasing
                },
                repeatMode = RepeatMode.Restart,
                initialStartOffset = StartOffset(rotationTime / numberOfLines / 2 * index),
            ),
            label = "wheel color animation",
        )
    }

    // Draws out the LoadingWheel Canvas composable and sets the animations
    Canvas(
        modifier = modifier
            .size(48.dp)
            .padding(8.dp)
            .graphicsLayer { rotationZ = rotationAnim }
            .semantics { this.contentDescription = contentDescription }
            .testTag("loadingWheel"),
    ) {
        repeat(numberOfLines) { index ->
            rotate(degrees = 360f / numberOfLines * index) {
                drawLine(
                    color = colorAnimValues[index].value,
                    // Animates the initially drawn 1 pixel alpha from 0 to 1
                    alpha = if (floatAnimValues[index].value < 1f) 1f else 0f,
                    strokeWidth = 6F,
                    cap = StrokeCap.Round,
                    start = Offset(size.width / 2, size.height / 4),
                    end = Offset(size.width / 2, floatAnimValues[index].value * size.height / 4),
                )
            }
        }
    }
}

@Composable
fun SwrIosLoadingWheel(
    contentDescription: String,
    modifier: Modifier = Modifier,
    @IntRange(from = 5, to = 12) numberOfLines: Int = 10,
    @IntRange(from = 5000, to = 20000) rotationTime: Int = 12000,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wheel transition")

    val progressLineColor = MaterialTheme.colorScheme.inversePrimary

    val colorAnimValues = (0 until numberOfLines).map { index ->
        infiniteTransition.animateColor(
            initialValue = progressLineColor.copy(alpha = 0.1f),
            targetValue = progressLineColor.copy(alpha = 0.1f),
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = rotationTime / 2
                    progressLineColor.copy(alpha = 1f) at rotationTime / numberOfLines / 2 using LinearEasing
                },
                repeatMode = RepeatMode.Restart,
                initialStartOffset = StartOffset(rotationTime / numberOfLines / 2 * index),
            ),
            label = "wheel color animation",
        )
    }

    Canvas(
        modifier = modifier
            .size(48.dp)
            .padding(8.dp)
            .semantics { this.contentDescription = contentDescription }
            .testTag("loadingWheel"),
    ) {
        repeat(numberOfLines) { index ->
            rotate(degrees = 360f / numberOfLines * index) {
                drawLine(
                    color = colorAnimValues[index].value,
                    strokeWidth = 6F,
                    cap = StrokeCap.Round,
                    start = Offset(size.width / 2, 0f),
                    end = Offset(size.width / 2, size.height / 4),
                )
            }
        }
    }
}

@Composable
fun SwrOverlayLoadingWheel(
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(60.dp),
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.83f),
        modifier = modifier.size(60.dp),
    ) {
        SwrLoadingWheel(
            contentDescription = contentDescription,
        )
    }
}

@ThemePreviews
@Composable
private fun SwrLoadingWheelPreview() {
    SwrTheme {
        Surface {
            SwrLoadingWheel(contentDescription = "SocialWorkReviewerOverlayLoadingWheel")
        }
    }
}

@ThemePreviews
@Composable
private fun SwrIosLoadingWheelPreview() {
    SwrTheme {
        Surface {
            SwrIosLoadingWheel(contentDescription = "SocialWorkReviewerOverlayLoadingWheel")
        }
    }
}

@ThemePreviews
@Composable
private fun SwrOverlayLoadingWheelPreview() {
    SwrTheme {
        Surface {
            SwrOverlayLoadingWheel(contentDescription = "SocialWorkReviewerOverlayLoadingWheel")
        }
    }
}
