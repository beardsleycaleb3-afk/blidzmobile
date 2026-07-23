package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_runs")
data class GameRun(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val yards: Int,
    val xp: Int,
    val coins: Int,
    val maxMultiplier: Float,
    val isFlawless: Boolean
)

@Entity(tableName = "user_equipment")
data class UserEquipment(
    @PrimaryKey val id: Int = 1,
    val helmetLevel: Int = 1,
    val padsLevel: Int = 1,
    val cleatsLevel: Int = 1,
    val glovesLevel: Int = 1,
    val ballLevel: Int = 1,
    val totalGoldFootballs: Int = 0,
    val totalCareerYards: Long = 0,
    val totalCareerXP: Long = 0
)
