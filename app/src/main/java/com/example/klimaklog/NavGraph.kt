import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.klimaklog.quiz.QuizScreen

@Composable
fun KlimaNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "search"
    ) {
        composable("search") { SearchScreen(navController) }
        composable("history") { HistoryScreen(navController) }
        composable("quiz") { QuizScreen(navController) }
        composable(
            "result/{query}",
            arguments = listOf(navArgument("query") { type = NavType.StringType })
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            ResultScreen(navController, query)
        }
    }
}

