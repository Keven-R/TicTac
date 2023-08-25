package com.cnc.tictac

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cnc.tictac.ui.screens.MenuScreen
import com.cnc.tictac.ui.screens.Screen
import com.cnc.tictac.ui.theme.TicTacTheme
import com.cnc.tictac.viewmodel.TicTacViewModel

private const val TAG = "MainActivity"


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

                NavHost(
                    navController = navController,
                    startDestination = Screen.MenuScreen.route
                ){
                    composable(route = Screen.MenuScreen.route){ MenuScreen(viewModel)}
                }
            }
        }
    }
}