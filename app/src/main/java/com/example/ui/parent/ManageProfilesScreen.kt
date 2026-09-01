package com.example.ui.parent

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import com.example.ui.components.AvatarHelper
import com.example.ui.components.AvatarOption
import com.example.ui.theme.ParentAccent
import com.example.ui.theme.ParentNavy
import com.example.ui.theme.ParentRose
import com.example.ui.theme.PlayfulGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageProfilesScreen(
    children: List<ChildProfile>,
    activeChildId: Long,
    onSelectChild: (Long) -> Unit,
    onAddChild: (name: String, age: Int, grade: String, avatarId: String, goalMins: Int) -> Unit,
    onUpdateChild: (ChildProfile) -> Unit,
    onDeleteChild: (Long) -> Unit,
    onBack: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingChild by remember { mutableStateOf<ChildProfile?>(null) }
    var deletingChildId by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Manage Child Profiles", fontWeight = FontWeight.Bold, color = Color.White)
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("manage_profiles_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = ParentNavy
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = ParentAccent,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("add_child_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Child Profile")
            }
        },
        containerColor = Color(0xFFFBFCF8)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "Learners in your family (${children.size})",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = ParentNavy
                    )
                )
                Text(
                    text = "Each child has customized game difficulties, streaks, and progress tracking.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF717970)),
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            items(children) { child ->
                val isActive = child.id == activeChildId
                val avatar = AvatarHelper.getAvatar(child.avatarId)

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = if (isActive) 2.dp else 1.dp,
                            color = if (isActive) ParentAccent else Color(0xFFDCE5DB),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .testTag("manage_child_card_${child.id}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(avatar.bgColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = avatar.emoji, fontSize = 28.sp)
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = child.name,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = ParentNavy
                                        )
                                    )
                                    if (isActive) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color(0xFFEDF3EB))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "ACTIVE",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = ParentAccent,
                                                    fontSize = 9.sp
                                                )
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "Age ${child.age} • ${child.gradeLevel} • ${child.dailyGoalMinutes}m Daily Goal",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF414941))
                                )
                                Text(
                                    text = "⭐ ${child.totalStars} Stars • 🔥 ${child.currentStreakDays} Days Streak",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF8B6A2B),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Actions Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!isActive) {
                                Button(
                                    onClick = { onSelectChild(child.id) },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = ParentNavy),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Switch To", fontSize = 13.sp)
                                }
                            }

                            OutlinedButton(
                                onClick = { editingChild = child },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Edit", fontSize = 13.sp)
                            }

                            if (children.size > 1) {
                                IconButton(
                                    onClick = { deletingChildId = child.id },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFFBECE8))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Profile",
                                        tint = ParentRose,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
        }
    }

    // Add Child Dialog
    if (showAddDialog) {
        ChildFormDialog(
            title = "Add Child Profile",
            initialName = "",
            initialAge = 6,
            initialGrade = "1st Grade",
            initialAvatarId = "bear",
            initialGoalMinutes = 20,
            onConfirm = { name, age, grade, avatar, goal ->
                onAddChild(name, age, grade, avatar, goal)
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }

    // Edit Child Dialog
    editingChild?.let { child ->
        ChildFormDialog(
            title = "Edit ${child.name}'s Profile",
            initialName = child.name,
            initialAge = child.age,
            initialGrade = child.gradeLevel,
            initialAvatarId = child.avatarId,
            initialGoalMinutes = child.dailyGoalMinutes,
            onConfirm = { name, age, grade, avatar, goal ->
                onUpdateChild(
                    child.copy(
                        name = name,
                        age = age,
                        gradeLevel = grade,
                        avatarId = avatar,
                        dailyGoalMinutes = goal
                    )
                )
                editingChild = null
            },
            onDismiss = { editingChild = null }
        )
    }

    // Delete confirmation
    deletingChildId?.let { id ->
        AlertDialog(
            onDismissRequest = { deletingChildId = null },
            title = { Text("Delete Child Profile?", fontWeight = FontWeight.Bold, color = ParentNavy) },
            text = { Text("Are you sure you want to remove this profile? All saved stars and badges for this child will be removed.") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteChild(id)
                        deletingChildId = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ParentRose)
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingChildId = null }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChildFormDialog(
    title: String,
    initialName: String,
    initialAge: Int,
    initialGrade: String,
    initialAvatarId: String,
    initialGoalMinutes: Int,
    onConfirm: (name: String, age: Int, grade: String, avatarId: String, goalMins: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var age by remember { mutableIntStateOf(initialAge) }
    var selectedAvatarId by remember { mutableStateOf(initialAvatarId) }
    var goalMinutes by remember { mutableIntStateOf(initialGoalMinutes) }

    val avatars: List<AvatarOption> = AvatarHelper.avatars

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(title, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = ParentNavy))
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Child's Name") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ParentAccent,
                        focusedLabelColor = ParentAccent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("child_name_input")
                )

                // Avatar selection
                Text(
                    text = "Pick Avatar Character:",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = ParentNavy)
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (avatar in avatars) {
                        val isSelected = avatar.id == selectedAvatarId
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(avatar.bgColor)
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) ParentAccent else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { selectedAvatarId = avatar.id }
                                .testTag("avatar_option_${avatar.id}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = avatar.emoji, fontSize = 22.sp)
                        }
                    }
                }

                // Age Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Age: $age years old",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, color = ParentNavy)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(4, 5, 6, 7, 8, 9, 10).forEach { a ->
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (age == a) ParentAccent else Color(0xFFF1F5F9))
                                    .clickable { age = a },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$a",
                                    color = if (age == a) Color.White else ParentNavy,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                // Daily Learning Goal Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Daily Goal: $goalMinutes mins",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, color = ParentNavy)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(15, 20, 30, 45).forEach { g ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (goalMinutes == g) PlayfulGreen else Color(0xFFF1F5F9))
                                    .clickable { goalMinutes = g }
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${g}m",
                                    color = if (goalMinutes == g) Color.White else ParentNavy,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val grade = when {
                            age <= 4 -> "Preschool"
                            age == 5 -> "Kindergarten"
                            age == 6 -> "1st Grade"
                            age == 7 -> "2nd Grade"
                            age == 8 -> "3rd Grade"
                            else -> "4th Grade"
                        }
                        onConfirm(name.trim(), age, grade, selectedAvatarId, goalMinutes)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ParentAccent),
                shape = RoundedCornerShape(12.dp),
                enabled = name.isNotBlank(),
                modifier = Modifier.testTag("confirm_child_form_button")
            ) {
                Text("Save Profile", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White
    )
}
