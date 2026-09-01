package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.data.local.AppDatabase
import com.example.data.model.Badge
import com.example.data.model.ChildProfile
import com.example.data.model.DailyMission
import com.example.data.model.GameCategory
import com.example.data.model.GameSession
import com.example.data.model.ParentSettings
import com.example.data.model.SubjectMastery
import com.example.data.repository.KidsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class KidsViewModel(
    application: Application,
    private val repository: KidsRepository
) : AndroidViewModel(application) {

    val allChildren: StateFlow<List<ChildProfile>> = repository.allChildren
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeChild: StateFlow<ChildProfile?> = repository.activeChild
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val parentSettings: StateFlow<ParentSettings> = repository.parentSettings
        .flatMapLatest { settings ->
            flowOf(settings ?: ParentSettings())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ParentSettings())

    val recentSessions: StateFlow<List<GameSession>> = repository.allRecentSessions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val badges: StateFlow<List<Badge>> = activeChild
        .flatMapLatest { child ->
            if (child != null) repository.getBadgesForChild(child.id)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dailyMissions: StateFlow<List<DailyMission>> = activeChild
        .flatMapLatest { child ->
            if (child != null) repository.getDailyMissionsForChild(child.id)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeChildSessions: StateFlow<List<GameSession>> = activeChild
        .flatMapLatest { child ->
            if (child != null) repository.getSessionsForChild(child.id)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _subjectMasteryList = MutableStateFlow<List<SubjectMastery>>(emptyList())
    val subjectMasteryList: StateFlow<List<SubjectMastery>> = _subjectMasteryList.asStateFlow()

    private val _rewardCelebrationMission = MutableStateFlow<DailyMission?>(null)
    val rewardCelebrationMission: StateFlow<DailyMission?> = _rewardCelebrationMission.asStateFlow()

    init {
        viewModelScope.launch {
            activeChild.collect { child ->
                if (child != null) {
                    repository.ensureDailyMissions(child.id)
                    refreshMastery(child.id)
                }
            }
        }
    }

    fun refreshMastery(childId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _subjectMasteryList.value = repository.computeSubjectMastery(childId)
        }
    }

    fun selectChild(childId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setActiveChild(childId)
            refreshMastery(childId)
        }
    }

    fun claimMissionReward(mission: DailyMission) {
        val child = activeChild.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val claimed = repository.claimMissionReward(child.id, mission.id)
            if (claimed != null) {
                _rewardCelebrationMission.value = claimed
            }
        }
    }

    fun dismissRewardCelebration() {
        _rewardCelebrationMission.value = null
    }

    fun recordGameSession(
        category: GameCategory,
        gameTitle: String,
        score: Int,
        totalQuestions: Int,
        correctAnswers: Int,
        durationSeconds: Int,
        starsEarned: Int
    ) {
        val child = activeChild.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            repository.recordGameCompletion(
                childId = child.id,
                gameCategory = category,
                gameTitle = gameTitle,
                score = score,
                totalQuestions = totalQuestions,
                correctAnswers = correctAnswers,
                durationSeconds = durationSeconds,
                starsEarned = starsEarned
            )
            refreshMastery(child.id)
        }
    }

    fun addChild(
        name: String,
        age: Int,
        gradeLevel: String,
        avatarId: String,
        dailyGoalMinutes: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val newId = repository.addChild(
                ChildProfile(
                    name = name.trim(),
                    age = age,
                    avatarId = avatarId,
                    gradeLevel = gradeLevel,
                    totalStars = 0,
                    currentStreakDays = 1,
                    lastPlayedTimestamp = System.currentTimeMillis(),
                    todayPlayedMinutes = 0,
                    dailyGoalMinutes = dailyGoalMinutes,
                    isCurrentActive = false
                )
            )
            if (activeChild.value == null) {
                repository.setActiveChild(newId)
            }
        }
    }

    fun updateChild(child: ChildProfile) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateChild(child)
        }
    }

    fun deleteChild(childId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteChild(childId)
        }
    }

    fun updateParentSettings(settings: ParentSettings) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveParentSettings(settings)
        }
    }

    fun resetTodayScreenTime() {
        val child = activeChild.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateChild(child.copy(todayPlayedMinutes = 0))
        }
    }

    fun resetAllProgress() {
        val child = activeChild.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            refreshMastery(child.id)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as Application
                val database = AppDatabase.getDatabase(application)
                val repository = KidsRepository(database.kidsDao())
                KidsViewModel(application, repository)
            }
        }
    }
}
