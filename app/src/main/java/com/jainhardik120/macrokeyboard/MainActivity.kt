package com.jainhardik120.macrokeyboard

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jainhardik120.macrokeyboard.ui.presentation.action.ActionEditScreen
import com.jainhardik120.macrokeyboard.ui.presentation.edit.EditButtonScreen
import com.jainhardik120.macrokeyboard.ui.presentation.home.HomeScreen
import com.jainhardik120.macrokeyboard.ui.presentation.settings.SettingsScreen
import com.jainhardik120.macrokeyboard.ui.theme.MacroKeyboardTheme
import com.jainhardik120.macrokeyboard.util.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MacroKeyboardTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.HomeScreen.route
                    ) {
                        composable(route = Screen.HomeScreen.route) {
                            HomeScreen(onNavigate = {
                                it.route?.let { it1 -> navController.navigate(it1) }
                            })
                        }
                        composable(route = Screen.SettingsScreen.route) {
                            SettingsScreen(onNavigate = {
                                navController.navigateUp()
                            })
                        }
                        composable(route = Screen.EditScreen.route + "/{screenId}?childId={childId}",
                            arguments = listOf(
                                navArgument("screenId") {
                                    type = NavType.StringType
                                    nullable = false
                                },
                                navArgument("childId") {
                                    type = NavType.StringType
                                    nullable = true
                                    defaultValue = "-1"
                                }
                            )
                        ) {
                            EditButtonScreen(onNavigate = {
                                Log.d("TAG", "onCreate: ${it.route}")
                                if(it.route==null){
                                    navController.navigateUp()
                                }else{
                                    navController.navigate(it.route)
                                }
                            })
                        }
                        composable(route = Screen.EditActionScreen.route + "/{childId}/{sno}",
                            arguments = listOf(
                                navArgument("childId") {
                                    type = NavType.StringType
                                    nullable = false
                                },
                                navArgument("sno") {
                                    type = NavType.StringType
                                    nullable = false
                                }
                            )) {
                                ActionEditScreen(onNavigate = {navController.navigateUp()})
                        }

                    }
                }
            }
        }
    }
}
