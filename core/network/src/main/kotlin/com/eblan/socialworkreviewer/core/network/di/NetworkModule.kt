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
package com.eblan.socialworkreviewer.core.network.di

import com.eblan.socialworkreviewer.core.network.firestore.AboutDataSource
import com.eblan.socialworkreviewer.core.network.firestore.CategoryDataSource
import com.eblan.socialworkreviewer.core.network.firestore.DefaultAboutDataSource
import com.eblan.socialworkreviewer.core.network.firestore.DefaultCategoryDataSource
import com.eblan.socialworkreviewer.core.network.firestore.DefaultNewsDataSource
import com.eblan.socialworkreviewer.core.network.firestore.DefaultQuestionDataSource
import com.eblan.socialworkreviewer.core.network.firestore.NewsDataSource
import com.eblan.socialworkreviewer.core.network.firestore.QuestionDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface NetworkModule {

    @Binds
    @Singleton
    fun categoryDataSource(impl: DefaultCategoryDataSource): CategoryDataSource

    @Binds
    @Singleton
    fun questionDataSource(impl: DefaultQuestionDataSource): QuestionDataSource

    @Binds
    @Singleton
    fun newsDataSource(impl: DefaultNewsDataSource): NewsDataSource

    @Binds
    @Singleton
    fun aboutDataSource(impl: DefaultAboutDataSource): AboutDataSource
}
