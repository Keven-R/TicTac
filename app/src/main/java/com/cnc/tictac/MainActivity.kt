package com.cnc.tictac

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cnc.tictac.ui.screens.*
import com.cnc.tictac.ui.theme.TicTacTheme
//import com.cnc.tictac.viewmodel.TicTacViewModel

private const val TAG = "MainActivity"

// All SCREENS found here
sealed class Destination(val route: String){
    object HomeScreen: Destination("home")
    object GameScreen: Destination("game")
    object GameSettingsScreen: Destination("settings")
    object UserDetailScreen: Destination("edit_info")
    object UserSelectScreen: Destination("player_select")
    object UserStatsScreen: Destination("stats")
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
                val navController = rememberNavController()
                // val viewModel = viewModel<TicTacViewModel>()

                NavigationAppHost(navController = navController)
            }
        }
    }
}

@Composable
fun NavigationAppHost (navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Destination.HomeScreen.route
    ){
        composable(route = Destination.HomeScreen.route){ HomeScreen() }
        composable(route = Destination.GameScreen.route){ GameScreen() }
        composable(route = Destination.GameSettingsScreen.route){ GameSetingsScreen() }
        composable(route = Destination.UserDetailScreen.route){ UserDetailScreen() }
        composable(route = Destination.UserSelectScreen.route){ UserSelectScreen() }
        composable(route = Destination.UserStatsScreen.route){ UserStatsScreen() }
    }
}