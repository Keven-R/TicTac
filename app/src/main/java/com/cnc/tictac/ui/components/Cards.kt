package com.cnc.tictac.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cnc.tictac.Destination
import com.cnc.tictac.viewmodel.PLAYERWINSTATUS
import com.cnc.tictac.viewmodel.TicTacEvent
import com.cnc.tictac.viewmodel.TicTacViewModel
import com.cnc.tictac.viewmodel.UIPLAYERSELECT
import com.cnc.tictac.R.string as copy

/* GamePlayerCard
 *
 * Knows uses:
 *  - GameScreen: Player information when game active or ended.
 *
 * REQUIRED PARAMS
 * @param[isRowLayout] Layout of card
 * @param[playerName] String of player's name
 * @param[playerAvatarResourceId] Resource ID of player's chosen avatar
 * @param[playerMarker] "x" or "o
 * @param[isGameEnded] True if game completed, false if ongoing
 *
 * OPTIONAL PARAMS
 * -- Game is ongoing
 * @param[isPlayerTurn] True if player turn, else false
 * @param[secondsLeft] 0 by default (not player's turn), otherwise 0-10
 *
 * -- Game is ended
 * @param[playerWinStatus] Win/loss/draw status for that player. See enum below.
 */
//enum class PLAYERWINSTATUS { LOSS, DRAW, WIN }
@Composable
fun GamePlayerCard (
    modifier: Modifier = Modifier,
    isRowLayout: Boolean,
    inverse: Boolean = false, // True for P2 in Medium+ width / portrait orientation
    playerName: String,
    playerAvatarResourceId: Int,
    playerMarker: String = "x", // "x" or "o"
    isGameEnded: Boolean,
    showTimer: Boolean = true,

    // if Game is ongoing, require:
    isPlayerTurn: Boolean = false,
    secondsLeft: Int = 0, // 0 if not player's turn

    // if Game is ended, require:
    playerWinStatus: Enum<PLAYERWINSTATUS> = PLAYERWINSTATUS.DRAW // TODO: not sure what our viewmodel uses, pls change
) {
    // Avatar design and content logic
    var borderTransparent: Boolean
    var isFilled: Boolean
    var playerNameLabel: String
    var playerStatusLabel: String
    var transparentIndex: Int
    val didPlayerWin = playerWinStatus == PLAYERWINSTATUS.WIN
    var padding = 16
    val alignment = if (inverse) Alignment.End else Alignment.Start
    var avatarSize = 104
    var timerOpacity = if (isGameEnded) 1f else if (isPlayerTurn) 1f else if (!showTimer) 0f else 1f

    if (isGameEnded) {
        // Design and content if game has ended
        borderTransparent = !didPlayerWin   // Transparent if not winner
        isFilled = didPlayerWin // Fill avatar block if player win
        playerNameLabel = playerName
        playerStatusLabel = when (playerWinStatus) {
            PLAYERWINSTATUS.WIN -> {
                stringResource(id = copy.game_status_win)
            }

            PLAYERWINSTATUS.LOSS -> {
                stringResource(id = copy.game_status_loss)
            }

            else -> {
                stringResource(id = copy.game_status_draw)
            }
        }
        transparentIndex = playerStatusLabel.length
    } else {
        // Design and content if game is ongoing
        borderTransparent = !isPlayerTurn   // Transparent if not current turn
        isFilled = isPlayerTurn // Fill if player's turn
        playerNameLabel = if (isPlayerTurn) {
            "$playerName's turn"
        } else {
            playerName
        } // Append if current turn
        playerStatusLabel = playerMarker.repeat(10)
        transparentIndex = if (isPlayerTurn) secondsLeft else 0
    }

    if (isRowLayout) {
        // Use this layout for mobile landscape and ALL portrait
        ReversibleRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            reverseLayout = inverse
        ) {
            AvatarBlock(
                avatarResourceId = playerAvatarResourceId,
                isCircle = true,
                isFilled = isFilled,
                isBorderTransparent = borderTransparent,
                boxModifier = Modifier.width(avatarSize.dp),
                padding = padding
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = alignment
            ) {
                Text(
                    text = playerNameLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                MarkerGraphics(
                    content = playerStatusLabel,
                    transparentIndex = transparentIndex,
                    alignCenter = false,
                    rowModifier = Modifier.alpha(timerOpacity),
                    textModifier = Modifier
                )
            }
        }
    } else {
        // Use this layout for non-mobile landscape
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = alignment
        ) {
            AvatarBlock(
                avatarResourceId = playerAvatarResourceId,
                isCircle = true,
                isFilled = isFilled,
                isBorderTransparent = borderTransparent,
                boxModifier = Modifier.width(avatarSize.dp),
                padding = padding
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = alignment
            ) {
                Text(
                    text = playerNameLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                MarkerGraphics(
                    content = playerStatusLabel,
                    transparentIndex = transparentIndex,
                    alignCenter = false,
                    rowModifier = Modifier.alpha(timerOpacity),
                    textModifier = Modifier,
                )
            }
        }
    }
}

@Composable
fun ReversibleRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    reverseLayout: Boolean = false,
    content: @Composable RowScope.() -> Unit
) {
    val originDirection = LocalLayoutDirection.current
    val direction = when {
        reverseLayout -> when (originDirection) {
            LayoutDirection.Rtl -> LayoutDirection.Ltr
            else -> LayoutDirection.Rtl
        }
        else -> originDirection
    }
    CompositionLocalProvider(LocalLayoutDirection provides direction) {
        Row(modifier, horizontalArrangement, verticalAlignment) {
            CompositionLocalProvider(LocalLayoutDirection provides originDirection) {
                content()
            }
        }
    }
}

/* MarkerGraphics
 *
 * Knows uses:
 *  - ProfileScreen: win ratio graphic
 *  - GameScreen: timer
 *
 * @param[content] 10 char long string to display e.g. "ooo/////xx"
 * @param[transparentIndex] String index of when transparency begins (timer).
 * @param[rowModifier] Modifier for row
 * @param[textModifier] Modifier for text
 */
@Composable
fun MarkerGraphics (
    content: String,
    transparentIndex: Int = 10,
    alignCenter : Boolean = true,
    rowModifier: Modifier = Modifier,
    textModifier: Modifier = Modifier
) {
    // Center align fo profile, start for game screen.
    val rowAlignment = if (alignCenter) Alignment.CenterHorizontally else Alignment.Start

    Row (
        modifier = rowModifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp, alignment = rowAlignment)
    ) {
        for (currIndex in content.indices) {
            // Add transparency for timer
            if (currIndex >= transparentIndex) {
                Text(
                    text = content[currIndex].toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = textModifier.alpha(0.16f),
                )
            // Default styling
            } else {
                Text(
                    text = content[currIndex].toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = textModifier,
                )
            }
        }
    }
}

/* PlayerSelectCard
 *
 * Knows uses:
 *  - MultiplayerSettingsScreen
 *
 * @param[isHorizontal] True if displaying card horizontally
 * @param[playerName] Use's name to display
 * @param[isPlayerOne] True if player one
 * @param[avatarResourceId] Avatar resource id for player
 * @param[layoutModifier] Modifier for row or column
 * @param[avatarModifier] Modifier for avatar image
 */
@Composable
fun PlayerSelectCard (
    isHorizontal: Boolean = true,
    viewModel: TicTacViewModel,
    navController: NavHostController,
    destination: Destination,
    playerName: String = "Guest",
    isPlayerOne: Boolean = true,
    avatarResourceId: Int,
    layoutModifier: Modifier = Modifier,
    avatarModifier: Modifier = Modifier
) {
    val playerDescId = if (isPlayerOne) copy.settings_p1 else copy.settings_p2

    if (isHorizontal) {
        Row (
            modifier = layoutModifier.height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ELEMENT: Player avatar
            AvatarBlock(
                avatarResourceId = avatarResourceId,
                isCircle = false,
                isFilled = false,
                boxModifier = avatarModifier
                    .weight(3f)
                    .fillMaxWidth()
            )

            // CONTAINER: Player info and button
            Column (
                modifier = Modifier
                    .weight(4f)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // CONTAINER: Player info
                Column (
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // ELEMENT: Player 1 or 2
                    Text(
                        text = stringResource(id = playerDescId),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    // ELEMENT: Player name
                    BodyLarge(
                        content = playerName,
                    )
                }

                // ELEMENT: BUTTON
                SecondaryButton(
                    label = stringResource(id = copy.settings_change_player),
                    modifier = Modifier.fillMaxWidth()
                ){
                    if(isPlayerOne) {
                        viewModel.uiSelectedPlayer = UIPLAYERSELECT.PLAYER1
                        viewModel.findAvatar()
                    } else{
                        viewModel.uiSelectedPlayer = UIPLAYERSELECT.PLAYER2
                        viewModel.findAvatar()
                    }
                    navController.navigate(destination.route)
                }
            }
        }
    } else {
        Column (
            modifier = layoutModifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // ELEMENT: Player avatar
            AvatarBlock(
                avatarResourceId = avatarResourceId,
                isCircle = false,
                isFilled = false,
                boxModifier = avatarModifier.fillMaxWidth()
            )

            // ELEMENT: Player 1 or 2
            BodyMedium (
                content = stringResource(id = playerDescId),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            // ELEMENT: Player name
            BodyLarge(
                content = playerName,
                rowModifier = Modifier.fillMaxWidth(),
                isCenter = true
            )

            // ELEMENT: BUTTON
            SecondaryButton(
                label = stringResource(id = copy.settings_change_player),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            ){
                if(isPlayerOne) {
                    viewModel.uiSelectedPlayer = UIPLAYERSELECT.PLAYER1
                    viewModel.userSelectIndex = viewModel.findUser()
                } else{
                    viewModel.uiSelectedPlayer = UIPLAYERSELECT.PLAYER2
                    viewModel.userSelectIndex = viewModel.findUser()
                }
                navController.navigate(destination.route)
            }
        }
    }
}

@Composable
fun UserCell(
    modifier: Modifier = Modifier,
    avatarModifier: Modifier= Modifier,
    viewModel: TicTacViewModel,
    playerName: String = "Guest",
    isSelected: Boolean = true,
    avatarResourceId: Int?,
    padding: Int = 24,
    position: Int
) {
    Column (
        modifier = modifier.clickable {
            viewModel.userSelectIndex = position
            viewModel.onEvent(TicTacEvent.ChangePlayer(viewModel.users[position]))
        },
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // ELEMENT: Player avatar
        if (avatarResourceId != null) {
            AvatarBlock(
                avatarResourceId = avatarResourceId,
                isCircle = false,
                isFilled = isSelected,
                isBorderTransparent = false,
                padding = padding,
                boxModifier = avatarModifier.fillMaxWidth()
            )
        }

        // ELEMENT: Player name
        Text(
            text = playerName,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun CardButton(
    modifier: Modifier = Modifier,
    boxModifier: Modifier = Modifier,
    label: String = "card button",
    icon: String = "+",
    onclick: () -> Unit,
) {
    Column (
        modifier = modifier.clickable { onclick },
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val shape = RoundedCornerShape(16.dp)
        val color = MaterialTheme.colorScheme.outline
        Box (
            modifier = boxModifier
                .clip(shape)
                .border(1.dp, color, shape)
                .background(MaterialTheme.colorScheme.primary)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            // ELEMENT: Icon
            Text(
                text = icon,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // ELEMENT: Label
        BodyLarge(
            content = label,
            rowModifier = Modifier.fillMaxWidth(),
            isCenter = true
        )
    }
}

@Composable
fun NewUserCardButton(
    modifier: Modifier = Modifier,
    boxModifier: Modifier = Modifier,
    viewModel: TicTacViewModel,
    navController: NavHostController,
    label: String = "card button",
    icon: String = "+"
) {
    Column (
        modifier = modifier.clickable {
            viewModel.newUser = true
//            viewModel.playerTextFieldValue = viewModel.findEditTextValue()
            viewModel.playerTextFieldValue
            viewModel.selectedAvatar = 0
            navController.navigate(Destination.UserDetailScreen.route)
        },
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val shape = RoundedCornerShape(16.dp)
        val color = MaterialTheme.colorScheme.outline
        Box (
            modifier = boxModifier
                .clip(shape)
                .border(1.dp, color, shape)
                .background(MaterialTheme.colorScheme.primary)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            // ELEMENT: Icon
            Text(
                text = icon,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // ELEMENT: Label
        BodyLarge(
            content = label,
            rowModifier = Modifier.fillMaxWidth(),
            isCenter = true
        )
    }
}