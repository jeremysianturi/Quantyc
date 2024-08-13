package com.example.quantyc.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quantyc.data.local.entities.UserEntity
import com.example.quantyc.ui.theme.Quantyc
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

sealed class Screen(val route: String) {

    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Main : Screen("main/{user}") {
        fun createRoute(user: String) = "main/${user}"

        val arguments: List<NamedNavArgument> = listOf(
            navArgument("user") {
                type = NavType.StringType
            }
        )
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route) {

        composable(route = Screen.Login.route) {
            LoginScreen(
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) },
                onLoginSuccess = { user ->
                    val userJson = Json.encodeToString(user)
                    navController.navigate(Screen.Main.createRoute(userJson))
                }
            )
        }

        composable(route = Screen.SignUp.route) {
            SignUpScreen(onNavigateToLogin = { navController.popBackStack() })
        }

        composable(
            route = Screen.Main.route,
            arguments = Screen.Main.arguments
        ) { backStackEntry ->
            val userJson = backStackEntry.arguments?.getString("user") ?: ""
            val user = Json.decodeFromString<UserEntity>(userJson)
            MainScreen(user = user, onLogout = { navController.navigate(Screen.Login.route)}, context = LocalContext.current)
        }
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Quantyc {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        Log.d("MainActivity", "got here")
//        deleteCachedData()
//    }
//
//    private fun deleteCachedData() {
//        val viewModel: PhotoViewModel by viewModels()
//        viewModel.deleteAllCachedData()
//    }
}