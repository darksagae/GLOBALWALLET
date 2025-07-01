package com.globalwallet.app.data.database.dao

import androidx.room.*
import com.globalwallet.app.data.model.DApp
import kotlinx.coroutines.flow.Flow

@Dao
interface DAppDao {
    
    @Query("SELECT * FROM dapps WHERE isActive = 1")
    fun getAllActiveDApps(): Flow<List<DApp>>
    
    @Query("SELECT * FROM dapps WHERE chain = :chain AND isActive = 1")
    fun getDAppsByChain(chain: String): Flow<List<DApp>>
    
    @Query("SELECT * FROM dapps WHERE category = :category AND isActive = 1")
    fun getDAppsByCategory(category: String): Flow<List<DApp>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDApp(dApp: DApp)
    
    @Update
    suspend fun updateDApp(dApp: DApp)
    
    @Delete
    suspend fun deleteDApp(dApp: DApp)
    
    @Query("UPDATE dapps SET isActive = :isActive WHERE id = :dAppId")
    suspend fun updateDAppStatus(dAppId: String, isActive: Boolean)
} 