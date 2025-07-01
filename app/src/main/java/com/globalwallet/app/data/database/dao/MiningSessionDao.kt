package com.globalwallet.app.data.database.dao

import androidx.room.*
import com.globalwallet.app.data.model.MiningSession
import kotlinx.coroutines.flow.Flow

@Dao
interface MiningSessionDao {
    
    @Query("SELECT * FROM mining_sessions WHERE userId = :userId ORDER BY startTime DESC LIMIT 1")
    suspend fun getLatestMiningSession(userId: String): MiningSession?
    
    @Query("SELECT * FROM mining_sessions WHERE userId = :userId ORDER BY startTime DESC")
    fun getMiningSessionsByUserId(userId: String): Flow<List<MiningSession>>
    
    @Query("SELECT COUNT(*) FROM mining_sessions WHERE userId = :userId AND isCompleted = 1")
    suspend fun getCompletedMiningSessionsCount(userId: String): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMiningSession(miningSession: MiningSession)
    
    @Update
    suspend fun updateMiningSession(miningSession: MiningSession)
    
    @Delete
    suspend fun deleteMiningSession(miningSession: MiningSession)
    
    @Query("DELETE FROM mining_sessions WHERE userId = :userId")
    suspend fun deleteMiningSessionsByUserId(userId: String)
} 