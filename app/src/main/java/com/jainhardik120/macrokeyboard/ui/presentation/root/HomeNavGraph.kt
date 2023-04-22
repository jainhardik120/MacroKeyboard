package com.jainhardik120.macrokeyboard.ui.presentation.root

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jainhardik120.macrokeyboard.ui.presentation.home.HomeScreen
import com.jainhardik120.macrokeyboard.ui.presentation.settings.SettingsScreen

@Composable
fun HomeNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Home.route) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                HomeScreen()
            }
        }
        composable(route = BottomBarScreen.Settings.route) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                SettingsScreen()
            }
        }
    }


}