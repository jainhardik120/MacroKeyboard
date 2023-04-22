package com.jainhardik120.macrokeyboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jainhardik120.macrokeyboard.ui.presentation.root.BottomBarScreen
import com.jainhardik120.macrokeyboard.ui.presentation.root.HomeNavGraph
import com.jainhardik120.macrokeyboard.ui.theme.MacroKeyboardTheme
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
                    val screens = listOf(
                        BottomBarScreen.Home,
                        BottomBarScreen.Settings
                    )
                    val navController: NavHostController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
                    Scaffold(
//                        topBar = {
//                            TopAppBar(title = {
//                                Text(text = "Macro Keys")
//                            })
//                        },
                        bottomBar = {

                            NavigationBar {
                                screens.forEachIndexed { index, screen ->
                                    NavigationBarItem(
                                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                                        label = { Text(screen.title) },
                                        selected = currentDestination?.hierarchy?.any {
                                            it.route == screen.route
                                        } == true,
                                        onClick = {
                                            navController.navigate(screen.route){
                                                popUpTo(navController.graph.findStartDestination().id)
                                                launchSingleTop = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    ) {
                        HomeNavGraph(navController = navController, paddingValues = it)
                    }
                }
            }
        }
    }
}
