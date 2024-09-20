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
package com.eblan.socialworkreviewer.core.domain

import com.eblan.socialworkreviewer.core.data.repository.ChoiceRepository
import com.eblan.socialworkreviewer.core.model.Choice
import javax.inject.Inject

class UpdateChoiceUseCase @Inject constructor(
    private val choiceRepository: ChoiceRepository,
) {
    suspend operator fun invoke(choice: Choice) {
        if (choice.question.correctChoices.size > 1) {
            multipleChoices(choice = choice)
        } else {
            singleChoice(choice = choice)
        }
    }

    private suspend fun multipleChoices(choice: Choice) {
        if (choice in choiceRepository.selectedChoices) {
            choiceRepository.deleteChoice(choice)
        } else {
            choiceRepository.addChoice(choice)
        }
    }

    private suspend fun singleChoice(choice: Choice) {
        choiceRepository.selectedChoices.find { oldChoice ->
            choice.question == oldChoice.question && choice != oldChoice
        }?.let { oldChoice ->
            choiceRepository.replaceChoice(oldChoice = oldChoice, newChoice = choice)
        }

        if (choice !in choiceRepository.selectedChoices) {
            choiceRepository.addChoice(choice)
        }
    }
}
