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
import com.android.socialworkreviewer.SwrBuildType

plugins {
    alias(libs.plugins.com.android.socialworkreviewer.application)
    alias(libs.plugins.com.android.socialworkreviewer.applicationCompose)
    alias(libs.plugins.com.android.socialworkreviewer.applicationJacoco)
    alias(libs.plugins.com.android.socialworkreviewer.hilt)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.android.socialworkreviewer"

    defaultConfig {
        applicationId = "com.android.socialworkreviewer"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = SwrBuildType.DEBUG.applicationIdSuffix
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            applicationIdSuffix = SwrBuildType.RELEASE.applicationIdSuffix
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )

            // Ensure Baseline Profile is fresh for release builds.
            baselineProfile.automaticGenerationDuringBuild = true
        }
    }
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.designSystem)
    implementation(projects.core.model)

    implementation(projects.feature.category)
    implementation(projects.feature.question)
    implementation(projects.feature.settings)

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.kotlinx.serialization.json)

    debugImplementation(libs.androidx.compose.ui.test.manifest)

    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(projects.core.testing)

    baselineProfile(projects.benchmarks)
}

baselineProfile {
    // Don't build on every iteration of a full assemble.
    // Instead enable generation directly for the release build variant.
    automaticGenerationDuringBuild = false
    dexLayoutOptimization = true
}

dependencyGuard {
    configuration("releaseRuntimeClasspath")
}