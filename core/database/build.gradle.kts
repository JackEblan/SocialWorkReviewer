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

plugins {
    alias(libs.plugins.com.android.socialworkreviewer.library)
    alias(libs.plugins.com.android.socialworkreviewer.libraryJacoco)
    alias(libs.plugins.com.android.socialworkreviewer.hilt)
    alias(libs.plugins.com.android.socialworkreviewer.room)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    namespace = "com.android.socialworkreviewer.core.database"

    sourceSets {
        getByName("androidTest").assets.srcDir("$projectDir/schemas")
    }
}

dependencies {
    androidTestImplementation(libs.room.testing)
    androidTestImplementation(projects.core.testing)
}