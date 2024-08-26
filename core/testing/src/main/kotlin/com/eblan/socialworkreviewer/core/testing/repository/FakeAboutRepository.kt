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
package com.eblan.socialworkreviewer.core.testing.repository

import com.eblan.socialworkreviewer.core.data.repository.AboutRepository
import com.eblan.socialworkreviewer.core.model.About
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeAboutRepository : AboutRepository {
    private val _aboutsFlow =
        MutableSharedFlow<List<About>>(replay = 1, onBufferOverflow = DROP_OLDEST)

    override fun getAbouts(): Flow<List<About>> {
        return _aboutsFlow
    }

    fun setAbouts(value: List<About>) {
        _aboutsFlow.tryEmit(value)
    }
}
