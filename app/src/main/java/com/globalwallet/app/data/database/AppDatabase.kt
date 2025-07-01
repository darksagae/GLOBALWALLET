package com.globalwallet.app.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.globalwallet.app.data.model.*
import com.globalwallet.app.data.database.converters.DateTimeConverters

@Database(
    entities = [
        Wallet::class,
        Balance::class,
        Transaction::class,
        User::class,
        MiningSession::class,
        EarnActivity::class,
        Referral::class,
        SocialFiPost::class,
        Community::class,
        UserCommunity::class,
        DApp::class,
        UserBadge::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTimeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun walletDao(): WalletDao
    abstract fun balanceDao(): BalanceDao
    abstract fun transactionDao(): TransactionDao
    abstract fun userDao(): UserDao
    abstract fun miningSessionDao(): MiningSessionDao
    abstract fun earnActivityDao(): EarnActivityDao
    abstract fun referralDao(): ReferralDao
    abstract fun socialFiPostDao(): SocialFiPostDao
    abstract fun communityDao(): CommunityDao
    abstract fun userCommunityDao(): UserCommunityDao
    abstract fun dAppDao(): DAppDao
    abstract fun userBadgeDao(): UserBadgeDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "global_wallet_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 