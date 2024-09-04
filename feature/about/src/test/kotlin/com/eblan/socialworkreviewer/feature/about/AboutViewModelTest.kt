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
package com.eblan.socialworkreviewer.feature.about

import com.eblan.socialworkreviewer.core.model.About
import com.eblan.socialworkreviewer.core.testing.linkparser.FakeLinkParser
import com.eblan.socialworkreviewer.core.testing.repository.FakeAboutRepository
import com.eblan.socialworkreviewer.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class AboutViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var aboutRepository: FakeAboutRepository

    private lateinit var linkParser: FakeLinkParser

    private lateinit var viewModel: AboutViewModel

    @Before
    fun setup() {
        aboutRepository = FakeAboutRepository()

        linkParser = FakeLinkParser()

        viewModel = AboutViewModel(aboutRepository = aboutRepository, linkParser = linkParser)
    }

    @Test
    fun aboutUiState_isLoading_whenStarted() {
        assertIs<AboutUiState.Loading>(viewModel.aboutUiState.value)
    }

    @Test
    fun aboutUiState_isSuccess() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.aboutUiState.collect()
        }

        val abouts = List(10) { index ->
            About(
                id = "$index",
                imageUrl = "imageUrl",
                title = "title",
                name = "name",
                message = "message",
                links = emptyList(),
            )
        }

        aboutRepository.setAbouts(abouts)

        assertIs<AboutUiState.Success>(viewModel.aboutUiState.value)
    }

    @Test
    fun openLink_isTrue() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.openLinkResult.collect()
        }

        linkParser.setOpenLInk(true)

        viewModel.openLink("")

        assertTrue(viewModel.openLinkResult.value!!)
    }

    @Test
    fun openLink_isFalse() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.openLinkResult.collect()
        }

        linkParser.setOpenLInk(false)

        viewModel.openLink("")

        assertFalse(viewModel.openLinkResult.value!!)
    }
}
