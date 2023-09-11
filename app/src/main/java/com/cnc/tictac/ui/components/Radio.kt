package com.cnc.tictac.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cnc.tictac.viewmodel.TicTacViewModel

/* Radio
 * Used to enable a user to select 1 out of up to 3 options.
 *
 * Knows uses:
 *  - Game settings screen
 *
 * @param[title] Describes what the user is choosing
 * @param[numOptions] Number of options to choose from, 2-3
 * @param[labels] Labels for each option item
 * @param[selectedIndex] Index of the current selected item
 * @param[isDisabled] Array with length == numOptions. Each item = true if disabled, false if enabled.
 */
@Composable
fun Radio(
    modifier: Modifier = Modifier,
    title: String = "Select one: ",
    viewModel: TicTacViewModel, // Needed to process onClick
    onClick: () -> Unit,
    labels: Array<String> = arrayOf("1", "2"),
    isDisabled: Array<Boolean> = arrayOf(false, false, false),
    selectedIndex: Int = 0
) {
    // CONTAINER: for both title and radio choices
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TitleMedium(title)

        // CONTAINER: for all radio choices, 2-3
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.CenterHorizontally
            )) {

            // Style radio button depending on if it's selected, disabled, or default
            for ((ind, currIndex) in labels.indices.withIndex()) {
                if (currIndex == selectedIndex) {
                    PrimaryButton(
                        label = labels[currIndex],
                        viewModel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        onClick = onClick
                    )
                } else if (isDisabled[ind]) {
                    SecondaryButtonDisabled(
                        label = labels[currIndex],
                        viewModel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        onClick = onClick
                    )
                } else {
                    SecondaryButton(
                        label = labels[currIndex],
                        viewModel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        onClick = onClick
                    )
                }
            }
        }
    }
}