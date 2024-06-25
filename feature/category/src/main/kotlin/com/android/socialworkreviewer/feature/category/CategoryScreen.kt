package com.android.socialworkreviewer.feature.category

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.android.socialworkreviewer.core.designsystem.component.DynamicAsyncImage
import com.android.socialworkreviewer.core.designsystem.component.SocialWorkReviewerLoadingWheel
import com.android.socialworkreviewer.core.model.Category

@Composable
internal fun CategoryRoute(
    modifier: Modifier = Modifier,
    viewModel: CategoryViewModel = hiltViewModel(),
    onCategoryClick: (String) -> Unit,
) {
    val categoryUiState = viewModel.categoryUiState.collectAsStateWithLifecycle().value

    CategoryScreen(
        modifier = modifier, categoryUiState = categoryUiState, onCategoryClick = onCategoryClick
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
    Scaffold(
        topBar = {
            CategoryTopAppBar(
                title = "Categories",
            )
        },
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues)
                .semantics {
                    testTagsAsResourceId = true
                }
                .testTag("apps"),
        ) {
            when (categoryUiState) {
                CategoryUiState.Loading -> LoadingState(
                    modifier = Modifier.align(Alignment.Center),
                )

                is CategoryUiState.Success -> SuccessState(
                    modifier = modifier,
                    categoryUiState = categoryUiState,
                    contentPadding = paddingValues,
                    onCategoryClick = onCategoryClick,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        modifier = modifier.testTag("category:topAppBar"),
    )
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    SocialWorkReviewerLoadingWheel(
        modifier = modifier,
        contentDescription = "SocialWorkReviewerLoadingWheel",
    )
}

@Composable
private fun SuccessState(
    modifier: Modifier = Modifier,
    categoryUiState: CategoryUiState.Success,
    contentPadding: PaddingValues,
    onCategoryClick: (String) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(300.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag("category:lazyVerticalGrid"),
        contentPadding = contentPadding,
    ) {
        items(categoryUiState.categories) { category ->
            CategoryItem(
                category = category, onCategoryClick = onCategoryClick
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
    Box(modifier = modifier
        .clickable {
            onCategoryClick(category.id)
        }
        .size(300.dp), contentAlignment = Alignment.Center) {
        DynamicAsyncImage(
            model = category.imageUrl,
            contentDescription = "categoryImage",
            modifier = Modifier.fillMaxSize()
        )

        Text(text = category.title, style = MaterialTheme.typography.titleLarge)

    }
}