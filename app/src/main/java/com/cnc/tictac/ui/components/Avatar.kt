package com.cnc.tictac.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
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
    imageModifier: Modifier = Modifier
) {
    val color = if (onPrimaryColour) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
    Image(
        painter = painterResource(id = avatarResourceId),
        contentDescription = stringResource(id = contentDescriptionId),
        modifier = imageModifier,
        colorFilter = ColorFilter.tint(color),
        contentScale = ContentScale.FillWidth
    )
}
/* AvatarBlock
 *
 * Knows uses:
 *  - GameScreen: Player avatar card
 *  - UserSelectScreen: Player avatar card
 *  - Multiplayer screen: Player avatar card
 *
 * @param[avatarResourceId] e.g. R.drawable.<xxxx>
 * @param[isRound] Fully circle
 * @param[isFilled] Solid colour bg or not
 * @param[isBorderTransparent] Use transparent border?
 * @param[padding] Specify padding around avatar is different to default (24)
 * @param[onPrimaryColour] true for default i.e. on default bg
 * @param[boxModifier] applied to Image, default is .size(160dp)
 */
@Composable
fun AvatarBlock (
    avatarResourceId: Int,
    isCircle: Boolean = true,
    isFilled: Boolean = true,
    isBorderTransparent : Boolean = false,
    padding : Int = 24,
    boxModifier: Modifier = Modifier
) {
    val shape = if (isCircle) CircleShape else RoundedCornerShape(16.dp)
    val bgColor = if (isFilled) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
    val borderColor = if (isBorderTransparent) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onPrimary

    Box (
        modifier = boxModifier
            .clip(shape)
            .border(1.dp, borderColor, shape)
            .background(bgColor)
            .padding(padding.dp)
    ) {
        Avatar(
            onPrimaryColour = !isFilled,
            avatarResourceId = avatarResourceId,
        )
    }
}