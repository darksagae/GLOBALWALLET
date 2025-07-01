package com.globalwallet.app.data.database.dao

import androidx.room.*
import com.globalwallet.app.data.model.Balance
import kotlinx.coroutines.flow.Flow

@Dao
interface BalanceDao {
    
    @Query("SELECT * FROM balances WHERE walletId = :walletId")
    fun getBalancesByWalletId(walletId: String): Flow<List<Balance>>
    
    @Query("SELECT * FROM balances WHERE chain = :chain")
    fun getBalancesByChain(chain: String): Flow<List<Balance>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBalance(balance: Balance)
    
    @Update
    suspend fun updateBalance(balance: Balance)
    
    @Delete
    suspend fun deleteBalance(balance: Balance)
    
    @Query("DELETE FROM balances WHERE walletId = :walletId")
    suspend fun deleteBalancesByWalletId(walletId: String)
} 