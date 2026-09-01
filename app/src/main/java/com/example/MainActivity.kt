package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.components.DailyMissionRewardDialog
import com.example.ui.components.ParentGateDialog
import com.example.ui.kids.BadgeShowcaseScreen
import com.example.ui.kids.GameHubScreen
import com.example.ui.kids.KidsHomeScreen
import com.example.ui.kids.games.MathGalaxyScreen
import com.example.ui.kids.games.MemoryMatchScreen
import com.example.ui.kids.games.ScienceTriviaScreen
import com.example.ui.kids.games.ShapeKingdomScreen
import com.example.ui.kids.games.WordExplorerScreen
import com.example.ui.parent.ManageProfilesScreen
import com.example.ui.parent.ParentDashboardScreen
import com.example.ui.parent.ParentSettingsScreen
import com.example.ui.theme.WonderKidsTheme
import com.example.ui.viewmodel.KidsViewModel

object AppRoutes {
    const val KIDS_HOME = "kids_home"
    const val GAME_HUB = "game_hub"
    const val GAME_MATH = "game_math"
    const val GAME_WORD = "game_word"
    const val GAME_MEMORY = "game_memory"
    const val GAME_SCIENCE = "game_science"
    const val GAME_SHAPES = "game_shapes"
    const val BADGES = "badges"
    const val PARENT_DASHBOARD = "parent_dashboard"
    const val MANAGE_PROFILES = "manage_profiles"
    const val PARENT_SETTINGS = "parent_settings"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WonderKidsApp()
        }
    }
}

@Composable
fun WonderKidsApp(
    viewModel: KidsViewModel = viewModel(factory = KidsViewModel.Factory)
) {
    val navController = rememberNavController()

    val allChildren by viewModel.allChildren.collectAsStateWithLifecycle()
    val activeChild by viewModel.activeChild.collectAsStateWithLifecycle()
    val sessions by viewModel.recentSessions.collectAsStateWithLifecycle()
    val subjectMasteryList by viewModel.subjectMasteryList.collectAsStateWithLifecycle()
    val badges by viewModel.badges.collectAsStateWithLifecycle()
    val dailyMissions by viewModel.dailyMissions.collectAsStateWithLifecycle()
    val rewardCelebrationMission by viewModel.rewardCelebrationMission.collectAsStateWithLifecycle()
    val parentSettings by viewModel.parentSettings.collectAsStateWithLifecycle()

    var showParentGate by remember { mutableStateOf(false) }

    WonderKidsTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = AppRoutes.KIDS_HOME
            ) {
                // 1. Kids Home Screen (Discovery Hub)
                composable(AppRoutes.KIDS_HOME) {
                    KidsHomeScreen(
                        activeChild = activeChild,
                        allChildren = allChildren,
                        badges = badges,
                        dailyMissions = dailyMissions,
                        onSelectChild = { childId -> viewModel.selectChild(childId) },
                        onNavigateToGame = { route -> navController.navigate(route) },
                        onNavigateToGameHub = { navController.navigate(AppRoutes.GAME_HUB) },
                        onNavigateToBadges = { navController.navigate(AppRoutes.BADGES) },
                        onClaimMissionReward = { mission -> viewModel.claimMissionReward(mission) },
                        onOpenParentZone = { showParentGate = true }
                    )
                }

                // 2. Dedicated Game Hub Screen (Interactive Mini-Games & Skills Lab)
                composable(AppRoutes.GAME_HUB) {
                    GameHubScreen(
                        activeChild = activeChild,
                        allChildren = allChildren,
                        sessions = sessions,
                        subjectMasteryList = subjectMasteryList,
                        badges = badges,
                        dailyMissions = dailyMissions,
                        onBack = { navController.popBackStack() },
                        onNavigateToGame = { route -> navController.navigate(route) },
                        onNavigateToBadges = { navController.navigate(AppRoutes.BADGES) },
                        onOpenParentZone = { showParentGate = true },
                        onSelectChild = { childId -> viewModel.selectChild(childId) }
                    )
                }

                // 2. Math Galaxy Game
                composable(AppRoutes.GAME_MATH) {
                    MathGalaxyScreen(
                        child = activeChild,
                        onBack = { navController.popBackStack() },
                        onGameFinished = { category, title, score, total, correct, duration, stars ->
                            viewModel.recordGameSession(category, title, score, total, correct, duration, stars)
                        }
                    )
                }

                // 3. Word Explorer Game
                composable(AppRoutes.GAME_WORD) {
                    WordExplorerScreen(
                        child = activeChild,
                        onBack = { navController.popBackStack() },
                        onGameFinished = { category, title, score, total, correct, duration, stars ->
                            viewModel.recordGameSession(category, title, score, total, correct, duration, stars)
                        }
                    )
                }

                // 4. Memory Match Game
                composable(AppRoutes.GAME_MEMORY) {
                    MemoryMatchScreen(
                        child = activeChild,
                        onBack = { navController.popBackStack() },
                        onGameFinished = { category, title, score, total, correct, duration, stars ->
                            viewModel.recordGameSession(category, title, score, total, correct, duration, stars)
                        }
                    )
                }

                // 5. Science Trivia Game
                composable(AppRoutes.GAME_SCIENCE) {
                    ScienceTriviaScreen(
                        child = activeChild,
                        onBack = { navController.popBackStack() },
                        onGameFinished = { category, title, score, total, correct, duration, stars ->
                            viewModel.recordGameSession(category, title, score, total, correct, duration, stars)
                        }
                    )
                }

                // 6. Shape & Color Game
                composable(AppRoutes.GAME_SHAPES) {
                    ShapeKingdomScreen(
                        child = activeChild,
                        onBack = { navController.popBackStack() },
                        onGameFinished = { category, title, score, total, correct, duration, stars ->
                            viewModel.recordGameSession(category, title, score, total, correct, duration, stars)
                        }
                    )
                }

                // 7. Trophy & Badge Showcase
                composable(AppRoutes.BADGES) {
                    BadgeShowcaseScreen(
                        child = activeChild,
                        badges = badges,
                        onBack = { navController.popBackStack() }
                    )
                }

                // 8. Parent Dashboard
                composable(AppRoutes.PARENT_DASHBOARD) {
                    ParentDashboardScreen(
                        activeChild = activeChild,
                        allChildren = allChildren,
                        sessions = sessions,
                        subjectMasteryList = subjectMasteryList,
                        parentSettings = parentSettings,
                        onSelectChild = { childId -> viewModel.selectChild(childId) },
                        onNavigateToProfiles = { navController.navigate(AppRoutes.MANAGE_PROFILES) },
                        onNavigateToSettings = { navController.navigate(AppRoutes.PARENT_SETTINGS) },
                        onExitParentZone = { navController.navigate(AppRoutes.KIDS_HOME) { popUpTo(AppRoutes.KIDS_HOME) { inclusive = true } } }
                    )
                }

                // 9. Manage Child Profiles
                composable(AppRoutes.MANAGE_PROFILES) {
                    ManageProfilesScreen(
                        children = allChildren,
                        activeChildId = activeChild?.id ?: 1L,
                        onSelectChild = { childId -> viewModel.selectChild(childId) },
                        onAddChild = { name, age, grade, avatar, goal ->
                            viewModel.addChild(name, age, grade, avatar, goal)
                        },
                        onUpdateChild = { child -> viewModel.updateChild(child) },
                        onDeleteChild = { childId -> viewModel.deleteChild(childId) },
                        onBack = { navController.popBackStack() }
                    )
                }

                // 10. Parent Account Settings & Controls
                composable(AppRoutes.PARENT_SETTINGS) {
                    ParentSettingsScreen(
                        settings = parentSettings,
                        onUpdateSettings = { newSettings -> viewModel.updateParentSettings(newSettings) },
                        onResetScreenTime = { viewModel.resetTodayScreenTime() },
                        onResetAllProgress = { viewModel.resetAllProgress() },
                        onBack = { navController.popBackStack() }
                    )
                }
            }

            // Secure Adult Gate Modal
            if (showParentGate) {
                ParentGateDialog(
                    correctPin = parentSettings.pinCode,
                    onSuccess = {
                        showParentGate = false
                        navController.navigate(AppRoutes.PARENT_DASHBOARD)
                    },
                    onDismiss = { showParentGate = false }
                )
            }

            // Daily Mission Badge Reward Celebration Modal
            rewardCelebrationMission?.let { mission ->
                DailyMissionRewardDialog(
                    mission = mission,
                    onDismiss = { viewModel.dismissRewardCelebration() },
                    onViewBadges = {
                        viewModel.dismissRewardCelebration()
                        navController.navigate(AppRoutes.BADGES)
                    }
                )
            }
        }
    }
}
