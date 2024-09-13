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
package com.eblan.socialworkreviewer.feature.news

import com.eblan.socialworkreviewer.core.model.News
import com.eblan.socialworkreviewer.core.testing.repository.FakeNewsRepository
import com.eblan.socialworkreviewer.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertIs

class NewsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var newsRepository: FakeNewsRepository

    private lateinit var viewModel: NewsViewModel

    @Before
    fun setup() {
        newsRepository = FakeNewsRepository()

        viewModel = NewsViewModel(
            newsRepository = newsRepository,
        )
    }

    @Test
    fun newsUiState_isLoading_whenStarted() {
        assertIs<NewsUiState.Loading>(viewModel.newsUiState.value)
    }

    @Test
    fun newsUiState_isSuccess() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.newsUiState.collect()
        }

        val news = List(10) { index ->
            News(id = "$index", title = "", message = "")
        }

        newsRepository.setNews(news)

        assertIs<NewsUiState.Success>(viewModel.newsUiState.value)
    }
}
