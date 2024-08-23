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
package com.eblan.socialworkreviewer.feature.question.dialog.quit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
internal fun QuestionsDataDialog(
    modifier: Modifier = Modifier,
    minutes: String?,
    onOkayClick: () -> Unit,
    contentDescription: String,
) {
    QuestionsDataDialogContainer(
        modifier = modifier
            .width(IntrinsicSize.Max)
            .height(IntrinsicSize.Min)
            .padding(16.dp)
            .semantics { this.contentDescription = contentDescription },
        onDismissRequest = {},
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
        ) {
            QuestionsDataDialogTitle()

            QuestionsDataDialogContent(minutes = minutes)

            QuestionsDataDialogButtons(
                modifier = modifier.fillMaxWidth(),
                onOkayClick = onOkayClick,
            )
        }
    }
}

@Composable
private fun QuestionsDataDialogContainer(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    content: @Composable (ColumnScope.() -> Unit),
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
        ) {
            content()
        }
    }
}

@Composable
private fun QuestionsDataDialogTitle(modifier: Modifier = Modifier) {
    Spacer(modifier = Modifier.height(10.dp))

    Text(
        modifier = modifier,
        text = "Question Data",
        style = MaterialTheme.typography.titleLarge,
    )
}

@Composable
private fun QuestionsDataDialogContent(modifier: Modifier = Modifier, minutes: String?) {
    Spacer(modifier = modifier.height(10.dp))

    Text(
        modifier = modifier,
        text = minutes ?: "Time's up!",
        style = MaterialTheme.typography.titleLarge,
    )
}

@Composable
private fun QuestionsDataDialogButtons(
    modifier: Modifier = Modifier,
    onOkayClick: () -> Unit,
) {
    Spacer(modifier = Modifier.height(10.dp))

    Row(
        modifier = modifier,
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
