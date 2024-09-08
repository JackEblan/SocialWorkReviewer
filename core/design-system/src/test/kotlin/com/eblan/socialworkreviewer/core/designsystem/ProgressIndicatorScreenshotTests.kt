package com.eblan.socialworkreviewer.core.designsystem

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.eblan.socialworkreviewer.core.designsystem.component.SwrLinearProgressIndicator
import com.eblan.socialworkreviewer.core.screenshottesting.util.captureMultiTheme
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class, qualifiers = "480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class ProgressIndicatorScreenshotTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun progressIndicator_multipleThemes() {
        composeTestRule.captureMultiTheme("ProgressIndicator") {
            SwrLinearProgressIndicator(
                progress = { 0.5f },
            )
        }
    }
}