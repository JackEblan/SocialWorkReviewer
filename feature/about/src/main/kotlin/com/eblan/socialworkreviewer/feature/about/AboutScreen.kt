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

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.eblan.socialworkreviewer.core.designsystem.component.SwrLoadingWheel
import com.eblan.socialworkreviewer.core.designsystem.icon.Swr
import com.eblan.socialworkreviewer.core.model.About

@Composable
internal fun AboutRoute(
    modifier: Modifier = Modifier,
    viewModel: AboutViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit,
) {
    val aboutUiState = viewModel.aboutUiState.collectAsStateWithLifecycle().value

    val openLinkResult = viewModel.openLinkResult.collectAsStateWithLifecycle().value

    LaunchedEffect(key1 = openLinkResult) {
        if (openLinkResult != null && openLinkResult.not()) {
            onShowSnackBar("Invalid link")
            viewModel.resetOpenLinkResult()
        }
    }

    AboutScreen(modifier = modifier, aboutUiState = aboutUiState, onLinkCLick = viewModel::openLink)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun AboutScreen(
    modifier: Modifier = Modifier,
    aboutUiState: AboutUiState,
    onLinkCLick: (String) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .semantics {
                testTagsAsResourceId = true
            }
            .testTag("about"),
    ) {
        when (aboutUiState) {
            AboutUiState.Loading -> LoadingState(
                modifier = Modifier.align(Alignment.Center),
            )

            is AboutUiState.Success -> {
                if (aboutUiState.abouts.isNotEmpty()) {
                    SuccessState(
                        modifier = modifier,
                        aboutUiState = aboutUiState,
                        onLinkCLick = onLinkCLick,
                    )
                } else {
                    EmptyState(text = "No Information found!")
                }
            }
        }
    }
}

@Composable
private fun SuccessState(
    modifier: Modifier = Modifier,
    aboutUiState: AboutUiState.Success,
    onLinkCLick: (String) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(300.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag("about:lazyVerticalStaggeredGrid"),
    ) {
        items(
            aboutUiState.abouts,
            key = { about ->
                about.id
            },
        ) { about ->
            AboutItem(modifier = Modifier.animateItem(), about = about, onLinkCLick = onLinkCLick)
        }
    }
}

@Composable
private fun AboutItem(
    modifier: Modifier = Modifier,
    about: About,
    onLinkCLick: (String) -> Unit,
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AboutImage(headerImageUrl = about.imageUrl)

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = about.title, style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = about.name)

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = about.message,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
            )

            Spacer(modifier = Modifier.height(10.dp))

            WebLinks(links = about.links, onLinkCLick = onLinkCLick)
        }
    }
}

@Composable
private fun WebLinks(
    modifier: Modifier = Modifier,
    links: List<String>,
    onLinkCLick: (String) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        links.forEach { link ->
            IconButton(
                onClick = {
                    onLinkCLick(link)
                },
            ) {
                Icon(
                    imageVector = Swr.Link,
                    contentDescription = "",
                )
            }
        }
    }
}

@Composable
private fun AboutImage(
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
            .size(50.dp)
            .clip(CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
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
                painterResource(com.eblan.socialworkreviewer.core.designsystem.R.drawable.ic_placeholder)
            },
            contentDescription = null,
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
            .testTag("about:emptyListPlaceHolderScreen"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Swr.Info,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    SwrLoadingWheel(
        modifier = modifier,
        contentDescription = "SwrLoadingWheel",
    )
}
