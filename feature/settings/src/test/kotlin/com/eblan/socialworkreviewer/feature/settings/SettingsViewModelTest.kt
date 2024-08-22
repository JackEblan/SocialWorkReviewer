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
package com.eblan.socialworkreviewer.feature.settings

import com.eblan.socialworkreviewer.core.model.DarkThemeConfig
import com.eblan.socialworkreviewer.core.model.ThemeBrand
import com.eblan.socialworkreviewer.core.model.UserData
import com.eblan.socialworkreviewer.core.testing.repository.FakeUserDataRepository
import com.eblan.socialworkreviewer.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertIs

class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var userDataRepository: FakeUserDataRepository

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        userDataRepository = FakeUserDataRepository()

        viewModel = SettingsViewModel(
            userDataRepository = userDataRepository,
        )
    }

    @Test
    fun settingsUiState_isLoading_whenStarted() {
        assertIs<SettingsUiState.Loading>(viewModel.settingsUiState.value)
    }

    @Test
    fun settingsUiState_isSuccess() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.settingsUiState.collect() }

        userDataRepository.setUserData(
            userData = UserData(
                themeBrand = ThemeBrand.DEFAULT,
                darkThemeConfig = DarkThemeConfig.DARK,
                useDynamicColor = false,
            ),
        )

        userDataRepository.setThemeBrand(ThemeBrand.ANDROID)

        userDataRepository.setDarkThemeConfig(DarkThemeConfig.DARK)

        assertIs<SettingsUiState.Success>(viewModel.settingsUiState.value)

        collectJob.cancel()
    }
}
