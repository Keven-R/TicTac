package com.cnc.tictac.ui.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.cnc.tictac.Destination
import com.cnc.tictac.R
import com.cnc.tictac.viewmodel.MENU
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
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val label = "← $currentPageLabel"

    Row(
        modifier = modifier
            .widthIn(min = 44.dp)
            .heightIn(min = 44.dp)
            .clickable { navController.popBackStack() }
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
    viewModel: TicTacViewModel, // Needed to process onClick
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

@Composable
fun PrimaryButton(
    label: String,
    navController: NavHostController,
    viewModel: TicTacViewModel,
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

@Composable
fun PrimaryButton(
    label: String,
    navController: NavHostController,
    destination: Destination,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {navController.navigate(destination.route)},
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


@Composable
fun PrimaryButton(
    label: String,
    navController: NavHostController,
    destination: Destination,
    viewModel: TicTacViewModel,
    event: TicTacEvent,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {
            viewModel.onEvent(event)
            navController.navigate(destination.route)
                  },
        colors = buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
        ),
        border = BorderStroke(1.dp, SolidColor(MaterialTheme.colorScheme.secondary)),
        modifier = modifier.heightIn(44.dp).widthIn(44.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondary)
    }
}

@Composable
fun PrimaryButton(
    label: String,
    navController: NavHostController,
    viewModel: TicTacViewModel,
    event: TicTacEvent,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {
            viewModel.onEvent(event)
            navController.popBackStack()
        },
        colors = buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
        ),
        border = BorderStroke(1.dp, SolidColor(MaterialTheme.colorScheme.secondary)),
        modifier = modifier.heightIn(44.dp).widthIn(44.dp)
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
    viewModel: TicTacViewModel, // Needed to process onClick
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
    viewModel: TicTacViewModel, // Needed to process onClick
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = false,
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
            color = MaterialTheme.colorScheme.outline)
    }
}

//@Composable
//fun SecondaryButtonDisabled(
//    label: String,
//    modifier: Modifier = Modifier,
//    onClick: () -> Unit
//) {
//    OutlinedButton(
//        onClick = onClick,
//        enabled = false,
//        colors = buttonColors(
//            containerColor = MaterialTheme.colorScheme.primary,
//            contentColor = MaterialTheme.colorScheme.onPrimary,
//            disabledContentColor = MaterialTheme.colorScheme.outline,
//            disabledContainerColor = MaterialTheme.colorScheme.primary,
//        ),
//        border = BorderStroke(1.dp, SolidColor(MaterialTheme.colorScheme.outline)),
//        modifier = modifier
//            .heightIn(44.dp)
//            .widthIn(44.dp)
//    ) {
//        Text(
//            text = label,
//            style = MaterialTheme.typography.labelSmall,
//            color = MaterialTheme.colorScheme.outline
//        )
//    }
//}

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
fun GameMenuButtonGroup (
    viewModel: TicTacViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    enableUndo: Boolean = viewModel.undoAvailable
) {
    Row (
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        GameMenuButton(
            label = stringResource(id = copy.game_actions_pause),
    //            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            viewModel.onEvent(TicTacEvent.TimerStop)
            viewModel.gameUIState = MENU.PAUSE
            navController.navigate(Destination.GameMenuScreen.route)
        }

        GameMenuButton(
            label = stringResource(id = copy.game_actions_undo),
    //            modifier = Modifier.weight(1f).fillMaxWidth(),
            enabled = enableUndo
        ) {
            viewModel.onEvent(TicTacEvent.TimerStop)
            viewModel.gameUIState = MENU.UNDO
            navController.navigate(Destination.GameMenuScreen.route)
        }

        GameMenuButton(
            label = stringResource(id = copy.game_actions_restart),
    //            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            viewModel.onEvent(TicTacEvent.TimerStop)
            viewModel.gameUIState = MENU.RESTART
            navController.navigate(Destination.GameMenuScreen.route)
        }

        GameMenuButton(
            label = stringResource(id = copy.game_actions_exit),
    //            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            viewModel.onEvent(TicTacEvent.TimerStop)
            viewModel.gameUIState = MENU.EXIT
            navController.navigate(Destination.GameMenuScreen.route)
        }
    }
}

@Composable
fun GameMenuButtonGroup(
    modifier: Modifier = Modifier,
    menu: MENU,
    stackVertically: Boolean = true,
    navController: NavHostController,
    viewModel: TicTacViewModel
) {

    val buttonGroupContent = getGameMenuLabelAndEvents(menu,navController,viewModel)
    val (confirmLabelId, confirmEvent) = buttonGroupContent[0]
    val (dismissLabelId, dismissEvent) = buttonGroupContent[1]

    if (stackVertically) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(space = 32.dp, alignment = Alignment.Bottom),
            horizontalAlignment = Alignment.Start
        ) {
            GameMenuButton(
                onClick = confirmEvent,
                label = stringResource(id = confirmLabelId),
                modifier = Modifier,
                )
            GameMenuButton(
                onClick = dismissEvent,
                label = stringResource(id = dismissLabelId),
                modifier = Modifier,
            )
        }
    } else {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            GameMenuButton(
                onClick = if (menu == MENU.PAUSE) dismissEvent else confirmEvent,
                label = stringResource(id = if (menu == MENU.PAUSE) dismissLabelId else confirmLabelId),
            )
            GameMenuButton(
                onClick = if (menu == MENU.PAUSE) confirmEvent else dismissEvent,
                label = stringResource(id = if (menu == MENU.PAUSE) confirmLabelId else dismissLabelId),
            )
        }

    }
}

// Returns content required for game menu buttons as list of pairs
// List[i] is content for 1 button (in a Pair)
// Pair(a,b) A = label, B = onclick action
fun getGameMenuLabelAndEvents (menu: MENU, navController: NavHostController, viewModel: TicTacViewModel) : Array<Pair<Int, () -> Unit >> {
    when (menu) {
        MENU.PAUSE -> {
            return arrayOf(
                Pair(R.string.game_menu_pause_confirm) {
                    viewModel.onEvent(TicTacEvent.TimerStart)
                    navController.popBackStack()
                },
                Pair(R.string.game_menu_pause_dismiss) {
                    viewModel.onEvent(TicTacEvent.TimerStart)
                    navController.popBackStack()
                }
            )
        }
        MENU.RESTART -> {
            return arrayOf(
                Pair(R.string.game_menu_restart_confirm) {
                    viewModel.onEvent(TicTacEvent.Restart)
                    viewModel.onEvent(TicTacEvent.TimerStart)
                    navController.popBackStack()
                },
                Pair(R.string.game_menu_restart_dismiss) {
                    viewModel.onEvent(TicTacEvent.TimerStart)
                    navController.popBackStack()
                }
            )
        }
        MENU.EXIT -> {
            return arrayOf(
                Pair(R.string.game_menu_exit_confirm) {
                    viewModel.onEvent(TicTacEvent.Exit)
                    navController.navigate(Destination.HomeScreen.route)
                },
                Pair(R.string.game_menu_exit_dismiss) {
                    viewModel.onEvent(TicTacEvent.TimerStart)
                    navController.popBackStack()
                }
            )
        }
        else -> {
            // IGNORE, just needed something here lol
            return arrayOf(
                Pair(R.string.user_name_placeholder) { Log.d("Buttons", "Game Menu Button Error") },
                Pair(R.string.user_name_placeholder) { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun GameMenuButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: String,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier ,
        enabled = true,
        colors = buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}