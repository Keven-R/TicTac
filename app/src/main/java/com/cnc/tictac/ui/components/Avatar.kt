package com.cnc.tictac.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cnc.tictac.R.drawable as images
import com.cnc.tictac.R.string as copy


/* Avatar
 *
 * Knows uses:
 *  - Profile: Display avatar in profile page
 *
 * @param[avatarResourceId] e.g. R.drawable.<xxxx>
 * @param[contentDescriptionId] e.g.  R.string.<xxxx>
 * @param[onPrimaryColour] true for default i.e. on default bg
 * @param[modifier] applied to Image, default is .size(160dp)
 */
@Composable
fun Avatar(
    avatarResourceId: Int = images.avatar_1,
    contentDescriptionId: Int = copy.avatar,
    onPrimaryColour: Boolean = true,
    modifier: Modifier = Modifier.size(160.dp)
) {
    val color = if (onPrimaryColour) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
    Image(
        painter = painterResource(id = avatarResourceId),
        contentDescription = stringResource(id = contentDescriptionId),
        modifier = modifier,
        colorFilter = ColorFilter.tint(color)
    )
}

/* AvatarFilled
 *
 * Knows uses:
 *  - GameScreen: Player avatar card
 *  - UserSelectScreen: Player avatar card
 *
 * @param[isRound] Fully circle
 * @param[isFilled] e.g.  R.string.<xxxx>
 * @param[onPrimaryColour] true for default i.e. on default bg
 * @param[modifier] applied to Image, default is .size(160dp)
 */
@Composable
fun AvatarBlock(
    isRound: Boolean = true,
    isFilled: Boolean = true,
    modifier: Modifier = Modifier.size(108.dp)
) {
    Box(modifier = modifier) {

    }
}

/* AvatarOutlined
 *
 * Knows uses:
 *  - GameScreen: Player avatar card
 */
@Composable
fun AvatarOutlined (
) {

}