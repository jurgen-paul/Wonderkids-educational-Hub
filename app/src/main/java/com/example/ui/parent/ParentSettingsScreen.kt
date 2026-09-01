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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ParentSettings
import com.example.ui.theme.ParentAccent
import com.example.ui.theme.ParentNavy
import com.example.ui.theme.ParentRose

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentSettingsScreen(
    settings: ParentSettings,
    onUpdateSettings: (ParentSettings) -> Unit,
    onResetScreenTime: () -> Unit,
    onResetAllProgress: () -> Unit,
    onBack: () -> Unit
) {
    var showPinDialog by remember { mutableStateOf(false) }
    var showResetDataDialog by remember { mutableStateOf(false) }
    var showEditProfileDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Account Settings & Controls", fontWeight = FontWeight.Bold, color = Color.White)
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("settings_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Account Profile Header Card
            item {
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
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEDF3EB)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = ParentAccent,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = settings.parentName,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = ParentNavy
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0xFFDCF0DF))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "FAMILY PRO",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF235527),
                                                fontSize = 9.sp
                                            )
                                        )
                                    }
                                }
                                Text(
                                    text = settings.parentEmail,
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF717970))
                                )
                            }
                            TextButton(
                                onClick = { showEditProfileDialog = true },
                                modifier = Modifier.testTag("edit_parent_profile_button")
                            ) {
                                Text("Edit", color = ParentAccent, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // 2. Screen Time & Limits Section
            item {
                Text(
                    text = "⏱️ Screen Time & Wellness",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = ParentNavy
                    )
                )
            }

            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFDCE5DB), RoundedCornerShape(20.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Slider for Daily Limit
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Daily Screen Limit",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = ParentNavy
                                    )
                                )
                                Text(
                                    text = "${settings.dailyScreenLimitMinutes} mins/day",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        color = ParentAccent
                                    )
                                )
                            }

                            Slider(
                                value = settings.dailyScreenLimitMinutes.toFloat(),
                                onValueChange = { newLimit ->
                                    val rounded = ((newLimit / 15).toInt() * 15).coerceIn(15, 120)
                                    onUpdateSettings(settings.copy(dailyScreenLimitMinutes = rounded))
                                },
                                valueRange = 15f..120f,
                                steps = 6,
                                colors = SliderDefaults.colors(
                                    thumbColor = ParentAccent,
                                    activeTrackColor = ParentAccent
                                ),
                                modifier = Modifier.testTag("screen_limit_slider")
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("15m", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF717970)))
                                Text("45m", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF717970)))
                                Text("90m", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF717970)))
                                Text("120m", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF717970)))
                            }
                        }

                        // Bedtime lock switch
                        SettingsToggleRow(
                            icon = Icons.Default.Bedtime,
                            title = "Bedtime Mode (8:00 PM - 7:00 AM)",
                            subtitle = "Locks educational games during evening rest hours",
                            checked = settings.bedtimeReminderEnabled,
                            onCheckedChange = { onUpdateSettings(settings.copy(bedtimeReminderEnabled = it)) }
                        )

                        // Reset today's screen time button
                        Button(
                            onClick = onResetScreenTime,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEDF3EB)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("reset_today_timer_button")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, tint = ParentNavy, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Reset Today's Screen Time Counter", color = ParentNavy, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // 3. Security & Parent Gate
            item {
                Text(
                    text = "🔒 Security & Parental Controls",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = ParentNavy
                    )
                )
            }

            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFDCE5DB), RoundedCornerShape(20.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Change PIN row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showPinDialog = true }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFEDF3EB)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = ParentAccent, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Parent Access PIN",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = ParentNavy
                                        )
                                    )
                                    Text(
                                        text = "Current PIN: •••• (Tap to change)",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF717970))
                                    )
                                }
                            }
                            Text("Change", color = ParentAccent, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        // Sound Effects switch
                        SettingsToggleRow(
                            icon = Icons.Default.VolumeUp,
                            title = "Game Sound Effects",
                            subtitle = "Fun audio cues on correct answers and game milestones",
                            checked = settings.soundEffectsEnabled,
                            onCheckedChange = { onUpdateSettings(settings.copy(soundEffectsEnabled = it)) }
                        )

                        // Background Music switch
                        SettingsToggleRow(
                            icon = Icons.Default.MusicNote,
                            title = "Playful Background Music",
                            subtitle = "Gentle ambient melodies during game exploration",
                            checked = settings.backgroundMusicEnabled,
                            onCheckedChange = { onUpdateSettings(settings.copy(backgroundMusicEnabled = it)) }
                        )

                        // Math Gate Verification switch
                        SettingsToggleRow(
                            icon = Icons.Default.School,
                            title = "Grown-up Math Gate",
                            subtitle = "Requires solving multiplication challenges to open parent settings",
                            checked = settings.mathGateEnabled,
                            onCheckedChange = { onUpdateSettings(settings.copy(mathGateEnabled = it)) }
                        )
                    }
                }
            }

            // 4. Data Management Section
            item {
                Text(
                    text = "⚙️ Data & Reset",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = ParentNavy
                    )
                )
            }

            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFDCE5DB), RoundedCornerShape(20.dp))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Reset Progress",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = ParentRose
                            )
                        )
                        Text(
                            text = "Clear recent activity sessions and restart mastery metrics from scratch.",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF717970)),
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                        Button(
                            onClick = { showResetDataDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBECE8)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("reset_all_progress_button")
                        ) {
                            Text("Reset Learning Data", color = ParentRose, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Change PIN Dialog
    if (showPinDialog) {
        var newPin by remember { mutableStateOf("") }
        var errorMsg by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showPinDialog = false },
            title = { Text("Change Parent PIN", fontWeight = FontWeight.Bold, color = ParentNavy) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Enter a new 4-digit security PIN for accessing parent controls:")
                    OutlinedTextField(
                        value = newPin,
                        onValueChange = { if (it.length <= 4 && it.all { ch -> ch.isDigit() }) newPin = it },
                        label = { Text("New 4-digit PIN") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ParentAccent),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("new_pin_input")
                    )
                    if (errorMsg.isNotEmpty()) {
                        Text(errorMsg, color = ParentRose, style = MaterialTheme.typography.bodySmall)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newPin.length == 4) {
                            onUpdateSettings(settings.copy(pinCode = newPin))
                            showPinDialog = false
                        } else {
                            errorMsg = "PIN must be exactly 4 digits"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ParentAccent),
                    modifier = Modifier.testTag("confirm_pin_change_button")
                ) {
                    Text("Save PIN", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPinDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }

    // Edit Parent Profile Dialog
    if (showEditProfileDialog) {
        var parentNameInput by remember { mutableStateOf(settings.parentName) }
        var parentEmailInput by remember { mutableStateOf(settings.parentEmail) }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("Edit Parent Account", fontWeight = FontWeight.Bold, color = ParentNavy) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = parentNameInput,
                        onValueChange = { parentNameInput = it },
                        label = { Text("Parent / Guardian Name") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ParentAccent),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = parentEmailInput,
                        onValueChange = { parentEmailInput = it },
                        label = { Text("Contact Email") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ParentAccent),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateSettings(
                            settings.copy(
                                parentName = parentNameInput.trim(),
                                parentEmail = parentEmailInput.trim()
                            )
                        )
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ParentAccent)
                ) {
                    Text("Save", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }

    // Reset Confirmation Dialog
    if (showResetDataDialog) {
        AlertDialog(
            onDismissRequest = { showResetDataDialog = false },
            title = { Text("Confirm Data Reset?", fontWeight = FontWeight.Bold, color = ParentRose) },
            text = { Text("This will clear learning activity sessions and recalculate subject masteries. Child profiles and current stars will be preserved.") },
            confirmButton = {
                Button(
                    onClick = {
                        onResetAllProgress()
                        showResetDataDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ParentRose)
                ) {
                    Text("Yes, Reset Data", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDataDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }
}

@Composable
fun SettingsToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEEF2FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = ParentAccent, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = ParentNavy
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = ParentAccent
            )
        )
    }
}

