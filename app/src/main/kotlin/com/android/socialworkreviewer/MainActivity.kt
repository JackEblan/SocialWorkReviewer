package com.android.socialworkreviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.android.socialworkreviewer.core.designsystem.component.SocialWorkReviewerBackground
import com.android.socialworkreviewer.core.designsystem.theme.SocialWorkReviewerTheme
import com.android.socialworkreviewer.navigation.SocialWorkReviewerNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            SocialWorkReviewerTheme(
                darkTheme = false,
                androidTheme = true,
                disableDynamicTheming = false,
            ) {
                SocialWorkReviewerBackground {
                    SocialWorkReviewerNavHost(navController = navController)
                }
            }
        }
    }
}