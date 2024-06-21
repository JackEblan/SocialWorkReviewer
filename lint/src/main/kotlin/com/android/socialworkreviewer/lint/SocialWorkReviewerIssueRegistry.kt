package com.android.socialworkreviewer.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API

class SocialWorkReviewerIssueRegistry : IssueRegistry() {

    override val issues = listOf(
        TestMethodNameDetector.FORMAT,
        TestMethodNameDetector.PREFIX,
    )

    override val api: Int = CURRENT_API

    override val minApi: Int = 12

    override val vendor: Vendor = Vendor(
        vendorName = "SocialWorkReviewer",
        feedbackUrl = "https://github.com/JackEblan",
        contact = "https://github.com/JackEblan",
    )
}
