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
package com.android.socialworkreviewer.core.data.di

import com.android.socialworkreviewer.core.data.repository.AverageRepository
import com.android.socialworkreviewer.core.data.repository.CategoryRepository
import com.android.socialworkreviewer.core.data.repository.ChoiceRepository
import com.android.socialworkreviewer.core.data.repository.DefaultAverageRepository
import com.android.socialworkreviewer.core.data.repository.DefaultCategoryRepository
import com.android.socialworkreviewer.core.data.repository.DefaultChoiceRepository
import com.android.socialworkreviewer.core.data.repository.DefaultQuestionRepository
import com.android.socialworkreviewer.core.data.repository.DefaultUserDataRepository
import com.android.socialworkreviewer.core.data.repository.QuestionRepository
import com.android.socialworkreviewer.core.data.repository.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    internal abstract fun categoryRepository(impl: DefaultCategoryRepository): CategoryRepository

    @Binds
    @Singleton
    internal abstract fun questionRepository(impl: DefaultQuestionRepository): QuestionRepository

    @Binds
    internal abstract fun answerRepository(impl: DefaultChoiceRepository): ChoiceRepository

    @Binds
    @Singleton
    internal abstract fun userDataRepository(impl: DefaultUserDataRepository): UserDataRepository

    @Binds
    @Singleton
    internal abstract fun averageRepository(impl: DefaultAverageRepository): AverageRepository
}