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
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eblan.socialworkreviewer.core.designsystem.component.SwrLargeTopAppBar
import com.eblan.socialworkreviewer.core.designsystem.icon.Swr
import com.eblan.socialworkreviewer.core.designsystem.theme.LocalGradientColors
import com.eblan.socialworkreviewer.core.model.Category
import com.eblan.socialworkreviewer.core.model.QuestionSetting
import com.eblan.socialworkreviewer.core.model.Statistics
import kotlin.math.roundToInt

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
    statistics: Statistics,
    onStartQuestions: (Int, QuestionSetting) -> Unit,
    onStartQuickQuestions: () -> Unit,
) {
    val scrollBehavior = enterAlwaysScrollBehavior()

    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(
                visible = scrollBehavior.state.collapsedFraction == 0.0f,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
            ) {
                FloatingActionButton(onClick = onStartQuickQuestions) {
                    Icon(imageVector = Swr.Bolt, contentDescription = "")
                }
            }
        },
        topBar = {
            SwrLargeTopAppBar(
                title = {
                    Text(
                        text = "Question Mode",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            brush = Brush.linearGradient(
                                colors = LocalGradientColors.current.topBarTitleColorsDefault,
                            ),
                        ),
                    )
                },
                modifier = modifier.testTag("onBoarding:largeTopAppBar"),
                scrollBehavior = scrollBehavior,
            )
        },
    ) { paddingValues ->
        LazyVerticalGrid(
            modifier = modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .testTag("question:onBoarding:lazyVerticalStaggeredGrid"),
            columns = GridCells.Adaptive(300.dp),
            contentPadding = paddingValues,
        ) {
            item {
                Statistics(
                    modifier = modifier,
                    average = statistics.totalAverage,
                    totalScore = statistics.totalScore,
                    totalNumberOfQuestions = statistics.totalNumberOfQuestions,
                )
            }

            itemsIndexed(category.questionSettings) { index, questionSetting ->
                OutlinedCard(
                    modifier = Modifier.padding(10.dp),
                    onClick = {
                        onStartQuestions(
                            index,
                            questionSetting,
                        )
                    },
                ) {
                    OnBoardingItem(questionSetting = questionSetting)
                }
            }
        }
    }
}

@Composable
private fun OnBoardingItem(modifier: Modifier = Modifier, questionSetting: QuestionSetting) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .testTag("question:onBoarding:onBoardingItem"),
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

@Composable
private fun Statistics(
    modifier: Modifier = Modifier,
    average: Double,
    totalScore: Int,
    totalNumberOfQuestions: Int,
) {
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        AverageCircularProgressIndicator(modifier = Modifier.size(60.dp), average = average) {
            Text(
                modifier = Modifier.padding(5.dp),
                text = "${average.roundToInt()}%",
                style = MaterialTheme.typography.bodySmall,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Total Score", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(10.dp))

        Text(fontWeight = FontWeight.Bold, text = "$totalScore")

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Total Number Of Questions", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(10.dp))

        Text(fontWeight = FontWeight.Bold, text = "$totalNumberOfQuestions")
    }
}

@Composable
internal fun AverageCircularProgressIndicator(
    modifier: Modifier = Modifier,
    average: Double,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        content()

        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            progress = { (average / 100).toFloat() },
            strokeCap = StrokeCap.Round,
            trackColor = ProgressIndicatorDefaults.linearTrackColor,
        )
    }
}
