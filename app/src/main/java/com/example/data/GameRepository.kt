package com.example.data

import kotlinx.coroutines.flow.Flow

class GameRepository(private val gameDao: GameDao) {
    val allRuns: Flow<List<GameRun>> = gameDao.getAllRunsByXp()
    val recentRuns: Flow<List<GameRun>> = gameDao.getRecentRuns()
    val userEquipment: Flow<UserEquipment?> = gameDao.getUserEquipment()

    suspend fun recordRun(
        yards: Int,
        xp: Int,
        coins: Int,
        maxMultiplier: Float,
        isFlawless: Boolean,
        currentEquipment: UserEquipment
    ) {
        val run = GameRun(
            yards = yards,
            xp = xp,
            coins = coins,
            maxMultiplier = maxMultiplier,
            isFlawless = isFlawless
        )
        gameDao.insertRun(run)

        // Update Career stats & coins
        val updatedEquipment = currentEquipment.copy(
            totalGoldFootballs = currentEquipment.totalGoldFootballs + coins,
            totalCareerYards = currentEquipment.totalCareerYards + yards,
            totalCareerXP = currentEquipment.totalCareerXP + xp
        )
        gameDao.saveUserEquipment(updatedEquipment)
    }

    suspend fun saveEquipment(equipment: UserEquipment) {
        gameDao.saveUserEquipment(equipment)
    }
}
