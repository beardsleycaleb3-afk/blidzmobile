package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM game_runs ORDER BY xp DESC")
    fun getAllRunsByXp(): Flow<List<GameRun>>

    @Query("SELECT * FROM game_runs ORDER BY timestamp DESC LIMIT 10")
    fun getRecentRuns(): Flow<List<GameRun>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: GameRun)

    @Query("SELECT * FROM user_equipment WHERE id = 1")
    fun getUserEquipment(): Flow<UserEquipment?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserEquipment(equipment: UserEquipment)
}
