package com.cnc.tictac.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.cnc.tictac.Destination

// TODO
/* PRIMARY BUTTON
 *
 * Knows uses:
 *  - HomeScreen: Game mode select
 *
 * @param[label] Button text label
 * @param[onClickAction] Action performed on cl
 */
@Composable
fun PrimaryButton(
    label: String,
    onClickEvent: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.clickable { onClickEvent },
            text = label,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
fun PrimaryButton(
    label: String,
    navController: NavHostController,
    destination: Destination
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.clickable { navController.navigate(destination.route) },
            text = label,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}