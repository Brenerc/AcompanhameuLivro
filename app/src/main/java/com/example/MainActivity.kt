package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.components.BottomNavBar
import com.example.ui.components.NavTab
import com.example.ui.screens.AddEditBookScreen
import com.example.ui.screens.BookDetailScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.BookViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                HappyReadingApp()
            }
        }
    }
}

@Composable
fun HappyReadingApp() {
    val navController = rememberNavController()
    val viewModel: BookViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentTab = when {
        currentRoute?.startsWith("search") == true -> NavTab.SEARCH
        currentRoute?.startsWith("profile") == true -> NavTab.PROFILE
        else -> NavTab.LIBRARY
    }

    val showBottomBar = currentRoute in listOf("library", "search", "profile")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    currentTab = currentTab,
                    onTabSelected = { tab ->
                        val targetRoute = when (tab) {
                            NavTab.LIBRARY -> "library"
                            NavTab.SEARCH -> "search"
                            NavTab.PROFILE -> "profile"
                        }
                        if (currentRoute != targetRoute) {
                            navController.navigate(targetRoute) {
                                popUpTo("library") {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "library",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("library") {
                LibraryScreen(
                    viewModel = viewModel,
                    onBookClick = { bookId ->
                        navController.navigate("book_detail/$bookId")
                    },
                    onAddBookClick = {
                        navController.navigate("add_edit_book?bookId=0")
                    },
                    onProfileClick = {
                        navController.navigate("profile")
                    }
                )
            }

            composable("search") {
                SearchScreen(
                    viewModel = viewModel,
                    onBookClick = { bookId ->
                        navController.navigate("book_detail/$bookId")
                    },
                    onProfileClick = {
                        navController.navigate("profile")
                    }
                )
            }

            composable("profile") {
                ProfileScreen(
                    viewModel = viewModel
                )
            }

            composable(
                route = "book_detail/{bookId}",
                arguments = listOf(navArgument("bookId") { type = NavType.LongType })
            ) { backStackEntry ->
                val bookId = backStackEntry.arguments?.getLong("bookId") ?: 0L
                BookDetailScreen(
                    bookId = bookId,
                    viewModel = viewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onEditClick = { id ->
                        navController.navigate("add_edit_book?bookId=$id")
                    }
                )
            }

            composable(
                route = "add_edit_book?bookId={bookId}",
                arguments = listOf(
                    navArgument("bookId") {
                        type = NavType.LongType
                        defaultValue = 0L
                    }
                )
            ) { backStackEntry ->
                val bookId = backStackEntry.arguments?.getLong("bookId") ?: 0L
                AddEditBookScreen(
                    bookId = bookId,
                    viewModel = viewModel,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
