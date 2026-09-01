package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Badge
import com.example.data.model.ChildProfile
import com.example.data.model.DailyMission
import com.example.data.model.GameSession
import com.example.data.model.ParentSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface KidsDao {
    // Child Profiles
    @Query("SELECT * FROM child_profiles ORDER BY id ASC")
    fun getAllChildren(): Flow<List<ChildProfile>>

    @Query("SELECT * FROM child_profiles WHERE isCurrentActive = 1 LIMIT 1")
    fun getActiveChild(): Flow<ChildProfile?>

    @Query("SELECT * FROM child_profiles WHERE id = :id")
    suspend fun getChildById(id: Long): ChildProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChild(child: ChildProfile): Long

    @Update
    suspend fun updateChild(child: ChildProfile)

    @Query("UPDATE child_profiles SET isCurrentActive = 0")
    suspend fun clearActiveChildren()

    @Query("UPDATE child_profiles SET isCurrentActive = 1 WHERE id = :id")
    suspend fun setActiveChild(id: Long)

    @Query("DELETE FROM child_profiles WHERE id = :id")
    suspend fun deleteChild(id: Long)

    @Query("UPDATE child_profiles SET totalStars = totalStars + :stars, todayPlayedMinutes = todayPlayedMinutes + :minutesToAdd, lastPlayedTimestamp = :timestamp WHERE id = :childId")
    suspend fun incrementChildStarsAndPlaytime(childId: Long, stars: Int, minutesToAdd: Int, timestamp: Long)

    // Game Sessions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameSession(session: GameSession): Long

    @Query("SELECT * FROM game_sessions WHERE childId = :childId ORDER BY timestamp DESC")
    fun getSessionsForChild(childId: Long): Flow<List<GameSession>>

    @Query("SELECT * FROM game_sessions ORDER BY timestamp DESC LIMIT 50")
    fun getAllRecentSessions(): Flow<List<GameSession>>

    @Query("SELECT * FROM game_sessions WHERE childId = :childId AND gameCategory = :category")
    suspend fun getSessionsByChildAndCategory(childId: Long, category: String): List<GameSession>

    // Badges
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadges(badges: List<Badge>)

    @Query("SELECT * FROM badges WHERE childId = :childId ORDER BY id ASC")
    fun getBadgesForChild(childId: Long): Flow<List<Badge>>

    @Query("UPDATE badges SET isUnlocked = 1, unlockedAtTimestamp = :timestamp WHERE childId = :childId AND badgeCode = :badgeCode")
    suspend fun unlockBadge(childId: Long, badgeCode: String, timestamp: Long)

    // Daily Missions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyMissions(missions: List<DailyMission>)

    @Query("SELECT * FROM daily_missions WHERE childId = :childId AND dateEpochDay = :dateEpochDay ORDER BY id ASC")
    fun getDailyMissionsForChild(childId: Long, dateEpochDay: Long): Flow<List<DailyMission>>

    @Query("SELECT * FROM daily_missions WHERE childId = :childId AND dateEpochDay = :dateEpochDay ORDER BY id ASC")
    suspend fun getDailyMissionsDirect(childId: Long, dateEpochDay: Long): List<DailyMission>

    @Query("SELECT * FROM daily_missions WHERE childId = :childId ORDER BY id DESC")
    fun getAllMissionsForChild(childId: Long): Flow<List<DailyMission>>

    @Update
    suspend fun updateDailyMission(mission: DailyMission)

    @Query("UPDATE daily_missions SET currentProgress = :progress, isCompleted = :isCompleted WHERE id = :missionId")
    suspend fun updateMissionProgress(missionId: Long, progress: Int, isCompleted: Boolean)

    @Query("UPDATE daily_missions SET isRewardClaimed = 1 WHERE id = :missionId")
    suspend fun markMissionRewardClaimed(missionId: Long)

    // Parent Settings
    @Query("SELECT * FROM parent_settings WHERE id = 1")
    fun getParentSettings(): Flow<ParentSettings?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveParentSettings(settings: ParentSettings)
}
