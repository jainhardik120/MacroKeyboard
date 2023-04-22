package com.jainhardik120.macrokeyboard.util

sealed class Screen(val route: String){
    object HomeScreen : Screen("home_screen")
    object SettingsScreen : Screen("settings_screen")
    object EditScreen : Screen("edit_screen")

    fun withArgs(vararg args: String):String{
        return buildString {
            append(route)
            args.forEach { arg->
                append("/$arg")
            }
        }
    }
}
