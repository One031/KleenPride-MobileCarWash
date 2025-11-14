package com.example.kleenpride.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class Location (
    var id: String = "",
    var name: String = "",
    var address: String = "",
    var comment: String = ""
)

class LocationViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _locations = MutableLiveData<List<Location>>()
    val locations: LiveData<List<Location>> = _locations

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        loadLocations()
    }

    fun loadLocations() {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users")
            .document(uid)
            .collection("locations")
            .get()
            .addOnSuccessListener { result ->
                val loc = result.map { doc ->
                    Location(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        address = doc.getString("address") ?: "",
                        comment = doc.getString("comment") ?: ""
                    )
                }
                _locations.value = loc
            }
            .addOnFailureListener { e ->
                _error.value = e.message
            }
    }

    fun saveLocation(location: Location) {
        val uid = auth.currentUser?.uid ?: return

        firestore.collection("users")
            .document(uid)
            .collection("locations")
            .get()
            .addOnSuccessListener { result ->
                val isFirstLocation = result.isEmpty()

                // Assign default name "Home" to the first location
                if (isFirstLocation && location.name.isBlank()) {
                    location.name = "Home"
                }
            }

        val docRef = if (location.id.isEmpty()) {
            firestore.collection("users")
                .document(uid)
                .collection("locations")
                .document()
        } else {
            firestore.collection("users")
                .document(uid)
                .collection("locations")
                .document(location.id)
        }

        val data = mapOf(
            "name" to location.name,
            "address" to location.address,
            "comment" to location.comment
        )

        docRef.set(data)
            .addOnSuccessListener { loadLocations() }
            .addOnFailureListener { e ->
                _error.value = e.message
            }

        // Update id if newly created
        if (location.id.isEmpty()) {
            location.id = docRef.id
        }
    }

    fun deleteLocation(location: Location) {
        val uid = auth.currentUser?.uid ?: return
        if (location.id.isEmpty()) return

        firestore.collection("users")
            .document(uid)
            .collection("locations")
            .document(location.id)
            .delete()
            .addOnSuccessListener { loadLocations() }
            .addOnFailureListener { e ->
                _error.value = e.message
            }

    }

}
