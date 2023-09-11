package com.cnc.tictac.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cnc.tictac.Destination
import com.cnc.tictac.viewmodel.TicTacEvent
import com.cnc.tictac.viewmodel.TicTacViewModel

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
        modifier = modifier.heightIn(44.dp).widthIn(44.dp)
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
    navController: NavHostController,
    destination: Destination,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = {navController.navigate(destination.route)},
        colors = buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        border = BorderStroke(1.dp, SolidColor(MaterialTheme.colorScheme.outline)),
        modifier = modifier.heightIn(44.dp).widthIn(44.dp)
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
        modifier = modifier.heightIn(44.dp).widthIn(44.dp)
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
        modifier = modifier.heightIn(44.dp).widthIn(44.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline)
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
        ),
        border = BorderStroke(1.dp, SolidColor(MaterialTheme.colorScheme.outline)),
        modifier = modifier.heightIn(44.dp).widthIn(44.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline)
    }
}