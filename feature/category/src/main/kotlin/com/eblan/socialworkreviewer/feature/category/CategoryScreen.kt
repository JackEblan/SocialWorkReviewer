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
package com.eblan.socialworkreviewer.feature.category

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eblan.socialworkreviewer.core.designsystem.component.ShimmerImage
import com.eblan.socialworkreviewer.core.designsystem.component.SwrLoadingWheel
import com.eblan.socialworkreviewer.core.designsystem.icon.Swr
import com.eblan.socialworkreviewer.core.model.Category

@Composable
internal fun CategoryRoute(
    modifier: Modifier = Modifier,
    viewModel: CategoryViewModel = hiltViewModel(),
    onCategoryClick: (String) -> Unit,
) {
    val categoryUiState = viewModel.categoryUiState.collectAsStateWithLifecycle().value

    CategoryScreen(
        modifier = modifier,
        categoryUiState = categoryUiState,
        onCategoryClick = onCategoryClick,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@VisibleForTesting
@Composable
internal fun CategoryScreen(
    modifier: Modifier = Modifier,
    categoryUiState: CategoryUiState,
    onCategoryClick: (String) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .semantics {
                testTagsAsResourceId = true
            }
            .testTag("category"),
    ) {
        when (categoryUiState) {
            CategoryUiState.Loading -> LoadingState(
                modifier = Modifier.align(Alignment.Center),
            )

            is CategoryUiState.Success -> {
                if (categoryUiState.categories.isNotEmpty()) {
                    SuccessState(
                        modifier = modifier,
                        categoryUiState = categoryUiState,
                        onCategoryClick = onCategoryClick,
                    )
                } else {
                    EmptyState(text = "No Categories found!")
                }
            }
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    SwrLoadingWheel(
        modifier = modifier,
        contentDescription = "SwrLoadingWheel",
    )
}

@Composable
private fun SuccessState(
    modifier: Modifier = Modifier,
    categoryUiState: CategoryUiState.Success,
    onCategoryClick: (String) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(300.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag("category:lazyVerticalStaggeredGrid"),
    ) {
        items(
            categoryUiState.categories,
            key = { category ->
                category.id
            },
        ) { category ->
            CategoryItem(
                modifier = Modifier.animateItem(),
                category = category,
                onCategoryClick = onCategoryClick,
            )
        }
    }
}

@Composable
private fun CategoryItem(
    modifier: Modifier = Modifier,
    category: Category,
    onCategoryClick: (String) -> Unit,
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .testTag("category:categoryItem"),
        onClick = {
            onCategoryClick(category.id)
        },
    ) {
        ShimmerImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            headerImageUrl = category.imageUrl,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(text = category.title, style = MaterialTheme.typography.headlineLarge)

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = category.description, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun EmptyState(
    modifier: Modifier = Modifier,
    text: String,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("category:emptyListPlaceHolderScreen"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Swr.Question,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}
