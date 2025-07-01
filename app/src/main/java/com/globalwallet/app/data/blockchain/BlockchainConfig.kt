package com.globalwallet.app.data.blockchain

import com.globalwallet.app.config.AppConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlockchainConfig @Inject constructor() {
    
    // Infura API Keys - Get from AppConfig
    private val infuraApiKey = AppConfig.getInfuraKey()
    private val infuraSecretKey = AppConfig.INFURA_SECRET_KEY
    
    // Network RPC URLs
    val ethereumRpcUrl = "https://mainnet.infura.io/v3/$infuraApiKey"
    val polygonRpcUrl = "https://polygon-mainnet.infura.io/v3/$infuraApiKey"
    val baseRpcUrl = "https://base-mainnet.infura.io/v3/$infuraApiKey"
    val bscRpcUrl = "https://bsc-dataseed1.binance.org/"
    val avalancheRpcUrl = "https://api.avax.network/ext/bc/C/rpc"
    val solanaRpcUrl = "https://api.mainnet-beta.solana.com"
    
    // Testnet URLs (for development)
    val ethereumTestnetRpcUrl = "https://sepolia.infura.io/v3/$infuraApiKey"
    val polygonTestnetRpcUrl = "https://polygon-mumbai.infura.io/v3/$infuraApiKey"
    val baseTestnetRpcUrl = "https://base-sepolia.infura.io/v3/$infuraApiKey"
    
    // Chain IDs
    val ethereumChainId = 1L
    val polygonChainId = 137L
    val baseChainId = 8453L
    val bscChainId = 56L
    val avalancheChainId = 43114L
    val solanaChainId = 101L
    
    // Testnet Chain IDs
    val ethereumTestnetChainId = 11155111L
    val polygonTestnetChainId = 80001L
    val baseTestnetChainId = 84532L
    
    // Gas settings
    val defaultGasLimit = 21000L
    val defaultGasPrice = 20000000000L // 20 Gwei
    
    // Contract addresses (example)
    val glwTokenAddress = "0x..." // Replace with actual GLW token contract address
    
    fun isTestnet(): Boolean {
        return AppConfig.USE_TESTNET
    }
    
    fun getRpcUrl(chain: String): String {
        return when (chain.lowercase()) {
            "ethereum" -> if (isTestnet()) ethereumTestnetRpcUrl else ethereumRpcUrl
            "polygon" -> if (isTestnet()) polygonTestnetRpcUrl else polygonRpcUrl
            "base" -> if (isTestnet()) baseTestnetRpcUrl else baseRpcUrl
            "bsc" -> bscRpcUrl
            "avalanche" -> avalancheRpcUrl
            "solana" -> solanaRpcUrl
            else -> ethereumRpcUrl
        }
    }
    
    fun getChainId(chain: String): Long {
        return when (chain.lowercase()) {
            "ethereum" -> if (isTestnet()) ethereumTestnetChainId else ethereumChainId
            "polygon" -> if (isTestnet()) polygonTestnetChainId else polygonChainId
            "base" -> if (isTestnet()) baseTestnetChainId else baseChainId
            "bsc" -> bscChainId
            "avalanche" -> avalancheChainId
            "solana" -> solanaChainId
            else -> ethereumChainId
        }
    }
    
    fun getExplorerUrl(chain: String, txHash: String): String {
        return when (chain.lowercase()) {
            "ethereum" -> "https://etherscan.io/tx/$txHash"
            "polygon" -> "https://polygonscan.com/tx/$txHash"
            "base" -> "https://basescan.org/tx/$txHash"
            "bsc" -> "https://bscscan.com/tx/$txHash"
            "avalanche" -> "https://snowtrace.io/tx/$txHash"
            "solana" -> "https://solscan.io/tx/$txHash"
            else -> "https://etherscan.io/tx/$txHash"
        }
    }
} 