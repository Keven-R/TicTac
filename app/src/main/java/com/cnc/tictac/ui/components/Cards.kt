package com.cnc.tictac.ui.components

import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

// TODO
/* PROFILE CARD
 *
 * Knows uses:
 *  - HomeScreen: Navigates to current logged in user.
 *
 * @param[user]
 */
@Composable
fun ProfileCard (
    // STRING IS TEMP variable, NEED USER CLASS???
    user: String,
) {
    Card {
        Text(user)
    }
}