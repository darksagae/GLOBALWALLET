package com.globalwallet.app.di

import android.content.Context
import com.globalwallet.app.data.database.AppDatabase
import com.globalwallet.app.data.network.NetworkManager
import com.globalwallet.app.data.security.SecurityManager
import com.globalwallet.app.data.wallet.WalletManager
import com.globalwallet.app.data.repository.*
import com.globalwallet.app.data.api.CoinGeckoApi
import com.globalwallet.app.data.api.ApiService
import com.globalwallet.app.data.blockchain.BlockchainConfig
import com.globalwallet.app.data.blockchain.Web3Service
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }
    
    @Provides
    @Singleton
    fun provideSecurityManager(): SecurityManager {
        return SecurityManager()
    }
    
    @Provides
    @Singleton
    fun provideWalletManager(): WalletManager {
        return WalletManager()
    }
    
    @Provides
    @Singleton
    fun provideNetworkManager(): NetworkManager {
        return NetworkManager()
    }
    
    @Provides
    @Singleton
    fun provideBlockchainConfig(): BlockchainConfig {
        return BlockchainConfig()
    }
    
    @Provides
    @Singleton
    fun provideWeb3Service(blockchainConfig: BlockchainConfig): Web3Service {
        return Web3Service(blockchainConfig)
    }
    
    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository {
        return UserRepository()
    }
    
    @Provides
    @Singleton
    fun provideMiningRepository(): MiningRepository {
        return MiningRepository()
    }
    
    @Provides
    @Singleton
    fun provideEarnRepository(): EarnRepository {
        return EarnRepository()
    }
    
    @Provides
    @Singleton
    fun provideSocialFiRepository(): SocialFiRepository {
        return SocialFiRepository()
    }
    
    @Provides
    @Singleton
    fun provideTradeRepository(): TradeRepository {
        return TradeRepository()
    }
    
    @Provides
    @Singleton
    fun provideDiscoverRepository(): DiscoverRepository {
        return DiscoverRepository()
    }
    
    @Provides
    @Singleton
    fun provideCoinGeckoApi(): CoinGeckoApi {
        return CoinGeckoApi()
    }
    
    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiService()
    }
} 