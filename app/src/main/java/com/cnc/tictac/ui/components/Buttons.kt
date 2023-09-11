package com.cnc.tictac.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cnc.tictac.Destination
import com.cnc.tictac.viewmodel.TicTacEvent
import com.cnc.tictac.viewmodel.TicTacViewModel
import com.cnc.tictac.R.string as copy

/* DisplayButton
 *
 * Knows uses:
 *  - HomeScreen: Game mode select
 *
 * @param[label] Button text label
 * @param[onClickAction] Action performed on click
 */

//@Composable
//fun DisplayButton(
//    label: String,
//    onClickEvent: () -> Unit
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { onClickEvent }
//    ) {
//        Text(
//            text = label,
//            style = MaterialTheme.typography.headlineLarge,
//            color = MaterialTheme.colorScheme.onPrimary,
//        )
//    }
//}

@Composable
fun DisplayButton(
    label: String,
    viewModel: TicTacViewModel,
    event: TicTacEvent,
    navController: NavHostController,
    destination: Destination,
    textModifier: Modifier = Modifier,
    rowModifier: Modifier = Modifier
) {
    Row(
        modifier = rowModifier
            .clickable {
                viewModel.onEvent(event)
                navController.navigate(destination.route)
            }
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = textModifier
        )
    }
}

/* BackButton
 * Button that shows "← <name of previous page>".
 * Hugs contents with a Min height and width of 44dp.
 *
 * Knows uses:
 *  - ProfileScreen
 *  - GameSettingsScreen
 *  - UserSelectScreen
 *  - UserDetailScreen
 *
 * @param[currentPageLabel] Displays current page title
 * @param[destination] Destination page on click
 * @param[navController] App nav controller
 */
@Composable
fun BackButton(
    currentPageLabel: String,
    destination: Destination,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val label = "← $currentPageLabel"

    Row(
        modifier = modifier
            .widthIn(min = 44.dp)
            .heightIn(min = 44.dp)
            .clickable { navController.navigate(destination.route) }
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

/* PrimaryButton
 * Used for main action on the page.
 * Filled bg, fills container with, and min height of 44dp.
 *
 * Knows uses:
 *  - Game settings screen
 *
 * @param[label] Button text label
 * @param[onClickAction] Action performed on click
 */
@Composable
fun PrimaryButton(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
        ),
        border = BorderStroke(1.dp, SolidColor(MaterialTheme.colorScheme.secondary)),
        modifier = modifier
            .heightIn(44.dp)
            .widthIn(44.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondary)
    }
}

/* SecondaryButton
 * Used for non-primary actions on the page.
 * Uses OutlinedButton with custom colours + min height 44dp.
 * https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#OutlinedButton(kotlin.Function0,androidx.compose.ui.Modifier,kotlin.Boolean,androidx.compose.ui.graphics.Shape,androidx.compose.material3.ButtonColors,androidx.compose.material3.ButtonElevation,androidx.compose.foundation.BorderStroke,androidx.compose.foundation.layout.PaddingValues,androidx.compose.foundation.interaction.MutableInteractionSource,kotlin.Function1)
 *
 * Knows uses:
 *  -
 *
 * @param[label] Button text label
 * @param[onClickAction] Action performed on click
 */
@Composable
fun SecondaryButton(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        colors = buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        border = BorderStroke(1.dp, SolidColor(MaterialTheme.colorScheme.outline)),
        modifier = modifier
            .heightIn(44.dp)
            .widthIn(44.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
fun SecondaryButtonDisabled(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = false,
        colors = buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContentColor = MaterialTheme.colorScheme.outline,
            disabledContainerColor = MaterialTheme.colorScheme.primary,
        ),
        border = BorderStroke(1.dp, SolidColor(MaterialTheme.colorScheme.outline)),
        modifier = modifier
            .heightIn(44.dp)
            .widthIn(44.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

/* GameMenuButton
 * Used for actions in the game screen page.
 * Uses TextButton with custom colours + min height 44dp.
 * https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#TextButton(kotlin.Function0,androidx.compose.ui.Modifier,kotlin.Boolean,androidx.compose.ui.graphics.Shape,androidx.compose.material3.ButtonColors,androidx.compose.material3.ButtonElevation,androidx.compose.foundation.BorderStroke,androidx.compose.foundation.layout.PaddingValues,androidx.compose.foundation.interaction.MutableInteractionSource,kotlin.Function1)
 *
 * @param[label] Button text label
 * @param[onClick] Action performed on click
 * @param[enabled] state of button
 * @param[modifier] TextButton modifier
 */
@Composable
fun GameMenuButton(
    modifier: Modifier = Modifier,
    label: String = "button",
    enabled: Boolean = true,
    onClick: () -> Unit = { println("Text button clicked.") }
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
        colors = buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.16f)
        ),
        modifier = modifier
            .heightIn(44.dp)
            .widthIn(44.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,)
    }
}

/* GameMenuButtonGroup
 * Group of buttons to display in all orientations in game screen.
 * https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#TextButton(kotlin.Function0,androidx.compose.ui.Modifier,kotlin.Boolean,androidx.compose.ui.graphics.Shape,androidx.compose.material3.ButtonColors,androidx.compose.material3.ButtonElevation,androidx.compose.foundation.BorderStroke,androidx.compose.foundation.layout.PaddingValues,androidx.compose.foundation.interaction.MutableInteractionSource,kotlin.Function1)
 *
 * @param[enableUndo] whether undo is enabled or not
 * @param[modifier] Row modifier
 */
@Composable
fun GameMenuButtonGroup (modifier: Modifier = Modifier, enableUndo: Boolean = true) {
    Row (
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        GameMenuButton(
            label = stringResource(id = copy.game_actions_pause),
//            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            // TODO: Add action here for pause
        }

        GameMenuButton(
            label = stringResource(id = copy.game_actions_undo),
//            modifier = Modifier.weight(1f).fillMaxWidth(),
            enabled = enableUndo
        ) {
            // TODO: Add action here for undo
        }

        GameMenuButton(
            label = stringResource(id = copy.game_actions_restart),
//            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            // TODO: Add action here for restart
        }

        GameMenuButton(
            label = stringResource(id = copy.game_actions_exit),
//            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            // TODO: Add action here for exit
        }
    }
}