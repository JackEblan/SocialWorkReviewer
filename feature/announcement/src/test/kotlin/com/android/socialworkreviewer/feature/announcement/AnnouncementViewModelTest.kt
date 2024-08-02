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
package com.android.socialworkreviewer.feature.announcement

import com.android.socialworkreviewer.core.model.Announcement
import com.android.socialworkreviewer.core.testing.repository.FakeAnnouncementRepository
import com.android.socialworkreviewer.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertIs

class AnnouncementViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var announcementRepository: FakeAnnouncementRepository

    private lateinit var viewModel: AnnouncementViewModel

    @Before
    fun setup() {
        announcementRepository = FakeAnnouncementRepository()

        viewModel = AnnouncementViewModel(
            announcementRepository = announcementRepository,
        )
    }

    @Test
    fun announcementUiState_isLoading_whenStarted() {
        assertIs<AnnouncementUiState.Loading>(viewModel.announcementUiState.value)
    }

    @Test
    fun announcementUiState_isSuccess() = runTest {
        val announcements = List(10) { index ->
            Announcement(id = "$index", title = "", message = "")
        }

        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.announcementUiState.collect() }

        announcementRepository.setAnnouncements(announcements)

        assertIs<AnnouncementUiState.Success>(viewModel.announcementUiState.value)

        collectJob.cancel()
    }
}
