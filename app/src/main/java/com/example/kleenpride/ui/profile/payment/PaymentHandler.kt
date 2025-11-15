package com.example.kleenpride.ui.profile.payment

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import java.lang.System.currentTimeMillis

/**
 * Mock Payment Handler for testing booking payments without PayFast
 */

class MockPaymentHandler(private val context: Context) {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val mockPayFastManager = MockPayFastManager(context)

    /**
     * Simulate processing a booking payment
     */

    suspend fun processBookingPayment(
        bookingId: String,
        amount: Double,
        serviceName: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            onError("User not authenticated")
            return
        }

        try {
            // Get user details from Firestore
            val userDoc = firestore.collection("users").document(currentUser.uid).get().await()
            val firstName = userDoc.getString("firstName")
            val lastName = userDoc.getString("lastName")
            val email = currentUser.email ?: ""

            // Simulate payment processing
            mockPayFastManager.initiatePayment(
                amount = amount,
                itemName = serviceName,
                itemDescription = "Car Wash Booking -  $serviceName",
                userFirstName = firstName!!,
                userLastName = lastName!!,
                userEmail = email,
                bookingId = bookingId,
                onSuccess = { paymentId ->
                    // Update booking status to "paid"
                    updateBookingPaymentStatus(bookingId, paymentId, "complete")
                    onSuccess()
                },
                onError = onError
            )
        } catch (e: Exception) {
            onError("Failed to process payment: ${e.message}")
        }
    }

    // Simulate payment with saved card
    suspend fun processBookingPaymentWithCard(
        cardToken: String,
        bookingId: String,
        amount: Double,
        serviceName: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            // Simulate processing delay
            delay(2000)

            // Generate mock payment ID
            val mockPaymentId = "MOCK_ID_${System.currentTimeMillis()}"

            // Update booking status to "paid"
            updateBookingPaymentStatus(bookingId, mockPaymentId, "complete")

            onSuccess()
        }catch (e: Exception) {
            onError("Failed to process payment: ${e.message}")
        }
    }

    // Update booking payment status in Firestore
    private fun updateBookingPaymentStatus(
        bookingId: String,
        paymentId: String,
        status: String
    ) {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("users")
            .document(userId)
            .collection("bookings")
            .document(bookingId)
            .update(
                mapOf(
                    "paymentId" to paymentId,
                    "paymentStatus" to status,
                    "paymentMethod" to "mock_payfast",
                    "paymentDate" to currentTimeMillis()
                )
            )
            .addOnSuccessListener {
                Toast.makeText(context, "Payment successful", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Payment failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    // Mock payment verification (for testing)
    suspend fun verifyPayment(
        bookingId: String,
        onVerified: (Boolean) -> Unit
    ) {
        delay(500)
        onVerified(true)
    }
}