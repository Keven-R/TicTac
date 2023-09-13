package com.cnc.tictac.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cnc.tictac.viewmodel.MENU
import com.cnc.tictac.ui.components.GameMenuButtonGroup
import com.cnc.tictac.ui.components.GameMenuText
import com.cnc.tictac.ui.system.getDeviceInfo
import com.cnc.tictac.viewmodel.TicTacEvent
import com.cnc.tictac.viewmodel.TicTacViewModel

@Composable
fun GameMenuScreen(navController: NavHostController, viewModel: TicTacViewModel) {
    // Determine device UI layout.
    val deviceInfo = getDeviceInfo()
    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            DisplayWideGameMenuScreen(navController, viewModel)
        } else -> {
            DisplayDefaultGameMenuScreen(navController, viewModel)
        }
    }
}

@Composable
fun DisplayDefaultGameMenuScreen(navController: NavHostController, viewModel: TicTacViewModel) {
    // CONTAINER: Set bg colour
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.primary)) {
        // CONTAINER: All content on screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ELEMENT: Menu title & description
            GameMenuText(
                menu = viewModel.gameUIState,
                modifier = Modifier.widthIn(200.dp).padding(top = 32.dp)
            )

            // ELEMENT: Menu buttons
            GameMenuButtonGroup(
                menu = viewModel.gameUIState,
                modifier = Modifier.weight(1f).fillMaxHeight().fillMaxWidth().padding(bottom = 160.dp),
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun DisplayWideGameMenuScreen(navController: NavHostController, viewModel: TicTacViewModel) {
    // CONTAINER: Set bg colour
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.primary)) {
        // CONTAINER: All content on screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // ELEMENT: Menu title & description
            GameMenuText(
                menu = viewModel.gameUIState,
                modifier = Modifier.widthIn(200.dp).padding(top = 32.dp, start = 12.dp, end = 12.dp))

            // ELEMENT: Menu buttons
            GameMenuButtonGroup(
                menu = viewModel.gameUIState,
                modifier = Modifier.fillMaxWidth(),
                stackVertically = false,
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}