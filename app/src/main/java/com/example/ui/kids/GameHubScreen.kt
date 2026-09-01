package com.example.ui.kids

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Badge
import com.example.data.model.ChildProfile
import com.example.data.model.DailyMission
import com.example.data.model.GameCategory
import com.example.data.model.GameSession
import com.example.data.model.SubjectMastery
import com.example.ui.components.AvatarHelper
import com.example.ui.theme.ParentEmerald
import com.example.ui.theme.ParentNavy
import com.example.ui.theme.PlayfulCoral
import com.example.ui.theme.PlayfulGreen
import com.example.ui.theme.PlayfulOrange
import com.example.ui.theme.PlayfulPurple
import com.example.ui.theme.PlayfulSky
import com.example.ui.theme.PlayfulTeal
import com.example.ui.theme.PlayfulYellow
import com.example.ui.theme.PrimaryKids
import com.example.ui.theme.StarGold
import com.example.ui.theme.StreakFire

data class MiniGameInfo(
    val category: GameCategory,
    val title: String,
    val subtitle: String,
    val description: String,
    val icon: ImageVector,
    val emoji: String,
    val skillsLearned: List<String>,
    val gradientColors: List<Color>,
    val badgeThemeColor: Color,
    val routeKey: String,
    val ageRange: String,
    val difficulty: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameHubScreen(
    activeChild: ChildProfile?,
    allChildren: List<ChildProfile>,
    sessions: List<GameSession>,
    subjectMasteryList: List<SubjectMastery>,
    badges: List<Badge>,
    dailyMissions: List<DailyMission>,
    onBack: () -> Unit,
    onNavigateToGame: (String) -> Unit,
    onNavigateToBadges: () -> Unit,
    onOpenParentZone: () -> Unit,
    onSelectChild: (Long) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("ALL") }

    val allGames = listOf(
        MiniGameInfo(
            category = GameCategory.MATH,
            title = "Math Galaxy",
            subtitle = "Blast cosmic asteroids with addition & subtraction!",
            description = "Solve fun arithmetic equations with speed and precision. Build number sense, quick calculations, and logical reasoning.",
            icon = Icons.Default.RocketLaunch,
            emoji = "🚀",
            skillsLearned = listOf("Mental Addition", "Subtraction", "Speed Thinking", "Number Sense"),
            gradientColors = listOf(Color(0xFF386B3B), Color(0xFF5C7451)),
            badgeThemeColor = Color(0xFF386B3B),
            routeKey = "game_math",
            ageRange = "Ages 5-10",
            difficulty = "All Levels"
        ),
        MiniGameInfo(
            category = GameCategory.MEMORY,
            title = "Memory Safari",
            subtitle = "Match animal pairs in the wild savannah!",
            description = "Flip colorful jungle tiles to find twin animals. Enhances working memory, spatial recall, and visual attention span.",
            icon = Icons.Default.Extension,
            emoji = "🦁",
            skillsLearned = listOf("Visual Recall", "Pattern Recognition", "Focus & Attention", "Working Memory"),
            gradientColors = listOf(Color(0xFF8B6A2B), Color(0xFFA57D35)),
            badgeThemeColor = Color(0xFF8B6A2B),
            routeKey = "game_memory",
            ageRange = "Ages 4-9",
            difficulty = "3 Grid Sizes"
        ),
        MiniGameInfo(
            category = GameCategory.SPELLING,
            title = "Word Explorer",
            subtitle = "Spell magical words letter by letter!",
            description = "Unscramble letters and identify vocabulary with friendly emojis. Develops phonics, sight word recognition, and reading confidence.",
            icon = Icons.Default.MenuBook,
            emoji = "📚",
            skillsLearned = listOf("Phonics & Spelling", "Vocabulary Expansion", "Letter Sequencing", "Word Building"),
            gradientColors = listOf(Color(0xFF4A6B7C), Color(0xFF5F8295)),
            badgeThemeColor = Color(0xFF4A6B7C),
            routeKey = "game_word",
            ageRange = "Ages 5-10",
            difficulty = "Beginner to Pro"
        ),
        MiniGameInfo(
            category = GameCategory.SCIENCE,
            title = "Science & Nature",
            subtitle = "Explore animals, planets, and planet trivia!",
            description = "Discover amazing facts about our world, space, and living creatures. Boosts scientific curiosity and general knowledge.",
            icon = Icons.Default.Science,
            emoji = "🔬",
            skillsLearned = listOf("Animal Habitats", "Solar System", "Ecology & Nature", "Deductive Trivia"),
            gradientColors = listOf(Color(0xFF5C7451), Color(0xFF6B8E4E)),
            badgeThemeColor = Color(0xFF5C7451),
            routeKey = "game_science",
            ageRange = "Ages 6-12",
            difficulty = "Curiosity Quests"
        ),
        MiniGameInfo(
            category = GameCategory.SHAPES,
            title = "Color & Shapes",
            subtitle = "Mix magical colors & match geometric patterns!",
            description = "Identify 2D/3D shapes, discover secondary color mixing formulas, and complete geometric visual pattern sequences.",
            icon = Icons.Default.Category,
            emoji = "🎨",
            skillsLearned = listOf("Geometry & Shapes", "Color Theory", "Pattern Sequences", "Spatial Logic"),
            gradientColors = listOf(Color(0xFFC85A38), Color(0xFFD47335)),
            badgeThemeColor = Color(0xFFC85A38),
            routeKey = "game_shapes",
            ageRange = "Ages 4-8",
            difficulty = "Interactive Lab"
        )
    )

    val filterOptions = listOf(
        "ALL" to "All Mini-Games 🎮",
        "MATH" to "Math Puzzles 🔢",
        "MEMORY" to "Memory Matching 🧩",
        "SPELLING" to "Spelling & Words 📖",
        "SCIENCE" to "Science & Nature 🔬",
        "SHAPES" to "Shapes & Colors 🎨"
    )

    val filteredGames = when (selectedFilter) {
        "ALL" -> allGames
        else -> allGames.filter { it.category.name.equals(selectedFilter, ignoreCase = true) }
    }

    val childSessions = sessions.filter { it.childId == activeChild?.id }
    val totalGamesPlayed = childSessions.size
    val todayPlayedMinutes = activeChild?.todayPlayedMinutes ?: 0
    val dailyGoal = activeChild?.dailyGoalMinutes ?: 30
    val totalStars = activeChild?.totalStars ?: 0

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🎮", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Interactive Game Hub",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = ParentNavy
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("game_hub_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Home",
                            tint = ParentNavy
                        )
                    }
                },
                actions = {
                    // Quick Trophy Icon
                    IconButton(
                        onClick = onNavigateToBadges,
                        modifier = Modifier.testTag("game_hub_badges_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Trophies",
                            tint = Color(0xFF8B6A2B)
                        )
                    }
                    // Parent Zone quick gate
                    IconButton(
                        onClick = onOpenParentZone,
                        modifier = Modifier.testTag("game_hub_parent_gate_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Parent Dashboard",
                            tint = ParentNavy
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
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
                Spacer(modifier = Modifier.height(4.dp))

                // Active Player & Stats Header
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFDCE5DB), RoundedCornerShape(22.dp))
                ) {
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
                            // Avatar & Name
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val avatar = AvatarHelper.getAvatar(activeChild?.avatarId ?: "fox")
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(avatar.bgColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = avatar.emoji, fontSize = 26.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = activeChild?.name ?: "Explorer",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Black,
                                            color = ParentNavy
                                        )
                                    )
                                    Text(
                                        text = "${activeChild?.gradeLevel ?: "Kindergarten"} • Learning Arcade",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color(0xFF717970),
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }

                            // Stars & Streak
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color(0xFFFBECE8))
                                        .border(1.dp, Color(0xFFE5C0B5), RoundedCornerShape(14.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "🔥 ${activeChild?.currentStreakDays ?: 1}d",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = StreakFire
                                        )
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color(0xFFFBF1DD))
                                        .border(1.dp, Color(0xFFE2CCA2), RoundedCornerShape(14.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "⭐ $totalStars",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF7A581E)
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick Stats Grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Stat 1: Games Completed
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFEDF3EB)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "$totalGamesPlayed",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Black,
                                            color = Color(0xFF386B3B)
                                        )
                                    )
                                    Text(
                                        text = "Sessions Played",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 10.sp,
                                            color = Color(0xFF5C7451)
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            // Stat 2: Daily Learning Time
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF1DD)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "${todayPlayedMinutes}m",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Black,
                                            color = Color(0xFF7A581E)
                                        )
                                    )
                                    Text(
                                        text = "Time Today",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 10.sp,
                                            color = Color(0xFF8B6A2B)
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            // Stat 3: Daily Missions
                            val missionsDone = dailyMissions.count { it.isCompleted }
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF1F8)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "$missionsDone/${dailyMissions.size}",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Black,
                                            color = Color(0xFF4A6B7C)
                                        )
                                    )
                                    Text(
                                        text = "Missions Done",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 10.sp,
                                            color = Color(0xFF5F8295)
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Real-Time Parent Dashboard Sync Notice Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F7EE)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFC3DEC0), RoundedCornerShape(18.dp))
                        .testTag("parent_sync_banner")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFDCF0DF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = Color(0xFF386B3B),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Live Parent Dashboard Sync",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF386B3B)
                                )
                            )
                            Text(
                                text = "Every mini-game tracks accuracy, problem solving speed, and subject mastery for parents in real time.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF414941),
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }

            // Category Filter Chips
            item {
                Column {
                    Text(
                        text = "Explore Learning Categories",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = ParentNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(filterOptions) { (key, label) ->
                            val isSelected = selectedFilter == key
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedFilter = key },
                                label = {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PrimaryKids,
                                    selectedLabelColor = Color.White,
                                    containerColor = Color.White,
                                    labelColor = ParentNavy
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = if (isSelected) PrimaryKids else Color(0xFFDCE5DB)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("filter_chip_$key")
                            )
                        }
                    }
                }
            }

            // Game Cards List with Real-Time Child Mastery
            items(filteredGames.size) { index ->
                val game = filteredGames[index]
                val mastery = subjectMasteryList.find { it.category == game.category }
                val gameSessions = childSessions.filter { it.gameCategory == game.category.name }
                val highestScore = gameSessions.maxOfOrNull { it.score } ?: 0
                val sessionsCount = gameSessions.size
                val masteryPercent = mastery?.accuracyPercentage ?: 0

                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFDCE5DB), RoundedCornerShape(22.dp))
                        .testTag("game_hub_card_${game.routeKey}")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Top Row: Emoji Icon, Title, Difficulty Tags
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Brush.linearGradient(game.gradientColors)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = game.emoji, fontSize = 28.sp)
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = game.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = ParentNavy
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = game.subtitle,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF414941),
                                        fontSize = 11.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0xFFEDF3EB))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = game.ageRange,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = Color(0xFF386B3B),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 9.sp
                                            )
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0xFFFBF1DD))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = game.difficulty,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = Color(0xFF7A581E),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 9.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = game.description,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF414941),
                                fontSize = 12.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Skills Learned Tags
                        Text(
                            text = "Skills Developed:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF717970)
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            game.skillsLearned.take(3).forEach { skill ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFF3F6F0))
                                        .padding(horizontal = 7.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "✓ $skill",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFF414941),
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Real-Time Child Performance Metric Bar
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF7FAF6)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFFDCE5DB), RoundedCornerShape(14.dp))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${activeChild?.name ?: "Child"}'s Mastery Level",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = ParentNavy
                                        )
                                    )
                                    Text(
                                        text = "$masteryPercent%",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Black,
                                            color = if (masteryPercent >= 70) PlayfulGreen else PrimaryKids
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                LinearProgressIndicator(
                                    progress = { (masteryPercent / 100f).coerceIn(0f, 1f) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = if (masteryPercent >= 70) PlayfulGreen else PrimaryKids,
                                    trackColor = Color(0xFFDCE5DB)
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "High Score: ${highestScore}%",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color(0xFF717970),
                                            fontSize = 10.sp
                                        )
                                    )
                                    Text(
                                        text = "Completed: $sessionsCount games",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color(0xFF717970),
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Play Now Action Button
                        Button(
                            onClick = { onNavigateToGame(game.routeKey) },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = game.badgeThemeColor
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("play_game_button_${game.routeKey}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Play ${game.title} 🚀",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // Bottom Spacing
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
