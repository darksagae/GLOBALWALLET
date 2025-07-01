package com.globalwallet.app.config

/**
 * Application Configuration
 * 
 * IMPORTANT: Replace the placeholder values with your actual API keys and configuration
 * 
 * To get your Infura API key:
 * 1. Go to https://infura.io/
 * 2. Create an account or sign in
 * 3. Create a new project
 * 4. Copy your Project ID (this is your API key)
 * 5. Replace "YOUR_INFURA_API_KEY" below with your actual Project ID
 */
object AppConfig {
    
    // ===== INFURA API CONFIGURATION =====
    // Replace with your actual Infura Project ID
    const val INFURA_API_KEY = "YOUR_INFURA_API_KEY"
    
    // Optional: Replace with your Infura Secret Key (if you have one)
    const val INFURA_SECRET_KEY = "YOUR_INFURA_SECRET_KEY"
    
    // ===== NETWORK CONFIGURATION =====
    // Set to true for testnet, false for mainnet
    const val USE_TESTNET = false
    
    // ===== APP CONFIGURATION =====
    const val APP_NAME = "Global Wallet"
    const val APP_VERSION = "1.0.0"
    
    // ===== SECURITY CONFIGURATION =====
    const val BIOMETRIC_ENABLED = true
    const val ENCRYPTION_ENABLED = true
    
    // ===== FEATURE FLAGS =====
    const val ENABLE_REAL_BLOCKCHAIN = true
    const val ENABLE_MOCK_DATA = true
    const val ENABLE_SOCIAL_FEATURES = true
    const val ENABLE_MINING = true
    const val ENABLE_REFERRALS = true
    
    // ===== REWARD CONFIGURATION =====
    const val MINING_REWARD_GLW = "2.0"
    const val REFERRAL_REWARD_GLW = "5.0"
    const val DAILY_TASK_REWARD_GLW = "1.0"
    const val SOCIAL_POST_REWARD_GLW = "0.5"
    
    // ===== API ENDPOINTS =====
    const val COINGECKO_API_BASE_URL = "https://api.coingecko.com/api/v3/"
    
    // ===== SUPPORTED CHAINS =====
    val SUPPORTED_CHAINS = listOf(
        "ethereum",
        "polygon", 
        "bsc",
        "avalanche",
        "base"
    )
    
    // ===== CHAIN CONFIGURATION =====
    object Chains {
        const val ETHEREUM = "ethereum"
        const val POLYGON = "polygon"
        const val BSC = "bsc"
        const val AVALANCHE = "avalanche"
        const val BASE = "base"
        const val SOLANA = "solana"
    }
    
    // ===== ERROR MESSAGES =====
    object ErrorMessages {
        const val INFURA_KEY_MISSING = "Infura API key not configured. Please add your Infura Project ID to AppConfig.INFURA_API_KEY"
        const val NETWORK_ERROR = "Network connection error. Please check your internet connection."
        const val TRANSACTION_FAILED = "Transaction failed. Please try again."
        const val INSUFFICIENT_BALANCE = "Insufficient balance for this transaction."
        const val INVALID_ADDRESS = "Invalid wallet address."
        const val WALLET_NOT_FOUND = "Wallet not found."
    }
    
    // ===== VALIDATION =====
    fun isInfuraKeyConfigured(): Boolean {
        return INFURA_API_KEY != "YOUR_INFURA_API_KEY" && INFURA_API_KEY.isNotEmpty()
    }
    
    fun getInfuraKey(): String {
        if (!isInfuraKeyConfigured()) {
            throw IllegalStateException(ErrorMessages.INFURA_KEY_MISSING)
        }
        return INFURA_API_KEY
    }
} 