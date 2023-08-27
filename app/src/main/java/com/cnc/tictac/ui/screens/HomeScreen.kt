package com.cnc.tictac.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp


// COMPONENT imports
import com.cnc.tictac.ui.components.PrimaryButton

// RESOURCES import
import com.cnc.tictac.R.string as content

@Composable
fun HomeScreen() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // UI: App logo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, bottom = 64.dp)
            ) {
                Text(
                    text = stringResource(id = content.app_display_name),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            // UI: Home screen actions
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    
            ) {
                // TODO: Add destination (game settings) to onclick event
                PrimaryButton(stringResource(id = content.button_play_solo))
                // TODO: Add destination (player select) to onclick event
                PrimaryButton(stringResource(id = content.button_play_multi))
                // TODO: Add destination (player select) to onclick event
                PrimaryButton(stringResource(id = content.button_profile))
            }
        }
    }
}