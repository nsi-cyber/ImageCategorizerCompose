package com.nsicyber.imagecategorizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nsicyber.imagecategorizer.models.ClassifiedFolderList
import com.nsicyber.imagecategorizer.models.ClassifiedFolderModel
import com.nsicyber.imagecategorizer.screens.ClassifiedFolderDetailScreen
import com.nsicyber.imagecategorizer.screens.ClassifiedFoldersScreen
import com.nsicyber.imagecategorizer.screens.HomeScreen
import com.nsicyber.imagecategorizer.screens.SuccessScreen
import com.nsicyber.imagecategorizer.ui.theme.ImageCategorizerTheme
import com.nsicyber.imagecategorizer.utils.fromJson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageCategorizerTheme {
                val navController = rememberNavController()

                NavHost(
                    modifier = Modifier.background(color = Color(0xFF27282E)),
                    navController = navController,
                    startDestination = "home_screen"
                ) {
                    val resultRoute = "folder_screen?result={result}"

                    composable("home_screen") {
                        BackHandler(true) {
                            if (navController.previousBackStackEntry?.destination?.route != "success_screen") {
                                navController.popBackStack()
                            }
                        }
                        HomeScreen(navHostController = navController)
                    }

                    composable(resultRoute, arguments = listOf(navArgument("result") { type = NavType.StringType })) {
                        val result = remember { it.arguments?.getString("result") }
                        ClassifiedFoldersScreen(navHostController = navController, result?.fromJson(ClassifiedFolderList::class.java))
                    }

                    composable("folder_screen_detail?result={result}", arguments = listOf(navArgument("result") { type = NavType.StringType })) {
                        val result = remember { it.arguments?.getString("result") }
                        ClassifiedFolderDetailScreen(navHostController = navController, result?.fromJson(ClassifiedFolderModel::class.java))
                    }

                    composable("success_screen") {
                        BackHandler(true) { navController.navigate("home_screen") }
                        SuccessScreen(navController = navController)
                    }
                }
            }
        }
    }
}
