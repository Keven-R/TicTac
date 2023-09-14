package com.cnc.tictac

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cnc.tictac.ui.screens.GameMenuScreen
import com.cnc.tictac.ui.screens.GameScreen
import com.cnc.tictac.ui.screens.GameSettingsScreen
import com.cnc.tictac.ui.screens.HomeScreen
import com.cnc.tictac.ui.screens.ProfileScreen
import com.cnc.tictac.ui.screens.UserDetailScreen
import com.cnc.tictac.ui.screens.UserSelectScreen
import com.cnc.tictac.ui.screens.MultiplayerSettingsScreen
import com.cnc.tictac.ui.theme.TicTacTheme
import com.cnc.tictac.viewmodel.TicTacEvent
import com.cnc.tictac.viewmodel.TicTacViewModel

private const val TAG = "MainActivity"

// All SCREENS found here
sealed class Destination(val route: String){
    object HomeScreen: Destination("home")
    object GameScreen: Destination("game")
    object GameSettingsScreen: Destination("settings")
    object MultiplayerSettingsScreen: Destination("multi")
    object UserDetailScreen: Destination("edit_info")
    object UserSelectScreen: Destination("player_select")
    object ProfileScreen: Destination("profile")
    object GameMenuScreen: Destination("game_menu")
}

/**
 * Main activity for TicTac app
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "MainActivity Created")
        super.onCreate(savedInstanceState)
        setContent {
            TicTacTheme {
                val viewModel = viewModel<TicTacViewModel>()
                val navController = rememberNavController()
                NavigationAppHost(navController, viewModel)
            }
        }
    }
}

@Composable
fun NavigationAppHost (navController: NavHostController, viewModel: TicTacViewModel) {
    NavHost(
        navController = navController,
        startDestination = Destination.HomeScreen.route
    ){
        composable(route = Destination.HomeScreen.route){ HomeScreen(navController,viewModel) }
        composable(route = Destination.GameScreen.route){ BackHandler(true) {}; GameScreen(navController,viewModel) }
        composable(route = Destination.GameSettingsScreen.route){ GameSettingsScreen(navController,viewModel) }
        composable(route = Destination.MultiplayerSettingsScreen.route){ MultiplayerSettingsScreen(navController, viewModel) }
        composable(route = Destination.UserDetailScreen.route){ UserDetailScreen(navController,viewModel) }
        composable(route = Destination.UserSelectScreen.route){ UserSelectScreen(navController,viewModel) }
        composable(route = Destination.ProfileScreen.route){ ProfileScreen(navController,viewModel) }
        composable(route = Destination.GameMenuScreen.route){ BackHandler(true) {}; GameMenuScreen(navController,viewModel) }
    }
}