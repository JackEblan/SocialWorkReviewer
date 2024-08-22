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
package com.eblan.socialworkreviewer.feature.question.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.eblan.socialworkreviewer.core.designsystem.icon.Swr
import com.eblan.socialworkreviewer.core.model.Category
import com.eblan.socialworkreviewer.core.model.QuestionSetting

@Composable
internal fun LoadingOnBoardingScreen(
    modifier: Modifier = Modifier,
    onGetCategory: () -> Unit,
) {
    LaunchedEffect(key1 = true) {
        onGetCategory()
    }

    LoadingScreen(modifier = modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SuccessOnBoardingScreen(
    modifier: Modifier = Modifier,
    category: Category,
    onStartQuestions: (Int, QuestionSetting) -> Unit,
    onStartQuickQuestions: () -> Unit,
) {
    val scrollBehavior = enterAlwaysScrollBehavior()

    Scaffold(floatingActionButton = {
        AnimatedVisibility(
            visible = scrollBehavior.state.collapsedFraction == 0.0f,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
        ) {
            FloatingActionButton(onClick = onStartQuickQuestions) {
                Icon(imageVector = Swr.Bolt, contentDescription = "")
            }
        }
    }, topBar = {
        OnBoardingTopAppBar(title = "Question Mode", scrollBehavior = scrollBehavior)
    }) { paddingValues ->
        LazyVerticalGrid(
            modifier = modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            columns = GridCells.Adaptive(300.dp),
            contentPadding = paddingValues,
        ) {
            itemsIndexed(category.questionSettings) { index, questionSetting ->
                OutlinedCard(modifier = Modifier.padding(10.dp), onClick = {
                    onStartQuestions(
                        index,
                        questionSetting,
                    )
                }) {
                    QuestionSettingItem(questionSetting = questionSetting)
                }
            }
        }
    }
}

@Composable
private fun QuestionSettingItem(modifier: Modifier = Modifier, questionSetting: QuestionSetting) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = Swr.Question,
                    contentDescription = "",
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "${questionSetting.numberOfQuestions} questions",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = Swr.AccessTime,
                    contentDescription = "",
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "${questionSetting.minutes} minutes",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OnBoardingTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val gradientColors = listOf(Color(0xFF00BCD4), Color(0xFF03A9F4), Color(0xFF9C27B0))

    LargeTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    brush = Brush.linearGradient(
                        colors = gradientColors,
                    ),
                ),
            )
        },
        modifier = modifier.testTag("onBoarding:largeTopAppBar"),
        scrollBehavior = scrollBehavior,
    )
}
