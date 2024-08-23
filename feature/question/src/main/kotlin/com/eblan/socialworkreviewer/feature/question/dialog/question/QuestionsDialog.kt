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
package com.eblan.socialworkreviewer.feature.question.dialog.question

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.eblan.socialworkreviewer.core.designsystem.icon.Swr
import com.eblan.socialworkreviewer.core.model.Question

@Composable
internal fun QuestionsDialog(
    modifier: Modifier = Modifier,
    minutes: String?,
    questions: List<Question>,
    questionsWithSelectedChoices: Map<Question, List<String>>,
    onQuestionClick: (Int) -> Unit,
    onOkayClick: () -> Unit,
    contentDescription: String,
) {
    QuestionsDialogContainer(
        modifier = modifier
            .padding(16.dp)
            .semantics { this.contentDescription = contentDescription },
        onDismissRequest = {},
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            QuestionsDialogTitle()

            QuestionsDialogContent(
                minutes = minutes,
                questions = questions,
                questionsWithSelectedChoices = questionsWithSelectedChoices,
                onQuestionClick = onQuestionClick,
            )

            QuestionsDialogButtons(
                onOkayClick = onOkayClick,
            )
        }
    }
}

@Composable
private fun QuestionsDialogContainer(
    modifier: Modifier = Modifier,
    shape: Shape = AlertDialogDefaults.shape,
    containerColor: Color = AlertDialogDefaults.containerColor,
    tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = modifier,
            shape = shape,
            color = containerColor,
            tonalElevation = tonalElevation,
            content = content,
        )
    }
}

@Composable
private fun QuestionsDialogTitle(modifier: Modifier = Modifier) {
    Spacer(modifier = Modifier.height(10.dp))

    Text(
        modifier = modifier,
        text = "Questions",
        style = MaterialTheme.typography.titleLarge,
    )
}

@Composable
private fun QuestionsDialogContent(
    minutes: String?,
    questions: List<Question>,
    questionsWithSelectedChoices: Map<Question, List<String>>,
    onQuestionClick: (Int) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            modifier = Modifier,
            text = minutes ?: "Time's up!",
            style = MaterialTheme.typography.headlineLarge,
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            modifier = Modifier,
            textAlign = TextAlign.Center,
            text = "Answered ${questionsWithSelectedChoices.size}/${questions.size} questions",
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyRow(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(questions) { index, question ->
                QuestionItem(
                    index = index,
                    question = question,
                    questionsWithSelectedChoices = questionsWithSelectedChoices,
                    onQuestionClick = onQuestionClick,
                )
            }
        }
    }
}

@Composable
private fun QuestionItem(
    modifier: Modifier = Modifier,
    index: Int,
    question: Question,
    questionsWithSelectedChoices: Map<Question, List<String>>,
    onQuestionClick: (Int) -> Unit,
) {
    Card(
        modifier = modifier
            .size(100.dp)
            .padding(5.dp),
        onClick = { onQuestionClick(index) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "${index + 1}")

            Spacer(modifier = Modifier.height(10.dp))

            Icon(
                imageVector = if (question in questionsWithSelectedChoices.keys) Swr.Check else Swr.Close,
                contentDescription = "",
            )
        }
    }
}

@Composable
private fun QuestionsDialogButtons(
    modifier: Modifier = Modifier,
    onOkayClick: () -> Unit,
) {
    Spacer(modifier = Modifier.height(10.dp))

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        TextButton(
            onClick = onOkayClick,
            modifier = Modifier.padding(5.dp),
        ) {
            Text(text = "Okay")
        }
    }
}
