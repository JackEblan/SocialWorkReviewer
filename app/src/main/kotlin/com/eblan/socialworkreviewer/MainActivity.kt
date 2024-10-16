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
package com.eblan.socialworkreviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.eblan.socialworkreviewer.core.designsystem.theme.SwrTheme
import com.eblan.socialworkreviewer.core.model.DarkThemeConfig
import com.eblan.socialworkreviewer.core.model.ThemeBrand
import com.eblan.socialworkreviewer.navigation.SwrNavHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        var mainActivityUiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mainActivityUiState.onEach { mainActivityUiState = it }.collect()
            }
        }

        splashScreen.setKeepOnScreenCondition {
            when (mainActivityUiState) {
                MainActivityUiState.Loading -> true
                is MainActivityUiState.Success -> false
            }
        }

        enableEdgeToEdge()

        setContent {
            val greenTheme = shouldUseGreenTheme(mainActivityUiState)

            val purpleTheme = shouldUsePurpleTheme(mainActivityUiState)

            val darkTheme = shouldUseDarkTheme(mainActivityUiState)

            val dynamicTheme = shouldUseDynamicTheme(mainActivityUiState)

            DisposableEffect(darkTheme) {
                enableEdgeToEdge()
                onDispose {}
            }

            SwrTheme(
                greenTheme = greenTheme,
                purpleTheme = purpleTheme,
                darkTheme = darkTheme,
                dynamicTheme = dynamicTheme,
            ) {
                SwrNavHost()
            }
        }
    }
}

@Composable
private fun shouldUseGreenTheme(
    mainActivityUiState: MainActivityUiState,
): Boolean = when (mainActivityUiState) {
    MainActivityUiState.Loading -> false
    is MainActivityUiState.Success -> when (mainActivityUiState.userData.themeBrand) {
        ThemeBrand.GREEN -> true
        ThemeBrand.PURPLE -> false
    }
}

@Composable
private fun shouldUsePurpleTheme(
    mainActivityUiState: MainActivityUiState,
): Boolean = when (mainActivityUiState) {
    MainActivityUiState.Loading -> false
    is MainActivityUiState.Success -> when (mainActivityUiState.userData.themeBrand) {
        ThemeBrand.GREEN -> false
        ThemeBrand.PURPLE -> true
    }
}

@Composable
private fun shouldUseDarkTheme(
    mainActivityUiState: MainActivityUiState,
): Boolean = when (mainActivityUiState) {
    MainActivityUiState.Loading -> isSystemInDarkTheme()
    is MainActivityUiState.Success -> when (mainActivityUiState.userData.darkThemeConfig) {
        DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        DarkThemeConfig.LIGHT -> false
        DarkThemeConfig.DARK -> true
    }
}

@Composable
private fun shouldUseDynamicTheme(
    mainActivityUiState: MainActivityUiState,
): Boolean = when (mainActivityUiState) {
    MainActivityUiState.Loading -> false
    is MainActivityUiState.Success -> mainActivityUiState.userData.useDynamicColor
}
