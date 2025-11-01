package com.example.kleenpride.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class Vehicle(
    var id: String = "",
    var type: String = "",
    var make: String = ""
)

class VehicleViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _vehicles = MutableLiveData<List<Vehicle>>()
    val vehicles: LiveData<List<Vehicle>> = _vehicles

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    /**
     * Load all vehicles for the current logged-in user from Firestore
     * This retrieves the user's "vehicles" subcollection
     */
    fun loadVehicles() {
        val uid = auth.currentUser?.uid ?: run {
            _error.value = "User not authenticated"
            return
        }

            firestore.collection("users")
                .document(uid)
                .collection("vehicles")
                .get()
                .addOnSuccessListener { result ->
                    // Convert the documents to a list of Vehicle objects
                    val list = result.documents.mapNotNull { doc ->
                        val vehicle = doc.toObject(Vehicle::class.java)
                        vehicle?.id = doc.id // Attach the Firestore document ID
                        vehicle
                    }
                    _vehicles.value = list
                }
                .addOnFailureListener { e ->
                    _error.value = e.message
                }

    }

    /**
     * Save a vehicle to Firestore
     * If the vehicle already has an ID, update the existing document
     * If not, create a new document and assign the generated ID to the vehicle
     */
    fun saveVehicle(vehicle: Vehicle) {
        val uid = auth.currentUser?.uid ?: return
        // if the vehicle has an id field that can be updated otherwise  make a new field
        val coll = firestore.collection("users").document(uid).collection("vehicles")

        val docRef = if (vehicle.id.isEmpty()) coll.document() else coll.document(vehicle.id)
        val newVehicle = vehicle.copy(id = docRef.id)

        docRef.set(newVehicle)
            .addOnSuccessListener {
                // Reload the list to update the UI
                loadVehicles()
            }
            .addOnFailureListener {
                _error.value = it.message
            }

    }

    /**
     * Delete a vehicle from Firestore using its document ID
     * This will fail safely if the vehicle has no ID
     */
    fun deleteVehicle(vehicle: Vehicle) {
        val uid = auth.currentUser?.uid ?: return
        if (vehicle.id.isEmpty()) {
            _error.value = "Vehicle has no ID"
            return
        }

        firestore.collection("users")
            .document(uid)
            .collection("vehicles")
            .document(vehicle.id)
            .delete()
            .addOnSuccessListener {
                // Reload the list to update the UI after deletion
                loadVehicles()
            }
            .addOnFailureListener {
                _error.value = it.message
            }


    }


}