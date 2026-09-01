package com.example.ui.parent

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChildProfile
import com.example.data.model.GameSession
import com.example.data.model.ParentSettings
import com.example.data.model.SubjectMastery
import com.example.ui.components.AvatarHelper
import com.example.ui.theme.ParentAccent
import com.example.ui.theme.ParentAmber
import com.example.ui.theme.ParentEmerald
import com.example.ui.theme.ParentNavy
import com.example.ui.theme.ParentRose
import com.example.ui.theme.ParentSlate
import com.example.ui.theme.PlayfulGreen
import com.example.ui.theme.PlayfulOrange
import com.example.ui.theme.PlayfulPurple
import com.example.ui.theme.PlayfulSky
import com.example.ui.theme.PlayfulTeal
import com.example.ui.theme.PrimaryKids
import com.example.ui.theme.StarGold
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentDashboardScreen(
    activeChild: ChildProfile?,
    allChildren: List<ChildProfile>,
    sessions: List<GameSession>,
    subjectMasteryList: List<SubjectMastery>,
    parentSettings: ParentSettings,
    onSelectChild: (Long) -> Unit,
    onNavigateToProfiles: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onExitParentZone: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Parent Dashboard", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onExitParentZone,
                        modifier = Modifier.testTag("parent_exit_button")
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kids Mode", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(
                        onClick = onNavigateToProfiles,
                        modifier = Modifier.testTag("parent_manage_profiles_nav")
                    ) {
                        Icon(Icons.Default.ManageAccounts, contentDescription = "Child Profiles", tint = Color.White)
                    }
                    IconButton(
                        onClick = onNavigateToSettings,
                        modifier = Modifier.testTag("parent_settings_nav")
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = "Account Settings", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = ParentNavy
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
            // Child Switcher Row
            item {
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(allChildren) { child ->
                        val isSelected = child.id == activeChild?.id
                        val avatar = AvatarHelper.getAvatar(child.avatarId)

                        Card(
                            onClick = { onSelectChild(child.id) },
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) ParentNavy else Color.White
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp),
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) ParentNavy else Color(0xFFDCE5DB),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .testTag("parent_child_tab_${child.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(avatar.bgColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = avatar.emoji, fontSize = 16.sp)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = child.name,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else ParentNavy
                                        )
                                    )
                                    Text(
                                        text = "Age ${child.age} • ${child.gradeLevel}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (isSelected) Color(0xFFC0CDC0) else Color(0xFF717970),
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Add child quick button
                    item {
                        OutlinedButton(
                            onClick = onNavigateToProfiles,
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.height(52.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = ParentAccent, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Kid", color = ParentAccent, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Screen Time & Daily Goal Progress Card
            item {
                if (activeChild != null) {
                    val played = activeChild.todayPlayedMinutes
                    val limit = parentSettings.dailyScreenLimitMinutes
                    val goal = activeChild.dailyGoalMinutes
                    val percent = if (limit > 0) (played.toFloat() / limit.toFloat()).coerceIn(0f, 1f) else 0f

                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFDCE5DB), RoundedCornerShape(22.dp))
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFEDF3EB)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Schedule,
                                            contentDescription = null,
                                            tint = ParentAccent,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "Today's Learning Time",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = ParentNavy
                                            )
                                        )
                                        Text(
                                            text = "Daily Screen Limit: $limit mins",
                                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF717970))
                                        )
                                    }
                                }

                                Text(
                                    text = "$played / $limit min",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = if (played >= limit) ParentRose else ParentEmerald
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            LinearProgressIndicator(
                                progress = { percent },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(12.dp)
                                    .clip(RoundedCornerShape(6.dp)),
                                color = if (played >= limit) ParentRose else ParentEmerald,
                                trackColor = Color(0xFFEDF3EB)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "🔥 ${activeChild.currentStreakDays}-day streak active",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = PlayfulOrange
                                    )
                                )
                                Text(
                                    text = "⭐ ${activeChild.totalStars} total stars",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF8B6A2B)
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Pedagogical Insights & Recommendations
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEDF3EB)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFDCE5DB), RoundedCornerShape(22.dp))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Insights",
                                tint = Color(0xFF386B3B),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Learning Insights & Recommendations",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B381C)
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val childName = activeChild?.name ?: "Your child"
                        val insightText = if ((activeChild?.age ?: 6) <= 6) {
                            "• $childName is showing high engagement with Counting & Color Puzzles!\n• Suggested next step: Practice simple 2-letter word spelling in Word Explorer.\n• Consistent 15-minute daily sessions provide the highest retention for early learners."
                        } else {
                            "• $childName maintains strong accuracy in Math Galaxy arithmetic & Science trivia!\n• Suggested next step: Try timed Memory Safari rounds to sharpen pattern recall.\n• Excellent learning streak of ${activeChild?.currentStreakDays ?: 1} days in a row!"
                        }

                        Text(
                            text = insightText,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF2E4C2D),
                                lineHeight = 22.sp
                            )
                        )
                    }
                }
            }

            // Subject Performance Breakdown
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📊 Subject Performance Breakdown",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ParentNavy
                        )
                    )
                }
            }

            items(subjectMasteryList) { item ->
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFDCE5DB), RoundedCornerShape(18.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(item.category.tagColorHex).copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = when (item.category.name) {
                                            "MATH" -> "🔢"
                                            "SPELLING" -> "📚"
                                            "MEMORY" -> "🧩"
                                            "SCIENCE" -> "🔬"
                                            else -> "🎨"
                                        },
                                        fontSize = 18.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = item.category.displayName,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = ParentNavy
                                        )
                                    )
                                    Text(
                                        text = item.recommendedLevel,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFF414941)
                                        )
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${item.accuracyPercentage}% Accuracy",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        color = if (item.accuracyPercentage >= 80) ParentEmerald else ParentAmber
                                    )
                                )
                                Text(
                                    text = "${item.totalPlayed} Sessions",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF717970))
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { (item.accuracyPercentage / 100f).coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = Color(item.category.tagColorHex),
                            trackColor = Color(0xFFEDF3EB)
                        )
                    }
                }
            }

            // Recent Activity Sessions Feed
            item {
                Text(
                    text = "🕒 Recent Learning Activity Log",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = ParentNavy
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (sessions.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFDCE5DB), RoundedCornerShape(16.dp))
                    ) {
                        Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                            Text("No game sessions recorded yet. Play a game to see real-time analytics!", color = Color(0xFF717970))
                        }
                    }
                }
            } else {
                items(sessions.take(10)) { session ->
                    val dateFormatted = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()).format(Date(session.timestamp))
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFDCE5DB), RoundedCornerShape(16.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = session.gameTitle,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = ParentNavy
                                    )
                                )
                                Text(
                                    text = "$dateFormatted • ${session.durationSeconds}s duration",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF717970),
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFDCF0DF))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${session.score}%",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF235527)
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "+${session.starsEarned}⭐",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF8B6A2B)
                                    )
                                )
                            }
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
