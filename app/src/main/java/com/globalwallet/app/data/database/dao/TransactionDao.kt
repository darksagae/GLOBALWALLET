package com.globalwallet.app.data.database.dao

import androidx.room.*
import com.globalwallet.app.data.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    
    @Query("SELECT * FROM transactions WHERE walletId = :walletId ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentTransactions(walletId: String, limit: Int = 10): Flow<List<Transaction>>
    
    @Query("SELECT * FROM transactions WHERE chain = :chain ORDER BY timestamp DESC")
    fun getTransactionsByChain(chain: String): Flow<List<Transaction>>
    
    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY timestamp DESC")
    fun getTransactionsByType(type: String): Flow<List<Transaction>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)
    
    @Update
    suspend fun updateTransaction(transaction: Transaction)
    
    @Delete
    suspend fun deleteTransaction(transaction: Transaction)
    
    @Query("DELETE FROM transactions WHERE walletId = :walletId")
    suspend fun deleteTransactionsByWalletId(walletId: String)
} 