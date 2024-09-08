package com.eblan.socialworkreviewer.core.designsystem

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import com.eblan.socialworkreviewer.core.designsystem.component.SwrLargeTopAppBar
import com.eblan.socialworkreviewer.core.designsystem.theme.LocalGradientColors
import com.eblan.socialworkreviewer.core.screenshottesting.util.DefaultRoborazziOptions
import com.eblan.socialworkreviewer.core.screenshottesting.util.captureMultiTheme
import com.github.takahirom.roborazzi.captureRoboImage
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.test.FontScale
import com.eblan.socialworkreviewer.core.designsystem.theme.SwrTheme

@OptIn(ExperimentalMaterial3Api::class)
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class, qualifiers = "480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class TopAppBarScreenshotTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun largeTopAppBar_multipleThemes() {
        composeTestRule.captureMultiTheme("LargeTopAppBar") {
            SwrLargeTopAppBarExample()
        }
    }

    @Test
    fun largeTopAppBar_hugeFont() {
        composeTestRule.setContent {
            CompositionLocalProvider(
                LocalInspectionMode provides true,
            ) {
                DeviceConfigurationOverride(
                    DeviceConfigurationOverride.FontScale(2f),
                ) {
                    SwrTheme {
                        SwrLargeTopAppBarExample()
                    }
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            "src/test/screenshots/TopAppBar/TopAppBar_fontScale2.png",
            roborazziOptions = DefaultRoborazziOptions,
        )
    }

    @Composable
    private fun SwrLargeTopAppBarExample() {
        SwrLargeTopAppBar(
            title = {
                Text(
                    text = "Large Top App Bar",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        brush = Brush.linearGradient(
                            colors = LocalGradientColors.current.topBarTitleColorsDefault,
                        ),
                    ),
                )
            },
        )
    }
}