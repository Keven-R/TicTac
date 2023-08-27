package com.cnc.tictac.ui.system

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


/* COMPOSABLE
 * getDeviceInfo
 *
 * Used in the entire com.cnc.tictac.ui.screens packages to determine device size and
 * display the correct layout for both mobile and tablet sizes in landscape and portrait.
 *
 * @return[DeviceInfo] Contains device type (see DeviceInfo doc) and exact screen sizes.
 */
@Composable
fun getDeviceInfo(): DeviceInfo {
    val configuration = LocalConfiguration.current
    return DeviceInfo(
        screenWidthType = when {
            configuration.screenWidthDp < 600 -> DeviceInfo.DeviceType.Compact
            configuration.screenWidthDp < 840 -> DeviceInfo.DeviceType.Medium
            else -> DeviceInfo.DeviceType.Expanded
        },
        screenHeightType = when {
            configuration.screenHeightDp < 480 -> DeviceInfo.DeviceType.Compact
            configuration.screenHeightDp < 900 -> DeviceInfo.DeviceType.Medium
            else -> DeviceInfo.DeviceType.Expanded
        },
        screenWidth = configuration.screenWidthDp.dp,
        screenHeight = configuration.screenHeightDp.dp
    )
    }

/* CLASS
 * DeviceInfo
 *
 * screenWidthType & screenHeightType: Compact, Medium, or Expanded.
 *      Compact = Mobile portrait
 *      Compact = Mobile landscape + tablet portrait
 *      Compact = Tablet landscape
 *
 * screenWidth: width of screen in Dp.
 * screenHeight: width of screen in Dp.
 *
 * Info: https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes
 */
data class DeviceInfo(
    val screenWidthType: DeviceType,
    val screenHeightType: DeviceType,
    val screenWidth: Dp,
    val screenHeight: Dp
) {
    sealed class DeviceType {
        object Compact: DeviceType()
        object Medium: DeviceType()
        object Expanded: DeviceType()
    }
}