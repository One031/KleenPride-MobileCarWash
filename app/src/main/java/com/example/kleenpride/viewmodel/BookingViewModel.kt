package com.example.kleenpride.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kleenpride.data.booking.Booking
import com.example.kleenpride.data.booking.BookingRepository

/**
 * ViewModel connects the UI (BookingScreen) with the repository
 * It manages UI state and performs logic between the View and the Data
 */
class BookingViewModel : ViewModel() {

    // Reference to the repo
    private val repo = BookingRepository()

    // Backing field for the list of bookings (mutable)
    private val _bookings = MutableLiveData<List<Booking>>()

    // Exposed immutable LiveData for the UI to observe
    val bookings: LiveData<List<Booking>> = _bookings

    // Holds any error messages that might occur
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /**
     * Loads all bookings from Firestore for the current user
     */
    fun loadBookings() {
        repo.getUserBookings()
            .addOnSuccessListener { result ->
                // Convert Firestore documents to a list of Booking objects
                val list = result.toObjects(Booking::class.java)
                _bookings.value = list
            }
            .addOnFailureListener { e ->
                // Update error LiveData with the message
                _error.value = e.message
            }
    }

    /**
     * Creates a new booking and refreshes the list when done
     */
    fun createBooking(booking: Booking) {
        repo.createBooking(booking)
            .addOnSuccessListener { loadBookings() }
            .addOnFailureListener { e -> _error.value = e.message }
    }
}