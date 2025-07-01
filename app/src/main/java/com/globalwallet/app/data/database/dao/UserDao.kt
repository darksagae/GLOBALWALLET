package com.globalwallet.app.data.database.dao

import androidx.room.*
import com.globalwallet.app.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): User?
    
    @Query("SELECT * FROM users WHERE referralCode = :referralCode")
    suspend fun getUserByReferralCode(referralCode: String): User?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
    
    @Update
    suspend fun updateUser(user: User)
    
    @Delete
    suspend fun deleteUser(user: User)
    
    @Query("UPDATE users SET totalEarned = totalEarned + :amount WHERE id = :userId")
    suspend fun addToTotalEarned(userId: String, amount: java.math.BigDecimal)
    
    @Query("UPDATE users SET totalMined = totalMined + :amount WHERE id = :userId")
    suspend fun addToTotalMined(userId: String, amount: java.math.BigDecimal)
    
    @Query("UPDATE users SET totalReferrals = totalReferrals + 1 WHERE id = :userId")
    suspend fun incrementTotalReferrals(userId: String)
} 