package com.example.kleenpride.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class UserData(
    val firstName: String = "",
    val lastName: String = "",
    val preferredName: String = "",
    val phoneNumber: String = "",
    val favourites: List<String> = emptyList(),
    val defaultVehicleId: String = "",
    val defaultAddressId: String = ""
)

class UserDataViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _userData = MutableLiveData<UserData>()
    val userData: LiveData<UserData> = _userData

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        loadUserData()
    }

    fun loadUserData() {
        val uid = auth.currentUser?.uid ?: run{
            _error.value = "User not authenticated"
            return
        }

        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = UserData(
                        firstName = document.getString("firstName") ?: "",
                        lastName = document.getString("lastName") ?: "",
                        preferredName = document.getString("preferredName") ?: "",
                        phoneNumber = document.getString("phoneNumber") ?: "",
                        defaultAddressId = document.getString("defaultAddressId") ?: "",
                        defaultVehicleId = document.getString("defaultVehicleId") ?: "",
                        favourites = document.get("favourites") as? List<String> ?: emptyList()
                    )
                    _userData.value = user
                } else {
                    _error.value = "User not found"
                }
        }
            .addOnFailureListener { e ->
                _error.value = e.message
            }

    }
}