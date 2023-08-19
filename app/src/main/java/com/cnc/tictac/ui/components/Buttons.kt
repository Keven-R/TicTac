package com.cnc.tictac.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


// TODO
/* PRIMARY BUTTON
 *
 * Knows uses:
 *  - HomeScreen: Game mode select
 *
 * @param[label] Button text label
 * @param[onClick] function
 */
@Composable
fun PrimaryButton(
    label: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
    ) {
        Text(label)
    }
}