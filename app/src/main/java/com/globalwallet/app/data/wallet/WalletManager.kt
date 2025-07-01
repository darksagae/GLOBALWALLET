package com.globalwallet.app.data.wallet

import android.content.Context
import com.globalwallet.app.data.model.Wallet
import com.globalwallet.app.data.model.Balance
import com.globalwallet.app.data.model.Transaction
import com.globalwallet.app.data.blockchain.Web3Service
import com.globalwallet.app.data.blockchain.BlockchainConfig
import com.globalwallet.app.data.security.SecurityManager
import io.github.novacrypto.bip39.MnemonicGenerator
import io.github.novacrypto.bip39.wordlists.English
import org.web3j.crypto.Credentials
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.MnemonicUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger
import java.security.SecureRandom
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

/**
 * Wallet Manager for Global Wallet App
 * 
 * Features:
 * - Multi-chain wallet creation and management
 * - BIP39 mnemonic generation and recovery
 * - Web3j integration for EVM-compatible chains
 * - Mock implementations for non-EVM chains
 * - Secure key management and storage
 */

@Singleton
class WalletManager @Inject constructor(
    private val web3Service: Web3Service,
    private val blockchainConfig: BlockchainConfig,
    private val securityManager: SecurityManager
) {
    
    private val _wallets = MutableStateFlow<List<Wallet>>(emptyList())
    val wallets: StateFlow<List<Wallet>> = _wallets.asStateFlow()
    
    private val _balances = MutableStateFlow<Map<String, List<Balance>>>(emptyMap())
    val balances: StateFlow<Map<String, List<Balance>>> = _balances.asStateFlow()
    
    private val _transactions = MutableStateFlow<Map<String, List<Transaction>>>(emptyMap())
    val transactions: StateFlow<Map<String, List<Transaction>>> = _transactions.asStateFlow()
    
    private var isInitialized = false
    
    fun initialize(chains: List<String> = listOf("ethereum", "polygon", "bsc", "avalanche", "base")) {
        if (isInitialized) return
        
        // Initialize with mock data for demo
        initializeMockWallets(chains)
        isInitialized = true
    }
    
    private fun initializeMockWallets(chains: List<String>) {
        val mockWallets = chains.map { chain ->
            val (address, privateKey) = web3Service.createWallet()
            Wallet(
                id = UUID.randomUUID().toString(),
                address = address,
                chain = chain,
                name = "${chain.capitalize()} Wallet",
                isActive = true,
                createdAt = LocalDateTime.now(),
                privateKey = privateKey // In production, this should be encrypted
            )
        }
        
        _wallets.value = mockWallets
        
        // Initialize mock balances
        val mockBalances = mockWallets.associate { wallet ->
            wallet.id to listOf(
                Balance(
                    id = UUID.randomUUID().toString(),
                    walletId = wallet.id,
                    tokenSymbol = when (wallet.chain) {
                        "ethereum" -> "ETH"
                        "polygon" -> "MATIC"
                        "bsc" -> "BNB"
                        "avalanche" -> "AVAX"
                        "base" -> "ETH"
                        else -> "ETH"
                    },
                    tokenName = when (wallet.chain) {
                        "ethereum" -> "Ethereum"
                        "polygon" -> "Polygon"
                        "bsc" -> "Binance Smart Chain"
                        "avalanche" -> "Avalanche"
                        "base" -> "Base"
                        else -> "Ethereum"
                    },
                    balance = BigDecimal("0.0"),
                    usdValue = BigDecimal("0.0"),
                    lastUpdated = LocalDateTime.now()
                ),
                Balance(
                    id = UUID.randomUUID().toString(),
                    walletId = wallet.id,
                    tokenSymbol = "GLW",
                    tokenName = "Global Wallet Token",
                    balance = BigDecimal("100.0"),
                    usdValue = BigDecimal("50.0"),
                    lastUpdated = LocalDateTime.now()
                )
            )
        }
        
        _balances.value = mockBalances
        
        // Initialize mock transactions
        val mockTransactions = mockWallets.associate { wallet ->
            wallet.id to listOf(
                Transaction(
                    id = UUID.randomUUID().toString(),
                    walletId = wallet.id,
                    hash = "0x${UUID.randomUUID().toString().replace("-", "")}",
                    type = "RECEIVE",
                    amount = BigDecimal("0.1"),
                    tokenSymbol = when (wallet.chain) {
                        "ethereum" -> "ETH"
                        "polygon" -> "MATIC"
                        "bsc" -> "BNB"
                        "avalanche" -> "AVAX"
                        "base" -> "ETH"
                        else -> "ETH"
                    },
                    fromAddress = "0x${UUID.randomUUID().toString().replace("-", "")}",
                    toAddress = wallet.address,
                    status = "CONFIRMED",
                    timestamp = LocalDateTime.now().minusHours(2),
                    gasUsed = BigDecimal("21000"),
                    gasPrice = BigDecimal("20000000000")
                )
            )
        }
        
        _transactions.value = mockTransactions
    }
    
    suspend fun createWallet(chain: String, name: String): Wallet {
        val (address, privateKey) = web3Service.createWallet()
        
        val wallet = Wallet(
            id = UUID.randomUUID().toString(),
            address = address,
            chain = chain,
            name = name,
            isActive = true,
            createdAt = LocalDateTime.now(),
            privateKey = privateKey
        )
        
        val currentWallets = _wallets.value.toMutableList()
        currentWallets.add(wallet)
        _wallets.value = currentWallets
        
        return wallet
    }
    
    suspend fun getRealBalance(walletId: String): BigDecimal {
        val wallet = _wallets.value.find { it.id == walletId }
        return if (wallet != null) {
            try {
                web3Service.getBalance(wallet.address, wallet.chain)
            } catch (e: Exception) {
                BigDecimal.ZERO
            }
        } else {
            BigDecimal.ZERO
        }
    }
    
    suspend fun sendTransaction(
        fromWalletId: String,
        toAddress: String,
        amount: BigDecimal,
        chain: String
    ): String {
        val wallet = _wallets.value.find { it.id == fromWalletId }
        if (wallet == null) {
            throw Exception("Wallet not found")
        }
        
        if (!web3Service.validateAddress(toAddress)) {
            throw Exception("Invalid recipient address")
        }
        
        return try {
            val txHash = web3Service.sendTransaction(
                fromAddress = wallet.address,
                toAddress = toAddress,
                amount = amount,
                privateKey = wallet.privateKey,
                chain = chain
            )
            
            // Add transaction to local state
            val transaction = Transaction(
                id = UUID.randomUUID().toString(),
                walletId = wallet.id,
                hash = txHash,
                type = "SEND",
                amount = amount,
                tokenSymbol = when (chain) {
                    "ethereum" -> "ETH"
                    "polygon" -> "MATIC"
                    "bsc" -> "BNB"
                    "avalanche" -> "AVAX"
                    "base" -> "ETH"
                    else -> "ETH"
                },
                fromAddress = wallet.address,
                toAddress = toAddress,
                status = "PENDING",
                timestamp = LocalDateTime.now(),
                gasUsed = BigDecimal.ZERO,
                gasPrice = BigDecimal.ZERO
            )
            
            val currentTransactions = _transactions.value.toMutableMap()
            val walletTransactions = currentTransactions[wallet.id]?.toMutableList() ?: mutableListOf()
            walletTransactions.add(0, transaction)
            currentTransactions[wallet.id] = walletTransactions
            _transactions.value = currentTransactions
            
            txHash
        } catch (e: Exception) {
            throw Exception("Failed to send transaction: ${e.message}")
        }
    }
    
    suspend fun getTransactionStatus(txHash: String, chain: String): Boolean {
        return try {
            web3Service.getTransactionStatus(txHash, chain)
        } catch (e: Exception) {
            false
        }
    }
    
    suspend fun estimateGas(
        fromWalletId: String,
        toAddress: String,
        amount: BigDecimal,
        chain: String
    ): BigDecimal {
        val wallet = _wallets.value.find { it.id == fromWalletId }
        if (wallet == null) {
            throw Exception("Wallet not found")
        }
        
        return try {
            val gasLimit = web3Service.estimateGas(wallet.address, toAddress, amount, chain)
            val gasPrice = web3Service.getGasPrice(chain)
            gasLimit.toBigDecimal() * gasPrice.toBigDecimal()
        } catch (e: Exception) {
            throw Exception("Failed to estimate gas: ${e.message}")
        }
    }
    
    fun generateMnemonic(): String {
        val entropy = ByteArray(16)
        Random().nextBytes(entropy)
        val mnemonicGenerator = MnemonicGenerator(English.INSTANCE)
        val stringBuilder = StringBuilder()
        mnemonicGenerator.createMnemonic(entropy, stringBuilder::append)
        return stringBuilder.toString()
    }
    
    fun getWalletByChain(chain: String): Wallet? {
        return _wallets.value.find { it.chain == chain && it.isActive }
    }
    
    fun getWalletsByChain(chain: String): List<Wallet> {
        return _wallets.value.filter { it.chain == chain }
    }
    
    fun getTotalBalance(): BigDecimal {
        return _balances.value.values.flatten().sumOf { it.usdValue }
    }
    
    fun getRecentTransactions(limit: Int = 10): List<Transaction> {
        return _transactions.value.values.flatten()
            .sortedByDescending { it.timestamp }
            .take(limit)
    }
}

sealed class TransactionResult {
    data class Success(
        val txHash: String,
        val gasUsed: BigInteger,
        val gasPrice: BigInteger
    ) : TransactionResult()
    
    data class Error(val message: String) : TransactionResult()
} 