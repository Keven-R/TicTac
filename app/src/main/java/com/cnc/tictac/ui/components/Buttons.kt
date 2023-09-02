package com.cnc.tictac.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
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
    onClickEvent: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickEvent }
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
fun DisplayButton(
    label: String,
    navController: NavHostController,
    destination: Destination
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate(destination.route) }
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onPrimary,
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
    navController: NavHostController
) {
    val label = "← $currentPageLabel"

    Row(
        modifier = Modifier
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
 *  -
 *
 * @param[label] Button text label
 * @param[onClickAction] Action performed on click
 */
@Composable
fun PrimaryButton(
    label: String,
    onClickEvent: () -> Unit
) {
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
        modifier = modifier.heightIn(44.dp).widthIn(44.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary)
    }
}