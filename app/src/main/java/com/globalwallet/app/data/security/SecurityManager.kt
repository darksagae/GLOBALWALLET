package com.globalwallet.app.data.security

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Security Manager for Global Wallet App
 * 
 * Features:
 * - Biometric authentication using Android BiometricPrompt
 * - Encrypted storage using EncryptedSharedPreferences
 * - Secure key generation and management
 * - Wallet encryption and decryption
 * - DID (Decentralized Identity) security
 */

@Singleton
class SecurityManager @Inject constructor() {
    
    private lateinit var encryptedPreferences: EncryptedSharedPreferences
    private lateinit var masterKey: MasterKey
    private lateinit var context: Context
    
    fun initialize(context: Context) {
        this.context = context
        setupEncryptedStorage()
    }
    
    private fun setupEncryptedStorage() {
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            "global_wallet_master_key",
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .setUserAuthenticationRequired(true)
            .setUserAuthenticationValidityDurationSeconds(300) // 5 minutes
            .build()
        
        masterKey = MasterKey.Builder(context)
            .setKeyGenParameterSpec(keyGenParameterSpec)
            .build()
        
        encryptedPreferences = EncryptedSharedPreferences.create(
            context,
            "global_wallet_secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences
    }
    
    suspend fun isBiometricAvailable(): Boolean = withContext(Dispatchers.IO) {
        val biometricManager = BiometricManager.from(context)
        return@withContext when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
    }
    
    suspend fun authenticateWithBiometric(
        activity: FragmentActivity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        onFailed: () -> Unit
    ) = withContext(Dispatchers.Main) {
        val executor = ContextCompat.getMainExecutor(activity)
        
        val biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errString.toString())
                }
                
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }
                
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onFailed()
                }
            })
        
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Global Wallet Authentication")
            .setSubtitle("Use your biometric to access your wallet")
            .setNegativeButtonText("Cancel")
            .build()
        
        biometricPrompt.authenticate(promptInfo)
    }
    
    suspend fun encryptData(data: String): String = withContext(Dispatchers.IO) {
        try {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                "global_wallet_data_key",
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .setUserAuthenticationRequired(false)
                .build()
            
            keyGenerator.init(keyGenParameterSpec)
            val secretKey = keyGenerator.generateKey()
            
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            
            val encryptedBytes = cipher.doFinal(data.toByteArray())
            val iv = cipher.iv
            
            // Combine IV and encrypted data
            val combined = iv + encryptedBytes
            return@withContext android.util.Base64.encodeToString(combined, android.util.Base64.DEFAULT)
        } catch (e: Exception) {
            throw SecurityException("Failed to encrypt data", e)
        }
    }
    
    suspend fun decryptData(encryptedData: String): String = withContext(Dispatchers.IO) {
        try {
            val keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)
            
            val secretKey = keyStore.getKey("global_wallet_data_key", null) as SecretKey
            
            val combined = android.util.Base64.decode(encryptedData, android.util.Base64.DEFAULT)
            val iv = combined.sliceArray(0..11)
            val encryptedBytes = combined.sliceArray(12 until combined.size)
            
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val spec = GCMParameterSpec(128, iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
            
            val decryptedBytes = cipher.doFinal(encryptedBytes)
            return@withContext String(decryptedBytes)
        } catch (e: Exception) {
            throw SecurityException("Failed to decrypt data", e)
        }
    }
    
    suspend fun storeSecureData(key: String, value: String) = withContext(Dispatchers.IO) {
        encryptedPreferences.edit().putString(key, value).apply()
    }
    
    suspend fun getSecureData(key: String): String? = withContext(Dispatchers.IO) {
        return@withContext encryptedPreferences.getString(key, null)
    }
    
    suspend fun removeSecureData(key: String) = withContext(Dispatchers.IO) {
        encryptedPreferences.edit().remove(key).apply()
    }
    
    suspend fun clearAllSecureData() = withContext(Dispatchers.IO) {
        encryptedPreferences.edit().clear().apply()
    }
    
    suspend fun storeWalletData(
        walletId: String,
        mnemonic: String,
        privateKey: String,
        publicKey: String,
        address: String,
        chain: String
    ) = withContext(Dispatchers.IO) {
        val encryptedMnemonic = encryptData(mnemonic)
        val encryptedPrivateKey = encryptData(privateKey)
        
        storeSecureData("wallet_${walletId}_mnemonic", encryptedMnemonic)
        storeSecureData("wallet_${walletId}_private_key", encryptedPrivateKey)
        storeSecureData("wallet_${walletId}_public_key", publicKey)
        storeSecureData("wallet_${walletId}_address", address)
        storeSecureData("wallet_${walletId}_chain", chain)
    }
    
    suspend fun getWalletData(walletId: String): WalletData? = withContext(Dispatchers.IO) {
        try {
            val encryptedMnemonic = getSecureData("wallet_${walletId}_mnemonic") ?: return@withContext null
            val encryptedPrivateKey = getSecureData("wallet_${walletId}_private_key") ?: return@withContext null
            val publicKey = getSecureData("wallet_${walletId}_public_key") ?: return@withContext null
            val address = getSecureData("wallet_${walletId}_address") ?: return@withContext null
            val chain = getSecureData("wallet_${walletId}_chain") ?: return@withContext null
            
            val mnemonic = decryptData(encryptedMnemonic)
            val privateKey = decryptData(encryptedPrivateKey)
            
            return@withContext WalletData(
                walletId = walletId,
                mnemonic = mnemonic,
                privateKey = privateKey,
                publicKey = publicKey,
                address = address,
                chain = chain
            )
        } catch (e: Exception) {
            return@withContext null
        }
    }
    
    suspend fun storeUserData(
        userId: String,
        username: String,
        email: String?,
        did: String?,
        referralCode: String,
        referredBy: String?
    ) = withContext(Dispatchers.IO) {
        storeSecureData("user_${userId}_username", username)
        email?.let { storeSecureData("user_${userId}_email", it) }
        did?.let { storeSecureData("user_${userId}_did", it) }
        storeSecureData("user_${userId}_referral_code", referralCode)
        referredBy?.let { storeSecureData("user_${userId}_referred_by", it) }
    }
    
    suspend fun getUserData(userId: String): UserData? = withContext(Dispatchers.IO) {
        try {
            val username = getSecureData("user_${userId}_username") ?: return@withContext null
            val email = getSecureData("user_${userId}_email")
            val did = getSecureData("user_${userId}_did")
            val referralCode = getSecureData("user_${userId}_referral_code") ?: return@withContext null
            val referredBy = getSecureData("user_${userId}_referred_by")
            
            return@withContext UserData(
                userId = userId,
                username = username,
                email = email,
                did = did,
                referralCode = referralCode,
                referredBy = referredBy
            )
        } catch (e: Exception) {
            return@withContext null
        }
    }
    
    suspend fun storeMiningData(
        userId: String,
        lastMiningTime: Long,
        currentStreak: Int,
        totalMined: String
    ) = withContext(Dispatchers.IO) {
        storeSecureData("mining_${userId}_last_time", lastMiningTime.toString())
        storeSecureData("mining_${userId}_streak", currentStreak.toString())
        storeSecureData("mining_${userId}_total", totalMined)
    }
    
    suspend fun getMiningData(userId: String): MiningData? = withContext(Dispatchers.IO) {
        try {
            val lastMiningTimeStr = getSecureData("mining_${userId}_last_time")
            val streakStr = getSecureData("mining_${userId}_streak")
            val totalStr = getSecureData("mining_${userId}_total")
            
            if (lastMiningTimeStr == null || streakStr == null || totalStr == null) {
                return@withContext null
            }
            
            return@withContext MiningData(
                userId = userId,
                lastMiningTime = lastMiningTimeStr.toLong(),
                currentStreak = streakStr.toInt(),
                totalMined = totalStr
            )
        } catch (e: Exception) {
            return@withContext null
        }
    }
    
    suspend fun generateSecureRandomString(length: Int = 32): String = withContext(Dispatchers.IO) {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return@withContext (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
    
    suspend fun generateReferralCode(): String = withContext(Dispatchers.IO) {
        val prefix = "GLW"
        val randomPart = generateSecureRandomString(8)
        return@withContext "$prefix$randomPart"
    }
    
    suspend fun validateReferralCode(code: String): Boolean = withContext(Dispatchers.IO) {
        // Basic validation - in a real app, this would check against a database
        return@withContext code.startsWith("GLW") && code.length == 11
    }
}

data class WalletData(
    val walletId: String,
    val mnemonic: String,
    val privateKey: String,
    val publicKey: String,
    val address: String,
    val chain: String
)

data class UserData(
    val userId: String,
    val username: String,
    val email: String?,
    val did: String?,
    val referralCode: String,
    val referredBy: String?
)

data class MiningData(
    val userId: String,
    val lastMiningTime: Long,
    val currentStreak: Int,
    val totalMined: String
) 