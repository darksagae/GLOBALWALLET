package com.globalwallet.app.data.blockchain

import org.web3j.crypto.Credentials
import org.web3j.crypto.ECKeyPair
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.protocol.http.HttpService
import org.web3j.tx.Transfer
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class Web3Service @Inject constructor(
    private val blockchainConfig: BlockchainConfig
) {
    
    private val web3jInstances = mutableMapOf<String, Web3j>()
    
    fun getWeb3j(chain: String): Web3j {
        return web3jInstances.getOrPut(chain) {
            val rpcUrl = blockchainConfig.getRpcUrl(chain)
            Web3j.build(HttpService(rpcUrl))
        }
    }
    
    suspend fun getBalance(address: String, chain: String): BigDecimal = withContext(Dispatchers.IO) {
        try {
            val web3j = getWeb3j(chain)
            val balance: EthGetBalance = web3j.ethGetBalance(address, org.web3j.protocol.core.DefaultBlockParameterName.LATEST).send()
            
            if (balance.hasError()) {
                throw Exception("Error getting balance: ${balance.error.message}")
            }
            
            val balanceWei = balance.balance
            Convert.fromWei(balanceWei.toBigDecimal(), Convert.Unit.ETHER)
        } catch (e: Exception) {
            throw Exception("Failed to get balance: ${e.message}")
        }
    }
    
    suspend fun sendTransaction(
        fromAddress: String,
        toAddress: String,
        amount: BigDecimal,
        privateKey: String,
        chain: String
    ): String = withContext(Dispatchers.IO) {
        try {
            val web3j = getWeb3j(chain)
            val credentials = Credentials.create(privateKey)
            
            // Validate addresses
            if (!org.web3j.utils.Validation.isValidAddress(fromAddress)) {
                throw Exception("Invalid from address")
            }
            if (!org.web3j.utils.Validation.isValidAddress(toAddress)) {
                throw Exception("Invalid to address")
            }
            
            // Convert amount to Wei
            val amountWei = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger()
            
            // Get gas price
            val gasPrice = web3j.ethGasPrice().send().gasPrice
            
            // Estimate gas limit
            val gasLimit = web3j.ethEstimateGas(
                org.web3j.protocol.core.methods.request.Transaction.createEtherTransaction(
                    fromAddress, null, toAddress, amountWei
                )
            ).send().amountUsed
            
            // Send transaction
            val transactionReceipt: TransactionReceipt = Transfer.sendFunds(
                web3j, credentials, toAddress, amount, Convert.Unit.ETHER
            ).send()
            
            if (transactionReceipt.isStatusOK) {
                transactionReceipt.transactionHash
            } else {
                throw Exception("Transaction failed")
            }
        } catch (e: Exception) {
            throw Exception("Failed to send transaction: ${e.message}")
        }
    }
    
    suspend fun getTransactionStatus(txHash: String, chain: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val web3j = getWeb3j(chain)
            val receipt = web3j.ethGetTransactionReceipt(txHash).send()
            
            if (receipt.hasError()) {
                throw Exception("Error getting transaction receipt: ${receipt.error.message}")
            }
            
            receipt.transactionReceipt.isPresent && receipt.transactionReceipt.get().isStatusOK
        } catch (e: Exception) {
            throw Exception("Failed to get transaction status: ${e.message}")
        }
    }
    
    suspend fun getGasPrice(chain: String): BigInteger = withContext(Dispatchers.IO) {
        try {
            val web3j = getWeb3j(chain)
            val gasPrice = web3j.ethGasPrice().send()
            
            if (gasPrice.hasError()) {
                throw Exception("Error getting gas price: ${gasPrice.error.message}")
            }
            
            gasPrice.gasPrice
        } catch (e: Exception) {
            throw Exception("Failed to get gas price: ${e.message}")
        }
    }
    
    suspend fun estimateGas(
        fromAddress: String,
        toAddress: String,
        amount: BigDecimal,
        chain: String
    ): BigInteger = withContext(Dispatchers.IO) {
        try {
            val web3j = getWeb3j(chain)
            val amountWei = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger()
            
            val gasEstimate = web3j.ethEstimateGas(
                org.web3j.protocol.core.methods.request.Transaction.createEtherTransaction(
                    fromAddress, null, toAddress, amountWei
                )
            ).send()
            
            if (gasEstimate.hasError()) {
                throw Exception("Error estimating gas: ${gasEstimate.error.message}")
            }
            
            gasEstimate.amountUsed
        } catch (e: Exception) {
            throw Exception("Failed to estimate gas: ${e.message}")
        }
    }
    
    fun validateAddress(address: String): Boolean {
        return try {
            org.web3j.utils.Validation.isValidAddress(address)
        } catch (e: Exception) {
            false
        }
    }
    
    fun createWallet(): Pair<String, String> {
        val keyPair = ECKeyPair.createRandom()
        val credentials = Credentials.create(keyPair)
        return Pair(credentials.address, keyPair.privateKey.toString(16))
    }
    
    fun getAddressFromPrivateKey(privateKey: String): String {
        val credentials = Credentials.create(privateKey)
        return credentials.address
    }
} 