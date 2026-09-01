package com.example.ui.kids

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Badge
import com.example.data.model.ChildProfile
import com.example.data.model.DailyMission
import com.example.data.model.GameCategory
import com.example.ui.components.KidTopAppBar
import com.example.ui.theme.ParentNavy
import com.example.ui.theme.PlayfulBlue
import com.example.ui.theme.PlayfulCoral
import com.example.ui.theme.PlayfulGreen
import com.example.ui.theme.PlayfulIndigo
import com.example.ui.theme.PlayfulOrange
import com.example.ui.theme.PlayfulPink
import com.example.ui.theme.PlayfulPurple
import com.example.ui.theme.PlayfulSky
import com.example.ui.theme.PlayfulTeal
import com.example.ui.theme.PlayfulYellow
import com.example.ui.theme.PrimaryKids
import com.example.ui.theme.StarGold

data class GameCardItem(
    val category: GameCategory,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val emoji: String,
    val gradientColors: List<Color>,
    val difficultyLabel: String,
    val routeKey: String
)

@Composable
fun KidsHomeScreen(
    activeChild: ChildProfile?,
    allChildren: List<ChildProfile>,
    badges: List<Badge>,
    dailyMissions: List<DailyMission> = emptyList(),
    onSelectChild: (Long) -> Unit,
    onNavigateToGame: (String) -> Unit,
    onNavigateToGameHub: () -> Unit = {},
    onNavigateToBadges: () -> Unit,
    onClaimMissionReward: (DailyMission) -> Unit = {},
    onOpenParentZone: () -> Unit
) {
    val games = listOf(
        GameCardItem(
            category = GameCategory.MATH,
            title = "Math Galaxy",
            subtitle = "Blast off with fun counting & mental arithmetic!",
            icon = Icons.Default.RocketLaunch,
            emoji = "🚀",
            gradientColors = listOf(Color(0xFF386B3B), Color(0xFF53634F)),
            difficultyLabel = "All Ages",
            routeKey = "game_math"
        ),
        GameCardItem(
            category = GameCategory.SPELLING,
            title = "Word Explorer",
            subtitle = "Spelling jungle, phonics & magical vocabulary!",
            icon = Icons.Default.MenuBook,
            emoji = "📚",
            gradientColors = listOf(Color(0xFF3A7D84), Color(0xFF489098)),
            difficultyLabel = "Spelling",
            routeKey = "game_word"
        ),
        GameCardItem(
            category = GameCategory.MEMORY,
            title = "Memory Safari",
            subtitle = "Flip & match adorable animal friends!",
            icon = Icons.Default.Extension,
            emoji = "🧩",
            gradientColors = listOf(Color(0xFF73628A), Color(0xFF826E9C)),
            difficultyLabel = "Logic & Focus",
            routeKey = "game_memory"
        ),
        GameCardItem(
            category = GameCategory.SCIENCE,
            title = "Science & Nature",
            subtitle = "Explore our universe, animals & fascinating facts!",
            icon = Icons.Default.Science,
            emoji = "🔬",
            gradientColors = listOf(Color(0xFF5C7451), Color(0xFF6B8E4E)),
            difficultyLabel = "Curiosity",
            routeKey = "game_science"
        ),
        GameCardItem(
            category = GameCategory.SHAPES,
            title = "Color & Shapes",
            subtitle = "Mix magical colors & match geometric patterns!",
            icon = Icons.Default.Category,
            emoji = "🎨",
            gradientColors = listOf(Color(0xFFC85A38), Color(0xFFD47335)),
            difficultyLabel = "Creativity",
            routeKey = "game_shapes"
        )
    )

    val unlockedBadgesCount = badges.count { it.isUnlocked }
    val completedMissionsCount = dailyMissions.count { it.isCompleted }
    val totalMissionsCount = dailyMissions.size
    val allMissionsDone = totalMissionsCount > 0 && completedMissionsCount == totalMissionsCount

    Scaffold(
        topBar = {
            KidTopAppBar(
                activeChild = activeChild,
                allChildren = allChildren,
                onSelectChild = onSelectChild,
                onOpenBadges = onNavigateToBadges,
                onOpenParentZone = onOpenParentZone
            )
        },
        containerColor = Color(0xFFFBFCF8)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(6.dp))

                // Hero Banner Card with Generated Art
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFDCE5DB), RoundedCornerShape(24.dp))
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.hero_kids_learning),
                                contentDescription = "Kids Learning Adventures",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color.Transparent, Color(0xFF191C19).copy(alpha = 0.7f))
                                        )
                                    )
                            )
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(16.dp)
                            ) {
                                val name = activeChild?.name ?: "Explorer"
                                Text(
                                    text = "Ready to explore, $name? 🌟",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                )
                                Text(
                                    text = "Play fun learning games and earn shiny stars today!",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                )
                            }
                        }

                        // Daily Goal Progress bar inside Hero
                        if (activeChild != null) {
                            val goal = activeChild.dailyGoalMinutes
                            val done = activeChild.todayPlayedMinutes
                            val progress = if (goal > 0) (done.toFloat() / goal.toFloat()).coerceIn(0f, 1f) else 0f

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "🎯", fontSize = 16.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Today's Learning Goal",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = ParentNavy
                                            )
                                        )
                                    }
                                    Text(
                                        text = "$done / $goal mins",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (done >= goal) PlayfulGreen else PrimaryKids
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                LinearProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(10.dp)
                                        .clip(RoundedCornerShape(5.dp)),
                                    color = if (done >= goal) PlayfulGreen else PrimaryKids,
                                    trackColor = Color(0xFFEDF3EB)
                                )
                            }
                        }
                    }
                }
            }

            // Daily Missions Core Section
            if (dailyMissions.isNotEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFDCE5DB), RoundedCornerShape(22.dp))
                            .testTag("daily_missions_container")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            // Section Header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFEDF3EB)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "🎯", fontSize = 20.sp)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "Daily Missions",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Black,
                                                color = ParentNavy
                                            )
                                        )
                                        Text(
                                            text = "Complete modules & earn virtual badges",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = Color(0xFF717970),
                                                fontSize = 11.sp
                                            )
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (allMissionsDone) Color(0xFFDCF0DF) else Color(0xFFFBF1DD))
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "$completedMissionsCount / $totalMissionsCount Done",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (allMissionsDone) Color(0xFF386B3B) else Color(0xFF7A581E)
                                        )
                                    )
                                }
                            }

                            // All completed celebration banner
                            if (allMissionsDone) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Card(
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFDCF0DF)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, Color(0xFF386B3B).copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "🎖️", fontSize = 24.sp)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "All Daily Missions Complete!",
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF386B3B)
                                                )
                                            )
                                            Text(
                                                text = "Daily All-Star Hero Badge unlocked in Trophy Room!",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = Color(0xFF386B3B).copy(alpha = 0.9f),
                                                    fontSize = 11.sp
                                                )
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // List of Daily Missions
                            Column(
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                dailyMissions.forEach { mission ->
                                    val isCompleted = mission.isCompleted
                                    val isClaimed = mission.isRewardClaimed
                                    val gameRoute = when (mission.category.uppercase()) {
                                        "MATH" -> "game_math"
                                        "SPELLING" -> "game_word"
                                        "MEMORY" -> "game_memory"
                                        "SCIENCE" -> "game_science"
                                        "SHAPES" -> "game_shapes"
                                        else -> "game_math"
                                    }

                                    Card(
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = when {
                                                isClaimed -> Color(0xFFF7FAF6)
                                                isCompleted -> Color(0xFFFBF4D8)
                                                else -> Color(0xFFFDFEFA)
                                            }
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(
                                                width = 1.dp,
                                                color = when {
                                                    isClaimed -> Color(0xFFDCE5DB)
                                                    isCompleted -> Color(0xFFE2CCA2)
                                                    else -> Color(0xFFDCE5DB)
                                                },
                                                shape = RoundedCornerShape(16.dp)
                                            )
                                            .testTag("daily_mission_${mission.missionCode}")
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                // Icon
                                                Box(
                                                    modifier = Modifier
                                                        .size(42.dp)
                                                        .clip(CircleShape)
                                                        .background(
                                                            if (isCompleted) Color(0xFFEDF3EB)
                                                            else Color(0xFFF3F6F0)
                                                        ),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(text = mission.iconEmoji, fontSize = 22.sp)
                                                }

                                                Spacer(modifier = Modifier.width(12.dp))

                                                Column(modifier = Modifier.weight(1f)) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Text(
                                                            text = mission.title,
                                                            style = MaterialTheme.typography.titleSmall.copy(
                                                                fontWeight = FontWeight.Bold,
                                                                color = ParentNavy
                                                            )
                                                        )
                                                        if (isCompleted) {
                                                            Spacer(modifier = Modifier.width(4.dp))
                                                            Icon(
                                                                imageVector = Icons.Default.CheckCircle,
                                                                contentDescription = "Completed",
                                                                tint = PlayfulGreen,
                                                                modifier = Modifier.size(16.dp)
                                                            )
                                                        }
                                                    }
                                                    Spacer(modifier = Modifier.height(2.dp))
                                                    Text(
                                                        text = mission.description,
                                                        style = MaterialTheme.typography.bodySmall.copy(
                                                            color = Color(0xFF414941),
                                                            fontSize = 11.sp
                                                        )
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(10.dp))

                                            // Reward Badge & Progress Row
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                // Reward Badge info pill
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(Color(0xFFEDF3EB))
                                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                                ) {
                                                    Text(text = mission.rewardBadgeIcon, fontSize = 14.sp)
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = "${mission.rewardBadgeTitle} (+${mission.rewardStars}⭐)",
                                                        style = MaterialTheme.typography.labelSmall.copy(
                                                            fontWeight = FontWeight.Bold,
                                                            color = Color(0xFF386B3B),
                                                            fontSize = 10.sp
                                                        )
                                                    )
                                                }

                                                // Action Buttons
                                                when {
                                                    isCompleted && !isClaimed -> {
                                                        Button(
                                                            onClick = { onClaimMissionReward(mission) },
                                                            shape = RoundedCornerShape(12.dp),
                                                            colors = ButtonDefaults.buttonColors(
                                                                containerColor = Color(0xFF386B3B)
                                                            ),
                                                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                                            modifier = Modifier
                                                                .height(34.dp)
                                                                .testTag("claim_mission_button_${mission.id}")
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.CardGiftcard,
                                                                contentDescription = null,
                                                                tint = Color.White,
                                                                modifier = Modifier.size(16.dp)
                                                            )
                                                            Spacer(modifier = Modifier.width(6.dp))
                                                            Text(
                                                                text = "Claim Badge 🎁",
                                                                style = MaterialTheme.typography.labelSmall.copy(
                                                                    fontWeight = FontWeight.Bold,
                                                                    color = Color.White
                                                                )
                                                            )
                                                        }
                                                    }
                                                    isClaimed -> {
                                                        Row(
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            modifier = Modifier
                                                                .clip(RoundedCornerShape(10.dp))
                                                                .background(Color(0xFFDCF0DF))
                                                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                                        ) {
                                                            Text(text = "🏅", fontSize = 12.sp)
                                                            Spacer(modifier = Modifier.width(4.dp))
                                                            Text(
                                                                text = "Badge Earned ✨",
                                                                style = MaterialTheme.typography.labelSmall.copy(
                                                                    fontWeight = FontWeight.Bold,
                                                                    color = Color(0xFF386B3B),
                                                                    fontSize = 11.sp
                                                                )
                                                            )
                                                        }
                                                    }
                                                    else -> {
                                                        FilledTonalButton(
                                                            onClick = { onNavigateToGame(gameRoute) },
                                                            shape = RoundedCornerShape(12.dp),
                                                            colors = ButtonDefaults.filledTonalButtonColors(
                                                                containerColor = Color(0xFFEDF3EB),
                                                                contentColor = PrimaryKids
                                                            ),
                                                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                                            modifier = Modifier
                                                                .height(34.dp)
                                                                .testTag("play_mission_button_${mission.id}")
                                                        ) {
                                                            Text(
                                                                text = "Play Module 🚀",
                                                                style = MaterialTheme.typography.labelSmall.copy(
                                                                    fontWeight = FontWeight.Bold,
                                                                    color = PrimaryKids
                                                                )
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Game Hub Spotlight Banner
            item {
                Card(
                    onClick = onNavigateToGameHub,
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEDF3EB)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, Color(0xFF386B3B).copy(alpha = 0.3f), RoundedCornerShape(22.dp))
                        .testTag("home_game_hub_card")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(PrimaryKids),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🎮", fontSize = 28.sp)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Arcade Game Hub",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = ParentNavy
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFF386B3B))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "NEW",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Black,
                                            color = Color.White,
                                            fontSize = 9.sp
                                        )
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Math puzzles, memory match, spelling & science labs!",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF414941),
                                    fontSize = 11.sp
                                )
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Open Game Hub",
                            tint = PrimaryKids
                        )
                    }
                }
            }

            // Badges & Trophy quick bar
            item {
                Card(
                    onClick = onNavigateToBadges,
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF1DD)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFE2CCA2), RoundedCornerShape(18.dp))
                        .testTag("home_trophy_ribbon")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF3DEBE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🏆", fontSize = 24.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Trophy Room & Badges",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF7A581E)
                                )
                            )
                            Text(
                                text = "$unlockedBadgesCount / ${badges.size} Badges Unlocked",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF8B6A2B)
                                )
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "View Badges",
                            tint = Color(0xFF8B6A2B)
                        )
                    }
                }
            }

            // Section Title
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🎮 Interactive Learning Games",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ParentNavy
                        )
                    )
                    OutlinedButton(
                        onClick = onNavigateToGameHub,
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier
                            .height(32.dp)
                            .testTag("view_game_hub_button")
                    ) {
                        Text(
                            text = "Game Hub ➔",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryKids
                            )
                        )
                    }
                }
            }

            // Game Cards
            items(games.size) { index ->
                val game = games[index]
                Card(
                    onClick = { onNavigateToGame(game.routeKey) },
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFDCE5DB), RoundedCornerShape(22.dp))
                        .testTag("game_card_${game.routeKey}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Game Gradient Icon Box
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(Brush.linearGradient(game.gradientColors)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = game.emoji, fontSize = 32.sp)
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = game.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = ParentNavy
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = game.subtitle,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF414941),
                                    fontSize = 12.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFEDF3EB))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = game.difficultyLabel,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryKids,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(PrimaryKids),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
