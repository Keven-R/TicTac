package com.cnc.tictac.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/* Radio
 * Used to enable a user to select 1 out of up to 3 options.
 *
 * Knows uses:
 *  - Game settings screen
 *
 * @param[title] Describes what the user is choosing
 * @param[numOptions] Number of options to choose from, 2-3
 * @param[labels] Labels for each option item
 * @param[selected] Index of the current selected item
 */
@Composable
fun Radio(
    title: String = "Select one: ",
    numOptions: Int = 2,
    labels: Array<String> = arrayOf("1", "2"),
    selectedIndex: Int = 0,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    // CONTAINER: for both title and radio choices
    Column(
        modifier = modifier,
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

            for (currIndex in labels.indices) {
                if (currIndex == selectedIndex) {
                    PrimaryButton(
                        label = labels[currIndex],
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        // TODO: what does clicking on button do????
                    }
                } else {
                    SecondaryButton(
                        label = labels[currIndex],
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)) {
                        // TODO: what does clicking on button do????
                    }
                }
            }
        }
    }
}