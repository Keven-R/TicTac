package com.cnc.tictac.ui.screens

sealed class Screen(val route: String){
    object MenuScreen: Screen("menu_screen")
}
