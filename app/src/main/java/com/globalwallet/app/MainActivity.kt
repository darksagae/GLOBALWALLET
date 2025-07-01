package com.globalwallet.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.globalwallet.app.ui.navigation.GlobalWalletNavigation
import com.globalwallet.app.ui.screens.AuthScreen
import com.globalwallet.app.ui.theme.GlobalWalletTheme
import com.globalwallet.app.ui.viewmodels.AuthViewModel
import com.globalwallet.app.data.security.SecurityManager
import com.globalwallet.app.data.wallet.WalletManager
import com.globalwallet.app.data.network.NetworkManager
import com.globalwallet.app.data.database.AppDatabase
import com.globalwallet.app.data.repository.UserRepository
import com.globalwallet.app.data.repository.MiningRepository
import com.globalwallet.app.data.repository.EarnRepository
import com.globalwallet.app.data.repository.SocialFiRepository
import com.globalwallet.app.data.repository.TradeRepository
import com.globalwallet.app.data.repository.DiscoverRepository
import com.globalwallet.app.data.api.CoinGeckoApi
import com.globalwallet.app.data.api.ApiService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Main Activity for Global Wallet App
 * 
 * This activity initializes all core components including:
 * - Web3j for blockchain interactions
 * - EncryptedSharedPreferences for secure storage
 * - Biometric authentication
 * - Room database for local data
 * - Network services for API calls
 * - DID (Decentralized Identity) system
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var securityManager: SecurityManager
    
    @Inject
    lateinit var walletManager: WalletManager
    
    @Inject
    lateinit var networkManager: NetworkManager
    
    @Inject
    lateinit var database: AppDatabase
    
    @Inject
    lateinit var userRepository: UserRepository
    
    @Inject
    lateinit var miningRepository: MiningRepository
    
    @Inject
    lateinit var earnRepository: EarnRepository
    
    @Inject
    lateinit var socialFiRepository: SocialFiRepository
    
    @Inject
    lateinit var tradeRepository: TradeRepository
    
    @Inject
    lateinit var discoverRepository: DiscoverRepository
    
    @Inject
    lateinit var coinGeckoApi: CoinGeckoApi
    
    @Inject
    lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize core components
        initializeComponents()
        
        setContent {
            GlobalWalletTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val authViewModel: AuthViewModel = hiltViewModel()
                    val authState by authViewModel.authState.collectAsState()
                    
                    if (authState.isAuthenticated) {
                        GlobalWalletNavigation(
                            onSignOut = { authViewModel.signOut() }
                        )
                    } else {
                        AuthScreen(
                            onAuthSuccess = { /* Navigation will be handled by state change */ }
                        )
                    }
                }
            }
        }
    }
    
    private fun initializeComponents() {
        // Initialize security components
        securityManager.initialize(this)
        
        // Initialize wallet manager with multi-chain support
        walletManager.initialize(
            chains = listOf(
                "ethereum", "base", "bsc", "polygon", "avalanche", "binance", "solana"
            )
        )
        
        // Initialize network manager
        networkManager.initialize()
        
        // Initialize repositories
        userRepository.initialize()
        miningRepository.initialize()
        earnRepository.initialize()
        socialFiRepository.initialize()
        tradeRepository.initialize()
        discoverRepository.initialize()
        
        // Initialize API services
        coinGeckoApi.initialize()
        apiService.initialize()
    }
} 