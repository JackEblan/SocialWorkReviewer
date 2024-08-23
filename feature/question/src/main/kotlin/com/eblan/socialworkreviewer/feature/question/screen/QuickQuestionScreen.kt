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

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.dp
import com.eblan.socialworkreviewer.core.designsystem.icon.Swr
import com.eblan.socialworkreviewer.core.designsystem.theme.LocalGradientColors
import com.eblan.socialworkreviewer.core.model.Choice
import com.eblan.socialworkreviewer.core.model.Question
import com.eblan.socialworkreviewer.feature.question.dialog.quit.QuitAlertDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QuickQuestionsScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    questions: List<Question>,
    selectedChoices: List<String>,
    onAddCurrentQuestion: (Question) -> Unit,
    onUpdateChoice: (Choice) -> Unit,
    onQuitQuestions: () -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = {
        questions.size
    })

    val scrollBehavior = enterAlwaysScrollBehavior()

    val animatedProgress by animateFloatAsState(
        targetValue = (pagerState.currentPage + 1f) / questions.size,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "animatedProgress",
    )

    var showQuitAlertDialog by rememberSaveable {
        mutableStateOf(false)
    }

    BackHandler(enabled = true) {
        showQuitAlertDialog = true
    }

    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onAddCurrentQuestion(questions[page])
        }
    }

    Scaffold(
        topBar = {
            QuickQuestionTopAppBar(
                title = "Quick Questions",
                scrollBehavior = scrollBehavior,
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues),
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .padding(10.dp),
                progress = {
                    animatedProgress
                },
                strokeCap = StrokeCap.Round,
            )

            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                state = pagerState,
            ) { page ->
                QuickQuestionPage(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    page = page,
                    isScrollInProgress = pagerState.isScrollInProgress,
                    questions = questions,
                    selectedChoices = selectedChoices,
                    onUpdateChoice = onUpdateChoice,
                )
            }
        }
    }

    if (showQuitAlertDialog) {
        QuitAlertDialog(
            onDismissRequest = {
                showQuitAlertDialog = false
            },
            onConfirmation = {
                showQuitAlertDialog = false
                onQuitQuestions()
            },
            dialogTitle = "Quit Quick Questions",
            dialogText = "Are you sure you want to quit?",
            icon = Swr.Question,
        )
    }
}

@Composable
private fun QuickQuestionPage(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    page: Int,
    isScrollInProgress: Boolean,
    questions: List<Question>,
    selectedChoices: List<String>,
    onUpdateChoice: (Choice) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        QuickQuestionText(question = questions[page].question)

        ChoicesType(
            numberOfChoices = questions[page].correctChoices.size,
        )

        QuickQuestionChoicesSelection(
            isScrollInProgress = isScrollInProgress,
            choices = questions[page].choices,
            correctChoices = questions[page].correctChoices,
            selectedChoices = selectedChoices,
            onUpdateChoice = { choice ->
                onUpdateChoice(
                    Choice(
                        question = questions[page],
                        choice = choice,
                    ),
                )
            },
        )
    }
}

@Composable
private fun QuickQuestionText(modifier: Modifier = Modifier, question: String) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Text(
            text = AnnotatedString.fromHtml(question),
            style = MaterialTheme.typography.headlineSmall,
        )
    }

    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
private fun QuickQuestionChoicesSelection(
    modifier: Modifier = Modifier,
    isScrollInProgress: Boolean,
    choices: List<String>,
    correctChoices: List<String>,
    selectedChoices: List<String>,
    onUpdateChoice: (String) -> Unit,
) {
    val wrongChoiceColor = CheckboxDefaults.colors().copy(
        checkedBoxColor = MaterialTheme.colorScheme.error,
        checkedBorderColor = MaterialTheme.colorScheme.error,
    )

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        choices.forEach { choice ->
            val selectedChoice = isScrollInProgress.not() && choice in selectedChoices

            val correctChoice =
                isScrollInProgress.not() && selectedChoices.size == correctChoices.size && choice in correctChoices

            val wrongChoice =
                isScrollInProgress.not() && selectedChoices.size == correctChoices.size && choice !in correctChoices && choice in selectedChoices

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (selectedChoice.not() && selectedChoices.size < correctChoices.size) {
                            onUpdateChoice(choice)
                        }
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = selectedChoice || correctChoice || wrongChoice,
                    onCheckedChange = {
                        if (selectedChoice.not() && selectedChoices.size < correctChoices.size) {
                            onUpdateChoice(choice)
                        }
                    },
                    colors = if (wrongChoice) wrongChoiceColor else CheckboxDefaults.colors(),
                )

                Box(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = choice)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuickQuestionTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    LargeTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    brush = Brush.linearGradient(
                        colors = LocalGradientColors.current.topBarTitleColorsDefault,
                    ),
                ),
            )
        },
        modifier = modifier.testTag("quickQuestion:largeTopAppBar"),
        scrollBehavior = scrollBehavior,
    )
}
