package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.GameRepository
import com.example.data.GameRun
import com.example.data.UserEquipment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import kotlinx.coroutines.flow.map

enum class AppTab {
    RUNNER,
    LOCKER_ROOM,
    CAREER_TROPHIES
}

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: GameRepository

    val allRuns: StateFlow<List<GameRun>>
    val userEquipment: StateFlow<UserEquipment>

    private val _currentTab = MutableStateFlow(AppTab.RUNNER)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _lastRunSummary = MutableStateFlow<GameRun?>(null)
    val lastRunSummary: StateFlow<GameRun?> = _lastRunSummary.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = GameRepository(db.gameDao())

        allRuns = repository.allRuns.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        val defaultEquipment = UserEquipment()
        userEquipment = repository.userEquipment
            .map { it ?: defaultEquipment }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = defaultEquipment
            )

        // Ensure default equipment row exists
        viewModelScope.launch {
            repository.userEquipment.collect { eq ->
                if (eq == null) {
                    repository.saveEquipment(defaultEquipment)
                }
            }
        }
    }

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun onGameRunFinished(yards: Int, xp: Int, coins: Int, maxMultiplier: Float, isFlawless: Boolean) {
        viewModelScope.launch {
            val currentEq = userEquipment.value
            repository.recordRun(yards, xp, coins, maxMultiplier, isFlawless, currentEq)
            _lastRunSummary.value = GameRun(
                yards = yards,
                xp = xp,
                coins = coins,
                maxMultiplier = maxMultiplier,
                isFlawless = isFlawless
            )
        }
    }

    fun upgradeGear(gearType: String): Boolean {
        val currentEq = userEquipment.value
        val cost = 25
        if (currentEq.totalGoldFootballs < cost) return false

        val updated = when (gearType) {
            "HELMET" -> if (currentEq.helmetLevel < 3) currentEq.copy(helmetLevel = currentEq.helmetLevel + 1, totalGoldFootballs = currentEq.totalGoldFootballs - cost) else return false
            "PADS" -> if (currentEq.padsLevel < 3) currentEq.copy(padsLevel = currentEq.padsLevel + 1, totalGoldFootballs = currentEq.totalGoldFootballs - cost) else return false
            "CLEATS" -> if (currentEq.cleatsLevel < 3) currentEq.copy(cleatsLevel = currentEq.cleatsLevel + 1, totalGoldFootballs = currentEq.totalGoldFootballs - cost) else return false
            "GLOVES" -> if (currentEq.glovesLevel < 3) currentEq.copy(glovesLevel = currentEq.glovesLevel + 1, totalGoldFootballs = currentEq.totalGoldFootballs - cost) else return false
            "BALL" -> if (currentEq.ballLevel < 3) currentEq.copy(ballLevel = currentEq.ballLevel + 1, totalGoldFootballs = currentEq.totalGoldFootballs - cost) else return false
            else -> return false
        }

        viewModelScope.launch {
            repository.saveEquipment(updated)
        }
        return true
    }
}
