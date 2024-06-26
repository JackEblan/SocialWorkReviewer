package com.android.socialworkreviewer.feature.question

import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.socialworkreviewer.core.model.Answer
import com.android.socialworkreviewer.core.model.Question

@Composable
fun AnswerScreen(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    questions: List<Question>,
    answers: List<Answer>,
) {
    val pagerState = rememberPagerState(pageCount = {
        questions.size
    })

    Scaffold { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues),
        ) {
            QuestionHeader(
                questionIndex = pagerState.currentPage, questionSize = questions.size
            )

            AnswerPager(
                pagerState = pagerState,
                scrollState = scrollState,
                questions = questions,
                answers = answers,
            )
        }
    }
}

@Composable
private fun AnswerPager(
    pagerState: PagerState,
    scrollState: ScrollState = rememberScrollState(),
    questions: List<Question>,
    answers: List<Answer>,
) {
    HorizontalPager(state = pagerState) { page ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            QuestionText(question = questions[page].question)

            Choices(choices = questions[page].correctChoices.plus(questions[page].wrongChoices),
                    correctChoices = questions[page].correctChoices,
                    selectedChoices = answers.map { it.choice })
        }
    }

}

@Composable
private fun Choices(
    modifier: Modifier = Modifier,
    choices: List<String>,
    correctChoices: List<String>,
    selectedChoices: List<String>,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        choices.forEach { choice ->
            val correctAnswer = choice in correctChoices || choice in selectedChoices

            val wrongAnswer = choice !in correctChoices && choice in selectedChoices

            val wrongAnswerColor = CheckboxDefaults.colors().copy(
                checkedBoxColor = Color.Red, checkedBorderColor = Color.Red
            )

            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = correctAnswer || wrongAnswer,
                    onCheckedChange = {},
                    colors = if (wrongAnswer) wrongAnswerColor else CheckboxDefaults.colors()
                )

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = choice)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
