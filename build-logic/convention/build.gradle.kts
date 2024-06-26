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

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.android.socialworkreviewer.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "com.android.socialworkreviewer.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidApplicationCompose") {
            id = "com.android.socialworkreviewer.applicationCompose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }

        register("androidApplicationJacoco") {
            id = "com.android.socialworkreviewer.applicationJacoco"
            implementationClass = "AndroidApplicationJacocoConventionPlugin"
        }

        register("androidLibrary") {
            id = "com.android.socialworkreviewer.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("androidLibraryJacoco") {
            id = "com.android.socialworkreviewer.libraryJacoco"
            implementationClass = "AndroidLibraryJacocoConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = "com.android.socialworkreviewer.libraryCompose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }

        register("androidFeature") {
            id = "com.android.socialworkreviewer.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }

        register("androidHilt") {
            id = "com.android.socialworkreviewer.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }

        register("androidLint") {
            id = "com.android.socialworkreviewer.lint"
            implementationClass = "AndroidLintConventionPlugin"
        }

        register("androidTest") {
            id = "com.android.socialworkreviewer.test"
            implementationClass = "AndroidTestConventionPlugin"
        }

        register("jvmLibrary") {
            id = "com.android.socialworkreviewer.jvmLibrary"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}