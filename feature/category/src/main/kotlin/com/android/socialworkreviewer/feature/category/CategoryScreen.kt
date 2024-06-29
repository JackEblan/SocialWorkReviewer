package com.android.socialworkreviewer.feature.category

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.socialworkreviewer.core.designsystem.component.DynamicAsyncImage
import com.android.socialworkreviewer.core.designsystem.component.SocialWorkReviewerLoadingWheel
import com.android.socialworkreviewer.core.designsystem.icon.SocialWorkReviewerIcons
import com.android.socialworkreviewer.core.model.Category

@Composable
internal fun CategoryRoute(
    modifier: Modifier = Modifier,
    viewModel: CategoryViewModel = hiltViewModel(),
    onCategoryClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
) {
    val categoryUiState = viewModel.categoryUiState.collectAsStateWithLifecycle().value

    val categoryErrorMessage = viewModel.categoryErrorMessage.collectAsStateWithLifecycle().value

    val snackbarHostState = remember { SnackbarHostState() }

    CategoryScreen(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        categoryUiState = categoryUiState,
        categoryErrorMessage = categoryErrorMessage,
        onCategoryClick = onCategoryClick,
        onSettingsClick = onSettingsClick,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@VisibleForTesting
@Composable
internal fun CategoryScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    categoryUiState: CategoryUiState,
    categoryErrorMessage: String?,
    onCategoryClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
) {
    LaunchedEffect(key1 = categoryErrorMessage) {
        if (categoryErrorMessage != null) {
            snackbarHostState.showSnackbar(
                message = categoryErrorMessage, duration = SnackbarDuration.Indefinite
            )
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            CategoryTopAppBar(
                title = "Categories",
                onSettingsClick = onSettingsClick,
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
                .testTag("category"),
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
    onSettingsClick: () -> Unit,
) {
    val gradientColors = listOf(Color(0xFF00BCD4), Color(0xFF03A9F4), Color(0xFF9C27B0))

    CenterAlignedTopAppBar(title = {
        Text(
            text = title, style = MaterialTheme.typography.headlineSmall.copy(
                brush = Brush.linearGradient(
                    colors = gradientColors
                )
            )
        )
    }, modifier = modifier.testTag("category:centerAlignedTopAppBar"), actions = {
        IconButton(onClick = onSettingsClick) {
            Icon(imageVector = SocialWorkReviewerIcons.Settings, contentDescription = "")
        }
    })
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
    OutlinedCard(modifier = modifier
        .fillMaxWidth()
        .padding(10.dp), onClick = {
        onCategoryClick(category.id)
    }) {
        DynamicAsyncImage(
            model = category.imageUrl,
            contentDescription = "categoryImage",
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = category.title, style = MaterialTheme.typography.headlineLarge)

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = category.description, style = MaterialTheme.typography.bodyLarge)
        }
    }
}