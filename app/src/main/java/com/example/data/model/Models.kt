package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class GameCategory(val displayName: String, val icon: String, val tagColorHex: Long) {
    MATH("Math Galaxy", "rocket", 0xFF4F46E5),
    SPELLING("Word Explorer", "book", 0xFF0288D1),
    MEMORY("Memory Safari", "puzzle", 0xFF009688),
    SCIENCE("Science & Trivia", "telescope", 0xFF8E24AA),
    SHAPES("Color & Shapes", "shapes", 0xFFFF8F00)
}

enum class AgeGroup(val title: String, val ageRange: String, val defaultGoalMinutes: Int) {
    TODDLER("Little Star", "Ages 3 - 5", 15),
    EXPLORER("Curious Explorer", "Ages 6 - 8", 25),
    CHAMPION("Brain Champion", "Ages 9 - 11", 35)
}

@Entity(tableName = "child_profiles")
data class ChildProfile(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val age: Int,
    val avatarId: String, // "owl", "fox", "lion", "panda", "astronaut", "dino", "unicorn", "robot"
    val gradeLevel: String,
    val totalStars: Int = 0,
    val currentStreakDays: Int = 1,
    val lastPlayedTimestamp: Long = System.currentTimeMillis(),
    val todayPlayedMinutes: Int = 0,
    val dailyGoalMinutes: Int = 20,
    val isCurrentActive: Boolean = false
)

@Entity(tableName = "game_sessions")
data class GameSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childId: Long,
    val gameCategory: String, // from GameCategory.name
    val gameTitle: String,
    val score: Int,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val durationSeconds: Int,
    val starsEarned: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "badges")
data class Badge(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childId: Long,
    val badgeCode: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val category: String,
    val isUnlocked: Boolean = false,
    val unlockedAtTimestamp: Long? = null
)

@Entity(tableName = "daily_missions")
data class DailyMission(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childId: Long,
    val missionCode: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val category: String, // "MATH", "SPELLING", "MEMORY", "SCIENCE", "SHAPES", "ALL"
    val targetCount: Int = 1,
    val currentProgress: Int = 0,
    val isCompleted: Boolean = false,
    val isRewardClaimed: Boolean = false,
    val rewardBadgeCode: String,
    val rewardBadgeTitle: String,
    val rewardBadgeIcon: String,
    val rewardBadgeDescription: String,
    val rewardStars: Int = 5,
    val dateEpochDay: Long = 0
)

@Entity(tableName = "parent_settings")
data class ParentSettings(
    @PrimaryKey val id: Int = 1,
    val parentName: String = "Sarah Jenkins",
    val parentEmail: String = "sarah.j@example.com",
    val pinCode: String = "1234",
    val dailyScreenLimitMinutes: Int = 45,
    val soundEffectsEnabled: Boolean = true,
    val backgroundMusicEnabled: Boolean = true,
    val mathGateEnabled: Boolean = true,
    val bedtimeReminderEnabled: Boolean = true,
    val bedtimeHour: Int = 20,
    val bedtimeMinute: Int = 0
)

data class SubjectMastery(
    val category: GameCategory,
    val totalPlayed: Int,
    val accuracyPercentage: Int,
    val starsWon: Int,
    val recommendedLevel: String
)
