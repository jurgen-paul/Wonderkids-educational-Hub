package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.model.GameCategory
import com.example.data.repository.KidsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `test app name string is WonderKids`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("WonderKids", appName)
    }

    @Test
    fun `test repository adds and retrieves child profiles and records game completion`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = AppDatabase.getDatabase(context)
        val dao = db.kidsDao()
        val repository = KidsRepository(dao)

        AppDatabase.populateInitialData(dao)

        val children = repository.allChildren.first()
        assertTrue("Children list should not be empty after initial seeding", children.isNotEmpty())

        val activeChild = repository.activeChild.first()
        assertNotNull("Active child should be present", activeChild)

        // Record a math game
        repository.recordGameCompletion(
            childId = activeChild!!.id,
            gameCategory = GameCategory.MATH,
            gameTitle = "Math Blast",
            score = 100,
            totalQuestions = 5,
            correctAnswers = 5,
            durationSeconds = 60,
            starsEarned = 5
        )

        val masteries = repository.computeSubjectMastery(activeChild.id)
        val mathMastery = masteries.find { it.category == GameCategory.MATH }
        assertNotNull("Math mastery should be computed", mathMastery)
        assertTrue("Math played should be >= 1", (mathMastery?.totalPlayed ?: 0) >= 1)
    }

    @Test
    fun `test daily missions lifecycle and badge rewards`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = AppDatabase.getDatabase(context)
        val dao = db.kidsDao()
        val repository = KidsRepository(dao)

        AppDatabase.populateInitialData(dao)

        val activeChild = repository.activeChild.first()
        assertNotNull("Active child should be present", activeChild)
        val childId = activeChild!!.id

        repository.ensureDailyMissions(childId)
        val missions = repository.getDailyMissionsForChild(childId).first()
        assertTrue("Should have daily missions seeded", missions.isNotEmpty())

        val mathMission = missions.find { it.category == "MATH" }
        assertNotNull("Should have a Math daily mission", mathMission)

        // Complete a math game session to complete the math mission
        repository.recordGameCompletion(
            childId = childId,
            gameCategory = GameCategory.MATH,
            gameTitle = "Math Galaxy Daily",
            score = 100,
            totalQuestions = 5,
            correctAnswers = 5,
            durationSeconds = 90,
            starsEarned = 5
        )

        val updatedMissions = repository.getDailyMissionsForChild(childId).first()
        val updatedMathMission = updatedMissions.find { it.id == mathMission!!.id }
        assertNotNull(updatedMathMission)
        assertTrue("Math mission should be completed", updatedMathMission!!.isCompleted)

        // Claim reward
        val starsBefore = repository.allChildren.first().find { it.id == childId }?.totalStars ?: 0
        val claimed = repository.claimMissionReward(childId, updatedMathMission.id)
        assertNotNull(claimed)
        assertTrue("Mission should be claimed", claimed!!.isRewardClaimed)

        val starsAfter = repository.allChildren.first().find { it.id == childId }?.totalStars ?: 0
        assertEquals(starsBefore + updatedMathMission.rewardStars, starsAfter)

        // Check badge unlocked
        val badges = repository.getBadgesForChild(childId).first()
        val unlockedRewardBadge = badges.find { it.badgeCode == updatedMathMission.rewardBadgeCode }
        assertNotNull(unlockedRewardBadge)
        assertTrue("Mission badge should be unlocked", unlockedRewardBadge!!.isUnlocked)
    }

    @Test
    fun `test mini games track progress and update parent dashboard metrics`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = AppDatabase.getDatabase(context)
        val dao = db.kidsDao()
        val repository = KidsRepository(dao)

        AppDatabase.populateInitialData(dao)

        val activeChild = repository.activeChild.first()
        assertNotNull(activeChild)
        val childId = activeChild!!.id

        // Record a Memory Match session
        repository.recordGameCompletion(
            childId = childId,
            gameCategory = GameCategory.MEMORY,
            gameTitle = "Memory Safari",
            score = 90,
            totalQuestions = 6,
            correctAnswers = 6,
            durationSeconds = 120,
            starsEarned = 3
        )

        // Record a Math Puzzle session
        repository.recordGameCompletion(
            childId = childId,
            gameCategory = GameCategory.MATH,
            gameTitle = "Math Galaxy",
            score = 100,
            totalQuestions = 10,
            correctAnswers = 10,
            durationSeconds = 180,
            starsEarned = 5
        )

        // 1. Verify Parent Dashboard session history is updated
        val recentSessions = repository.getSessionsForChild(childId).first()
        assertTrue("Parent dashboard should have recorded sessions", recentSessions.size >= 2)
        val memorySession = recentSessions.find { it.gameCategory == GameCategory.MEMORY.name }
        val mathSession = recentSessions.find { it.gameCategory == GameCategory.MATH.name }
        assertNotNull("Memory session found in parent log", memorySession)
        assertNotNull("Math session found in parent log", mathSession)
        assertEquals(90, memorySession?.score)
        assertEquals(100, mathSession?.score)

        // 2. Verify Subject Mastery metrics for Parent Dashboard
        val masteries = repository.computeSubjectMastery(childId)
        val memoryMastery = masteries.find { it.category == GameCategory.MEMORY }
        val mathMastery = masteries.find { it.category == GameCategory.MATH }
        assertNotNull(memoryMastery)
        assertNotNull(mathMastery)
        assertTrue("Memory total played updated", (memoryMastery?.totalPlayed ?: 0) >= 1)
        assertTrue("Math total played updated", (mathMastery?.totalPlayed ?: 0) >= 1)

        // 3. Verify Child screen time updated for Parent controls
        val updatedChild = repository.allChildren.first().find { it.id == childId }
        assertNotNull(updatedChild)
        assertTrue("Today played minutes should be tracked", updatedChild!!.todayPlayedMinutes >= 5)
    }
}
