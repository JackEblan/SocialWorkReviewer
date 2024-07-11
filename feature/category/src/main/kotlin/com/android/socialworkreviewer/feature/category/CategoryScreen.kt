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
package com.android.socialworkreviewer.feature.category

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.android.socialworkreviewer.core.designsystem.component.SwrLoadingWheel
import com.android.socialworkreviewer.core.designsystem.icon.Swr
import com.android.socialworkreviewer.core.model.Announcement
import com.android.socialworkreviewer.core.model.Category
import kotlin.math.roundToInt

@Composable
internal fun CategoryRoute(
    modifier: Modifier = Modifier,
    viewModel: CategoryViewModel = hiltViewModel(),
    onCategoryClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
) {
    val categoryUiState = viewModel.categoryUiState.collectAsStateWithLifecycle().value

    CategoryScreen(
        modifier = modifier,
        categoryUiState = categoryUiState,
        onCategoryClick = onCategoryClick,
        onSettingsClick = onSettingsClick,
    )
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@VisibleForTesting
@Composable
internal fun CategoryScreen(
    modifier: Modifier = Modifier,
    categoryUiState: CategoryUiState,
    onCategoryClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
) {
    val topAppBarScrollBehavior = enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            CategoryTopAppBar(
                topAppBarScrollBehavior = topAppBarScrollBehavior,
                onSettingsClick = onSettingsClick,
            )
        },
    ) { paddingValues ->
        Box(
            modifier = modifier
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
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

                is CategoryUiState.Success -> {
                    if (categoryUiState.categories.isNotEmpty()) {
                        SuccessState(
                            modifier = modifier,
                            categoryUiState = categoryUiState,
                            contentPadding = paddingValues,
                            onCategoryClick = onCategoryClick,
                        )
                    } else {
                        EmptyState(text = "No Categories found!")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryTopAppBar(
    modifier: Modifier = Modifier,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    onSettingsClick: () -> Unit,
) {
    val gradientColors = listOf(Color(0xFF00BCD4), Color(0xFF03A9F4), Color(0xFF9C27B0))

    LargeTopAppBar(
        title = {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.headlineSmall.copy(
                    brush = Brush.linearGradient(
                        colors = gradientColors,
                    ),
                ),
            )
        },
        modifier = modifier.testTag("category:centerAlignedTopAppBar"),
        actions = {
            IconButton(onClick = onSettingsClick) {
                Icon(imageVector = Swr.Settings, contentDescription = "")
            }
        },
        scrollBehavior = topAppBarScrollBehavior,
    )
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    SwrLoadingWheel(
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
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(300.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag("category:lazyVerticalGrid"),
        contentPadding = contentPadding,
    ) {
        items(categoryUiState.announcements, key = { announcement ->
            announcement.id
        }) { announcement ->
            AnnouncementItem(modifier = Modifier.animateItem(), announcement = announcement)
        }

        items(categoryUiState.categories, key = { category ->
            category.id
        }) { category ->
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
            .padding(10.dp),
        onClick = {
            onCategoryClick(category.id)
        },
    ) {
        CategoryHeaderImage(
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

            Spacer(modifier = Modifier.height(10.dp))

            AverageCircularProgressIndicator(
                modifier = Modifier.align(Alignment.End),
                average = category.average,
            )
        }
    }
}

@Composable
private fun AnnouncementItem(
    modifier: Modifier = Modifier,
    announcement: Announcement?,
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            Text(
                text = announcement?.title ?: "Social Work Reviewer",
                style = MaterialTheme.typography.headlineSmall,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = announcement?.message
                    ?: "High quality questions to challenge your knowledge",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun CategoryHeaderImage(
    headerImageUrl: String?,
) {
    var isLoading by remember { mutableStateOf(true) }

    var isError by remember { mutableStateOf(false) }

    val imageLoader = rememberAsyncImagePainter(
        model = headerImageUrl,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        },
    )
    val isLocalInspection = LocalInspectionMode.current

    val transition = rememberInfiniteTransition(label = "Transition")

    val lightGrayAnimation by transition.animateColor(
        initialValue = Color.LightGray.copy(alpha = 0.2f),
        targetValue = Color.LightGray,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "LightGrayAnimation",
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .drawBehind {
                    drawRect(
                        color = if (isLoading) lightGrayAnimation else Color.Transparent,
                        size = size,
                    )
                },
            contentScale = ContentScale.Crop,
            painter = if (isError.not() && isLocalInspection.not()) {
                imageLoader
            } else {
                painterResource(com.android.socialworkreviewer.core.designsystem.R.drawable.ic_placeholder)
            },
            contentDescription = null,
        )
    }
}

@Composable
fun AverageCircularProgressIndicator(modifier: Modifier = Modifier, average: Double) {
    val animatedProgress by animateFloatAsState(
        targetValue = (average / 100).toFloat(),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "animatedProgress",
    )

    Box(modifier = modifier.size(60.dp)) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(5.dp),
            text = "${average.roundToInt()}%",
            style = MaterialTheme.typography.bodySmall,
        )

        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            progress = { animatedProgress },
            strokeCap = StrokeCap.Round,
            trackColor = ProgressIndicatorDefaults.linearTrackColor,
        )
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
