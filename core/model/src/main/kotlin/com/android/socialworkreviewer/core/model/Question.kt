package com.android.socialworkreviewer.core.model

@NoArg
data class Question(
    val question: String, val choices: Array<Choice>, val answerId: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Question

        if (question != other.question) return false
        if (!choices.contentEquals(other.choices)) return false
        if (answerId != other.answerId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = question.hashCode()
        result = 31 * result + choices.contentHashCode()
        result = 31 * result + answerId.hashCode()
        return result
    }
}
