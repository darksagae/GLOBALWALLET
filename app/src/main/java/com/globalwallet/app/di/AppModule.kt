package com.globalwallet.app.di

import android.content.Context
import androidx.room.Room
import com.globalwallet.app.data.database.AppDatabase
import com.globalwallet.app.data.repository.FirebaseRepository
import com.globalwallet.app.data.repository.UserRepository
import com.globalwallet.app.data.repository.WalletRepository
import com.globalwallet.app.data.repository.TradeRepository
import com.globalwallet.app.data.repository.EarnRepository
import com.globalwallet.app.data.repository.MiningRepository
import com.globalwallet.app.data.repository.SocialFiRepository
import com.globalwallet.app.data.repository.DiscoverRepository
import com.globalwallet.app.data.api.ApiService
import com.globalwallet.app.data.api.CoinGeckoApi
import com.globalwallet.app.data.network.NetworkManager
import com.globalwallet.app.data.security.SecurityManager
import com.globalwallet.app.data.wallet.WalletManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "global_wallet_db"
        ).build()
    }
    
    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.coingecko.com/api/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideCoinGeckoApi(apiService: ApiService): CoinGeckoApi {
        return CoinGeckoApi(apiService)
    }
    
    @Provides
    @Singleton
    fun provideNetworkManager(@ApplicationContext context: Context): NetworkManager {
        return NetworkManager(context)
    }
    
    @Provides
    @Singleton
    fun provideSecurityManager(@ApplicationContext context: Context): SecurityManager {
        return SecurityManager(context)
    }
    
    @Provides
    @Singleton
    fun provideWalletManager(
        securityManager: SecurityManager,
        networkManager: NetworkManager
    ): WalletManager {
        return WalletManager(securityManager, networkManager)
    }
    
    @Provides
    @Singleton
    fun provideUserRepository(
        database: AppDatabase,
        firebaseRepository: FirebaseRepository
    ): UserRepository {
        return UserRepository(database.userDao(), firebaseRepository)
    }
    
    @Provides
    @Singleton
    fun provideWalletRepository(
        database: AppDatabase,
        walletManager: WalletManager,
        firebaseRepository: FirebaseRepository
    ): WalletRepository {
        return WalletRepository(database.walletDao(), walletManager, firebaseRepository)
    }
    
    @Provides
    @Singleton
    fun provideTradeRepository(
        database: AppDatabase,
        walletManager: WalletManager,
        firebaseRepository: FirebaseRepository
    ): TradeRepository {
        return TradeRepository(database.transactionDao(), walletManager, firebaseRepository)
    }
    
    @Provides
    @Singleton
    fun provideEarnRepository(
        database: AppDatabase,
        firebaseRepository: FirebaseRepository
    ): EarnRepository {
        return EarnRepository(database.earnActivityDao(), firebaseRepository)
    }
    
    @Provides
    @Singleton
    fun provideMiningRepository(
        database: AppDatabase,
        firebaseRepository: FirebaseRepository
    ): MiningRepository {
        return MiningRepository(database.miningSessionDao(), firebaseRepository)
    }
    
    @Provides
    @Singleton
    fun provideSocialFiRepository(
        database: AppDatabase,
        firebaseRepository: FirebaseRepository
    ): SocialFiRepository {
        return SocialFiRepository(database.socialFiPostDao(), firebaseRepository)
    }
    
    @Provides
    @Singleton
    fun provideDiscoverRepository(
        database: AppDatabase,
        coinGeckoApi: CoinGeckoApi,
        firebaseRepository: FirebaseRepository
    ): DiscoverRepository {
        return DiscoverRepository(database.dAppDao(), coinGeckoApi, firebaseRepository)
    }
} 