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
package com.eblan.socialworkreviewer.core.ui

import androidx.compose.ui.tooling.preview.Preview

/**
 * Multipreview annotation that represents various device sizes. Add this annotation to a composable
 * to render various devices.
 */
@Preview(name = "phone", device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
@Preview(name = "landscape", device = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480")
@Preview(name = "foldable", device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480")
@Preview(name = "tablet", device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480")
annotation class DevicePreviews
