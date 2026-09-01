package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.KidsDao
import com.example.data.model.Badge
import com.example.data.model.ChildProfile
import com.example.data.model.DailyMission
import com.example.data.model.GameCategory
import com.example.data.model.GameSession
import com.example.data.model.ParentSettings
import com.example.data.model.SubjectMastery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class KidsRepository(private val dao: KidsDao) {

    val allChildren: Flow<List<ChildProfile>> = dao.getAllChildren()
    val activeChild: Flow<ChildProfile?> = dao.getActiveChild()
    val allRecentSessions: Flow<List<GameSession>> = dao.getAllRecentSessions()
    val parentSettings: Flow<ParentSettings?> = dao.getParentSettings()

    fun getTodayEpochDay(): Long = System.currentTimeMillis() / (1000L * 60 * 60 * 24)

    fun getSessionsForChild(childId: Long): Flow<List<GameSession>> =
        dao.getSessionsForChild(childId)

    fun getBadgesForChild(childId: Long): Flow<List<Badge>> =
        dao.getBadgesForChild(childId)

    fun getDailyMissionsForChild(childId: Long): Flow<List<DailyMission>> {
        return dao.getDailyMissionsForChild(childId, getTodayEpochDay())
    }

    suspend fun ensureDailyMissions(childId: Long) {
        AppDatabase.seedDailyMissionsForChild(dao, childId, getTodayEpochDay())
    }

    suspend fun setActiveChild(childId: Long) {
        dao.clearActiveChildren()
        dao.setActiveChild(childId)
        ensureDailyMissions(childId)
    }

    suspend fun addChild(child: ChildProfile): Long {
        val newId = dao.insertChild(child)
        // Seed badges for the new child
        AppDatabase.seedBadgesForChild(dao, newId)
        // Seed daily missions for the new child
        AppDatabase.seedDailyMissionsForChild(dao, newId, getTodayEpochDay())
        return newId
    }

    suspend fun updateChild(child: ChildProfile) {
        dao.updateChild(child)
    }

    suspend fun deleteChild(childId: Long) {
        dao.deleteChild(childId)
        val children = dao.getAllChildren().firstOrNull() ?: emptyList()
        if (children.isNotEmpty() && children.none { it.isCurrentActive }) {
            dao.setActiveChild(children.first().id)
        }
    }

    suspend fun saveParentSettings(settings: ParentSettings) {
        dao.saveParentSettings(settings)
    }

    suspend fun recordGameCompletion(
        childId: Long,
        gameCategory: GameCategory,
        gameTitle: String,
        score: Int,
        totalQuestions: Int,
        correctAnswers: Int,
        durationSeconds: Int,
        starsEarned: Int
    ) {
        val now = System.currentTimeMillis()
        val minutesSpent = maxOf(1, durationSeconds / 60)

        // Insert session record
        dao.insertGameSession(
            GameSession(
                childId = childId,
                gameCategory = gameCategory.name,
                gameTitle = gameTitle,
                score = score,
                totalQuestions = totalQuestions,
                correctAnswers = correctAnswers,
                durationSeconds = durationSeconds,
                starsEarned = starsEarned,
                timestamp = now
            )
        )

        // Increment stars and playtime
        dao.incrementChildStarsAndPlaytime(
            childId = childId,
            stars = starsEarned,
            minutesToAdd = minutesSpent,
            timestamp = now
        )

        // Update Daily Missions progress
        val todayEpochDay = getTodayEpochDay()
        ensureDailyMissions(childId)
        val activeMissions = dao.getDailyMissionsDirect(childId, todayEpochDay)
        for (mission in activeMissions) {
            val matchesCategory = mission.category.equals(gameCategory.name, ignoreCase = true) ||
                    mission.category.equals("ALL", ignoreCase = true)
            if (matchesCategory && !mission.isCompleted) {
                val newProgress = (mission.currentProgress + 1).coerceAtMost(mission.targetCount)
                val isNowCompleted = newProgress >= mission.targetCount
                dao.updateMissionProgress(
                    missionId = mission.id,
                    progress = newProgress,
                    isCompleted = isNowCompleted
                )
            }
        }

        // Check if all today's missions are completed
        val updatedMissions = dao.getDailyMissionsDirect(childId, todayEpochDay)
        if (updatedMissions.isNotEmpty() && updatedMissions.all { it.isCompleted }) {
            dao.unlockBadge(childId, "MISSION_CHAMPION", now)
        }

        // Check badge unlocks
        dao.unlockBadge(childId, "FIRST_GAME", now)
        if (score == 100 && gameCategory == GameCategory.MATH) {
            dao.unlockBadge(childId, "MATH_WIZARD", now)
        }
        if (gameCategory == GameCategory.SPELLING && correctAnswers >= 4) {
            dao.unlockBadge(childId, "WORD_MASTER", now)
        }
        if (gameCategory == GameCategory.MEMORY && score == 100) {
            dao.unlockBadge(childId, "MEMORY_GENIUS", now)
        }
        if (gameCategory == GameCategory.SCIENCE) {
            dao.unlockBadge(childId, "SCIENCE_EXPLORER", now)
        }
        if (gameCategory == GameCategory.SHAPES && score == 100) {
            dao.unlockBadge(childId, "SHAPE_ARTIST", now)
        }

        val child = dao.getChildById(childId)
        if (child != null) {
            if (child.totalStars >= 50) {
                dao.unlockBadge(childId, "STAR_50", now)
            }
            if (child.totalStars >= 100) {
                dao.unlockBadge(childId, "STAR_100", now)
            }
        }
    }

    suspend fun claimMissionReward(childId: Long, missionId: Long): DailyMission? {
        val now = System.currentTimeMillis()
        val todayEpochDay = getTodayEpochDay()
        val missions = dao.getDailyMissionsDirect(childId, todayEpochDay)
        val mission = missions.find { it.id == missionId } ?: return null

        if (!mission.isRewardClaimed) {
            // Mark claimed
            dao.markMissionRewardClaimed(missionId)

            // Award reward stars
            dao.incrementChildStarsAndPlaytime(
                childId = childId,
                stars = mission.rewardStars,
                minutesToAdd = 0,
                timestamp = now
            )

            // Unlock the corresponding virtual badge
            dao.unlockBadge(childId, mission.rewardBadgeCode, now)

            // Check if all missions are completed, unlock all-star hero
            val refreshed = dao.getDailyMissionsDirect(childId, todayEpochDay)
            if (refreshed.isNotEmpty() && refreshed.all { it.isCompleted }) {
                dao.unlockBadge(childId, "MISSION_CHAMPION", now)
            }

            return mission.copy(isRewardClaimed = true)
        }
        return mission
    }

    suspend fun computeSubjectMastery(childId: Long): List<SubjectMastery> {
        val list = mutableListOf<SubjectMastery>()
        for (cat in GameCategory.values()) {
            val sessions = dao.getSessionsByChildAndCategory(childId, cat.name)
            val totalPlayed = sessions.size
            val totalQuestions = sessions.sumOf { it.totalQuestions }
            val correct = sessions.sumOf { it.correctAnswers }
            val accuracy = if (totalQuestions > 0) (correct * 100) / totalQuestions else 85
            val starsWon = sessions.sumOf { it.starsEarned }
            val level = when {
                totalPlayed >= 5 && accuracy >= 80 -> "Mastery Level 3 ⭐⭐⭐"
                totalPlayed >= 2 && accuracy >= 60 -> "Explorer Level 2 ⭐⭐"
                else -> "Starter Level 1 ⭐"
            }
            list.add(
                SubjectMastery(
                    category = cat,
                    totalPlayed = totalPlayed,
                    accuracyPercentage = accuracy,
                    starsWon = starsWon,
                    recommendedLevel = level
                )
            )
        }
        return list
    }
}
