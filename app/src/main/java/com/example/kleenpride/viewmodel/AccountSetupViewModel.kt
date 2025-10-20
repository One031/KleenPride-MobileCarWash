package com.example.kleenpride.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Shared ViewModel for the multi-step onboarding process
 * Holds all temporary user data
 */
class AccountSetupViewModel : ViewModel() {

    // Step 1: Basic Info
    var firstName = mutableStateOf("")
    var lastName = mutableStateOf("")

    // Step 2: Personal Info
    var preferredName = mutableStateOf("")
    var phoneNumber = mutableStateOf("")
    var address = mutableStateOf("")

    // Step 3: Car Size & Favourites
    var carSize = mutableStateOf("")
    val favourites = mutableStateListOf<String>()

    // Step 4: Preferences
    var receiveReminders = mutableStateOf(true)
    var receivePromotions = mutableStateOf(true)
    var enableNotifications = mutableStateOf(true)

    // Firestore & Auth
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // LiveData to observe success/error
    private val _updateSuccess = MutableLiveData<Boolean>(false)
    val updateSuccess: LiveData<Boolean> = _updateSuccess

    private val _updateError = MutableLiveData<String?>()
    val updateError: LiveData<String?> = _updateError

    /**
     * Save basic info immediately after registration
     */

   fun saveBasicInfo() {
       val uid = auth.currentUser?.uid ?: run {
           _updateError.value = "User not authenticated"
           return
       }

        val data = mapOf(
            "firstName" to firstName.value,
            "lastName" to lastName.value
        )

        firestore.collection("users").document(uid)
            .set(data)
            .addOnSuccessListener { _updateSuccess.value = true }
            .addOnFailureListener { e -> _updateError.value = e.message }
    }

    /**
     * Save personal info
     */

    fun savePersonalInfo() {
        val uid = auth.currentUser?.uid ?: run {
            _updateError.value = "User not authenticated"
            return
        }

        val data = mapOf(
            "preferredName" to preferredName.value,
            "phoneNumber" to phoneNumber.value,
            "address" to address.value
        )

        firestore.collection("users").document(uid)
            .update(data)
            .addOnSuccessListener { _updateSuccess.value = true }
            .addOnFailureListener { e -> _updateError.value = e.message }

    }

    /**
     * Save car type and favourites
     */

    fun saveCarDetails() {
        val uid = auth.currentUser?.uid ?: run {
            _updateError.value = "User not authenticated"
            return
        }

        val data = mapOf(
            "carSize" to carSize.value,
            "favourites" to favourites.toList()
        )

        firestore.collection("users").document(uid)
            .update(data)
            .addOnSuccessListener { _updateSuccess.value = true }
            .addOnFailureListener { e -> _updateError.value = e.message }

    }

    /**
     * Save preferences
     */

    fun savePreferences(onComplete: () -> Unit = {}){
        val uid = auth.currentUser?.uid ?: run {
            _updateError.value = "User not authenticated"
            return
        }

        val data = mapOf(
            "receiveReminders" to receiveReminders.value,
            "receivePromotions" to receivePromotions.value,
            "enableNotifications" to enableNotifications.value
        )

        firestore.collection("users").document(uid)
            .update(data)
            .addOnSuccessListener { onComplete() }
            .addOnFailureListener { e -> _updateError.value = e.message }
    }

    /**
     * Reset success after navigation
     */

    fun onNavigationComplete () { _updateSuccess.value = false }

}