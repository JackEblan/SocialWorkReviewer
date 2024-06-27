package com.android.socialworkreviewer.feature.question

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.android.socialworkreviewer.core.model.QuestionSetting

@Composable
internal fun QuestionSettingsScreen(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    questionSettings: List<QuestionSetting>,
    onGetQuestions: (Int, Int) -> Unit,
) {
    val (selectedQuickSetting, onQuickSettingSelected) = remember { mutableStateOf(questionSettings[0]) }

    Scaffold { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "How many questions would you like to take?",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    Modifier
                        .selectableGroup()
                        .verticalScroll(scrollState)) {
                    questionSettings.forEach { questionSetting ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = (questionSetting == selectedQuickSetting),
                                    onClick = { onQuickSettingSelected(questionSetting) },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (questionSetting == selectedQuickSetting), onClick = null
                            )
                            Text(
                                text = "${questionSetting.numberOfQuestions} Questions in ${questionSetting.minutes} minutes",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Checkbox(checked = true, onCheckedChange = {})

                    Text(text = "Quick Mode", style = MaterialTheme.typography.bodyLarge)
                }

                Button(onClick = {
                    onGetQuestions(
                        selectedQuickSetting.numberOfQuestions, selectedQuickSetting.minutes
                    )
                }) {
                    Text(text = "Start")
                }
            }
        }
    }
}