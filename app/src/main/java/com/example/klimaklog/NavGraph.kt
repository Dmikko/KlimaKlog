import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.klimaklog.HistoryScreen
import com.example.klimaklog.ResultScreen
import com.example.klimaklog.SearchScreen
import com.example.klimaklog.quiz.QuizScreen
import com.example.klimaklog.quiz.LetQuizScreen
import com.example.klimaklog.quiz.MellemQuizScreen
import com.example.klimaklog.quiz.PersonalChallengesScreen
import com.example.klimaklog.quiz.QuizOverviewScreen
import com.example.klimaklog.quiz.SvaerQuizScreen

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
        composable(route = "quiz/overview") {
            QuizOverviewScreen(navController)
        }
//niveauer:
        composable("quiz/personal") { PersonalChallengesScreen(navController) }
        composable("quiz/let") { LetQuizScreen(navController) }
        composable("quiz/mellem") { MellemQuizScreen(navController) }
        composable("quiz/svaer") { SvaerQuizScreen(navController) }

        composable(
            "result/{query}",
            arguments = listOf(navArgument("query") { type = NavType.StringType })
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            ResultScreen(navController, query)
        }
    }
}

