package com.example.kleenpride.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.kleenpride.ui.profile.payment.PaymentCard
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.System.currentTimeMillis

class AccountDetailsViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // Account info
    val fullName = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val currentPassword = mutableStateOf("") // For re-authentication

    // Payment cards (local, not saved to Firestore)
    val cards = mutableStateListOf<PaymentCard>()

    val cardIds = mutableStateListOf<String>()

    var selectedCardIndex = mutableStateOf(-1)

    // Loading states
    val isLoadingCards = mutableStateOf(false)
    val isSaving = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    init {
        loadAccountInfo()
        loadCards()
    }

    fun loadAccountInfo() {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                fullName.value = doc.getString("firstName") + " " + doc.getString("lastName")
                email.value = auth.currentUser?.email ?: ""
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error loading account info", e)
                errorMessage.value = "Failed to load account info"
            }
    }

    fun saveAccountInfo(
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        val uid = auth.currentUser?.uid ?: run {
            onError("User not authenticated")
            return
        }

        isSaving.value = true
        errorMessage.value = null

        val nameParts = fullName.value.trim().split(" ", limit = 2)
        val firstName = nameParts.getOrNull(0) ?: ""
        val lastName = nameParts.getOrNull(1) ?: ""

        // Update Firestore
        firestore.collection("users").document(uid)
            .update(
                mapOf(
                    "firstName" to firstName,
                    "lastName" to lastName
                )
            )
            .addOnSuccessListener {
                isSaving.value = false
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("AccountViewModel", "Error updating account info", e)
                errorMessage.value = "Failed to update account info"
                isSaving.value = false
                onError(e.message ?: "Unknown error")
            }
    }

    fun updateEmail(
        newEmail: String,
        currentPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val user = auth.currentUser?: run {
            onError("User not logged in")
            return
        }

        if (newEmail == user.email){
            onError("New email is the same as current email")
            return
        }

        // Re-authenticate before changing email
        val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)

        user.reauthenticate(credential)
            .addOnSuccessListener {
                // Now update email
                user.verifyBeforeUpdateEmail(newEmail)
                    .addOnSuccessListener {
                        // Send verification email
                        user.sendEmailVerification()
                            .addOnSuccessListener {
                                email.value = newEmail
                                onSuccess()
                            }
                            .addOnFailureListener { e ->
                                Log.e("AccountViewModel", "Error updating email", e)
                                when {
                                    e.message?.contains("email address already in use") == true -> {
                                        onError("Email address already in use by another account")
                                    }
                                    e.message?.contains("badly formatted") == true -> {
                                        onError("Invalid email format")
                                    }
                                    else -> {
                                        onError("Failed to update email")

                                    }
                                }
                            }
                    }
            }
            .addOnFailureListener { e ->
                Log.e("AccountViewModel", "Error re-authenticating", e)
                onError("Current password is incorrect")
            }
    }

    fun updatePassword(
        currentPassword: String,
        newPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val user = auth.currentUser ?: run {
            onError("User not logged in")
            return
        }

        if (newPassword.length < 6) {
            onError("Password must be at least 6 characters long")
            return
        }

        // Re-authenticate before changing password
        val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)

        user.reauthenticate(credential)
            .addOnSuccessListener {
                // Now update password
                user.updatePassword(newPassword)
                    .addOnSuccessListener {
                        password.value = "" // Clear password field
                        this.currentPassword.value = ""
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        Log.e("AccountViewModel", "Error updating password", e)
                        onError("Failed to update password")
                    }
            }
            .addOnFailureListener { e ->
                Log.e("AccountViewModel", "Error re-authenticating", e)
                onError("Current password is incorrect")
            }

    }

    fun loadCards() {
        val uid = auth.currentUser?.uid ?: return
        isLoadingCards.value = true

        val userRef = firestore.collection("users").document(uid)

        // Load cards from subcollection
        userRef.collection("paymentMethods").get().addOnSuccessListener { snapshot ->
            cards.clear()
            cardIds.clear()

            snapshot.documents.forEach { doc ->
                val alias = doc.getString("cardAlias") ?: ""
                val number = doc.getString("cardNumber") ?: ""
                val token = doc.getString("token") ?: ""
                val brand = doc.getString("brand") ?: ""
                cards.add(PaymentCard(alias, number, token, brand))
                cardIds.add(doc.id)
            }

            // Load default card selection
            userRef.get().addOnSuccessListener { userDoc ->
                val defaultToken = userDoc.getString("defaultPaymentToken") ?: ""
                if (defaultToken.isNotEmpty()) {
                    val idx = cards.indexOfFirst { it.token == defaultToken }
                    selectedCardIndex.value = if (idx >= 0) idx else -1
                }
                isLoadingCards.value = false
            }
        }
            .addOnFailureListener { e ->
                Log.e("AccountViewModel", "Error loading cards", e)
                errorMessage.value = "Failed to load cards"
                isLoadingCards.value = false
            }

    }

    fun addOrUpdateCard(card: PaymentCard, index: Int?) {
        val userId = auth.currentUser?.uid ?: return
        val userRef = firestore.collection("users").document(userId)
        val paymentMethodsRef  = userRef.collection("paymentMethods")

        if (index == null) {
            // Add new card (creates subcollection if needed)
            paymentMethodsRef.add(
                hashMapOf(
                    "cardAlias" to card.cardAlias,
                    "cardNumber" to card.cardNumber,
                    "token" to card.token,
                    "brand" to card.brand,
                    "createdAt" to currentTimeMillis()
                )
            ).addOnSuccessListener { doc ->
                cards.add(card)
                cardIds.add(doc.id)

                // If this is the first card, make it default
                if (cards.size == 1) {
                    setDefaultCard(0)
                }

                Log.d("AccountViewModel", "Card added successfully")
            }.addOnFailureListener { e ->
                Log.e("AccountViewModel", "Error adding card", e)
            }
        }else {
            // Update existing card
            val docId = cardIds.getOrNull(index)
            if (docId != null) {
                paymentMethodsRef.document(docId).update(
                    mapOf(
                        "cardAlias" to card.cardAlias,
                        "token" to card.token,
                        "brand" to card.brand
                    )
                )
                    .addOnSuccessListener {
                        cards[index] = card
                        Log.d("AccountViewModel", "Card updated successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Error updating card", e)
                    }
            }
        }

    }

    fun deleteCard(
        index: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit = {}
    ) {
        if (index < 0 || index >= cards.size) {
            onError("Invalid card index")
            return
        }

        val userId = auth.currentUser?.uid ?: return
        val docId = cardIds.getOrNull(index) ?: return

        firestore.collection("users")
            .document(userId)
            .collection("paymentMethods")
            .document(docId)
            .delete()
            .addOnSuccessListener {
                cards.removeAt(index)
                cardIds.removeAt(index)

                // If deleted card was selected, clear selection
                if (selectedCardIndex.value == index) {
                    selectedCardIndex.value = -1
                    clearDefaultCard()
                } else if (selectedCardIndex.value > index) {
                    selectedCardIndex.value -= 1
                }

                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("AccountViewModel", "Error deleting card", e)
                onError("Failed to delete card")
            }
    }

    fun selectCard(index: Int) {
        if (index >= 0 && index < cards.size){
            selectedCardIndex.value = index
            setDefaultCard(index)
        }
    }

    private fun setDefaultCard(index: Int) {
        val userId = auth.currentUser?.uid ?: return
        val token = cards.getOrNull(index)?.token ?: return

        firestore.collection("users").document(userId)
            .update("defaultPaymentToken", token)
            .addOnSuccessListener { e ->
                Log.d("AccountViewModel", "Default card set successfully")
            }
            .addOnFailureListener { e ->
                Log.e("AccountViewModel", "Error setting default card", e)
            }
    }

    private fun clearDefaultCard() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("users").document(userId)
            .update("defaultPaymentToken", "")
            .addOnFailureListener { e ->
                Log.e("AccountViewModel", "Error clearing default card", e)
            }
    }

}