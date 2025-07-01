package com.globalwallet.app.data.database.dao

import androidx.room.*
import com.globalwallet.app.data.model.Wallet
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {
    
    @Query("SELECT * FROM wallets WHERE isActive = 1")
    fun getAllActiveWallets(): Flow<List<Wallet>>
    
    @Query("SELECT * FROM wallets WHERE chain = :chain AND isActive = 1")
    suspend fun getWalletByChain(chain: String): Wallet?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallet(wallet: Wallet)
    
    @Update
    suspend fun updateWallet(wallet: Wallet)
    
    @Delete
    suspend fun deleteWallet(wallet: Wallet)
    
    @Query("DELETE FROM wallets WHERE id = :walletId")
    suspend fun deleteWalletById(walletId: String)
} 