package com.globalwallet.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.messaging.FirebaseMessaging
import com.globalwallet.app.data.model.User
import com.globalwallet.app.data.model.Wallet
import com.globalwallet.app.data.model.Transaction
import com.globalwallet.app.data.model.MiningSession
import com.globalwallet.app.data.model.SocialFiPost
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val messaging: FirebaseMessaging
) {
    
    // Authentication
    suspend fun signInWithEmail(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                val userData = getUserData(user.uid)
                Result.success(userData)
            } else {
                Result.failure(Exception("Authentication failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun signUpWithEmail(email: String, password: String, username: String): Result<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                val newUser = User(
                    id = user.uid,
                    email = email,
                    username = username,
                    createdAt = System.currentTimeMillis(),
                    glwBalance = 0.0,
                    referralCode = generateReferralCode(),
                    isVerified = false
                )
                saveUserData(newUser)
                Result.success(newUser)
            } else {
                Result.failure(Exception("User creation failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun signOut() {
        auth.signOut()
    }
    
    fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser
        return firebaseUser?.let { getUserData(it.uid) }
    }
    
    // User data
    private suspend fun saveUserData(user: User) {
        firestore.collection("users").document(user.id).set(user).await()
    }
    
    private suspend fun getUserData(userId: String): User {
        val document = firestore.collection("users").document(userId).get().await()
        return document.toObject(User::class.java) ?: User(
            id = userId,
            email = "",
            username = "",
            createdAt = System.currentTimeMillis(),
            glwBalance = 0.0,
            referralCode = "",
            isVerified = false
        )
    }
    
    // Wallet operations
    suspend fun saveWallet(wallet: Wallet) {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId)
            .collection("wallets").document(wallet.address).set(wallet).await()
    }
    
    suspend fun getWallets(): List<Wallet> {
        val userId = auth.currentUser?.uid ?: return emptyList()
        val snapshot = firestore.collection("users").document(userId)
            .collection("wallets").get().await()
        return snapshot.toObjects(Wallet::class.java)
    }
    
    // Transactions
    suspend fun saveTransaction(transaction: Transaction) {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId)
            .collection("transactions").add(transaction).await()
    }
    
    suspend fun getTransactions(): List<Transaction> {
        val userId = auth.currentUser?.uid ?: return emptyList()
        val snapshot = firestore.collection("users").document(userId)
            .collection("transactions")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(50)
            .get().await()
        return snapshot.toObjects(Transaction::class.java)
    }
    
    // Mining sessions
    suspend fun saveMiningSession(session: MiningSession) {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId)
            .collection("mining_sessions").add(session).await()
    }
    
    suspend fun getMiningSessions(): List<MiningSession> {
        val userId = auth.currentUser?.uid ?: return emptyList()
        val snapshot = firestore.collection("users").document(userId)
            .collection("mining_sessions")
            .orderBy("startTime", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(20)
            .get().await()
        return snapshot.toObjects(MiningSession::class.java)
    }
    
    // SocialFi
    suspend fun savePost(post: SocialFiPost) {
        firestore.collection("social_posts").add(post).await()
    }
    
    suspend fun getPosts(): List<SocialFiPost> {
        val snapshot = firestore.collection("social_posts")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(50)
            .get().await()
        return snapshot.toObjects(SocialFiPost::class.java)
    }
    
    // Referrals
    suspend fun addReferral(referrerId: String, referredUserId: String) {
        firestore.collection("referrals").add(
            mapOf(
                "referrerId" to referrerId,
                "referredUserId" to referredUserId,
                "timestamp" to System.currentTimeMillis(),
                "status" to "pending"
            )
        ).await()
    }
    
    suspend fun getReferrals(): List<Map<String, Any>> {
        val userId = auth.currentUser?.uid ?: return emptyList()
        val snapshot = firestore.collection("referrals")
            .whereEqualTo("referrerId", userId)
            .get().await()
        return snapshot.documents.map { it.data ?: emptyMap() }
    }
    
    // GLW Token operations
    suspend fun updateGLWBalance(userId: String, newBalance: Double) {
        firestore.collection("users").document(userId)
            .update("glwBalance", newBalance).await()
    }
    
    suspend fun addGLWReward(userId: String, amount: Double, reason: String) {
        val reward = mapOf(
            "userId" to userId,
            "amount" to amount,
            "reason" to reason,
            "timestamp" to System.currentTimeMillis()
        )
        firestore.collection("glw_rewards").add(reward).await()
        
        // Update user balance
        val userDoc = firestore.collection("users").document(userId).get().await()
        val currentBalance = userDoc.getDouble("glwBalance") ?: 0.0
        updateGLWBalance(userId, currentBalance + amount)
    }
    
    // Push notifications
    suspend fun subscribeToTopic(topic: String) {
        messaging.subscribeToTopic(topic).await()
    }
    
    suspend fun unsubscribeFromTopic(topic: String) {
        messaging.unsubscribeFromTopic(topic).await()
    }
    
    // File upload
    suspend fun uploadProfileImage(userId: String, imageBytes: ByteArray): String {
        val imageRef = storage.reference.child("profile_images/$userId.jpg")
        imageRef.putBytes(imageBytes).await()
        return imageRef.downloadUrl.await().toString()
    }
    
    // Utility functions
    private fun generateReferralCode(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..8).map { chars.random() }.joinToString("")
    }
    
    // Real-time listeners
    fun listenToUserData(userId: String): Flow<User> = flow {
        val listener = firestore.collection("users").document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                
                if (snapshot != null && snapshot.exists()) {
                    val user = snapshot.toObject(User::class.java)
                    if (user != null) {
                        trySend(user)
                    }
                }
            }
        
        // Keep the flow alive
        awaitClose { listener.remove() }
    }
    
    fun listenToTransactions(userId: String): Flow<List<Transaction>> = flow {
        val listener = firestore.collection("users").document(userId)
            .collection("transactions")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(20)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                
                if (snapshot != null) {
                    val transactions = snapshot.toObjects(Transaction::class.java)
                    trySend(transactions)
                }
            }
        
        awaitClose { listener.remove() }
    }
} 