package com.example.kleenpride.ui.profile.payment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.example.kleenpride.data.models.PayFastPayment
import com.example.kleenpride.data.models.TokenizationRequest
import kotlinx.coroutines.delay


class MockPayFastManager(private val context: Context) {
    /*
    Simulate payment initiation
    In production, this would open PayFast payment page
     */

    suspend fun initiatePayment(
        amount: Double,
        itemName: String,
        itemDescription: String,
        userFirstName: String,
        userLastName: String,
        userEmail: String,
        bookingId: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            // Simulate network delay
            delay(1500)

            // Generate mock payment ID
            val mockPaymentId = "MOCK_${System.currentTimeMillis()}"

            // Simulate successful payment
            onSuccess(mockPaymentId)
        } catch (e: Exception) {
            onError(e.message ?: "Mock payment failed")
        }
    }

    /**
     * Simulate card tokenization
     * In production, this would open PayFast and return a real token
     */

    suspend fun requestTokenization(
        userEmail: String,
        cardAlias: String,
        cardNumber: String,
        onTokenReceived: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            // Simulate processing time
            delay(2000)

            // Generate mock token
            val mockToken = "tok_${System.currentTimeMillis()}_${cardAlias.hashCode()}"

            // Simulate successful tokenization
            onTokenReceived(mockToken)
        } catch (e: Exception) {
            onError("Failed to tokenize card: ${e.message}")
        }
    }

    /**
     * Mock validation
     */
    fun validateITN(itnData: Map<String, String>, passphrase: String): Boolean {
        // Simulate validation
        return true
    }
}



/*
class PayFastManager (private val context: Context) {

    // Initiate a payment using PayFast
    @SuppressLint("DefaultLocale")
    fun initiatePayment(
        amount: Double,
        itemName: String,
        itemDescription: String,
        userFirstName: String,
        userLastName: String,
        userEmail: String,
        bookingId: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val payment = PayFastPayment(
                merchantId = PayFastConfig.MERCHANT_ID,
                merchantKey = PayFastConfig.MERCHANT_KEY,
                returnUrl = PayFastConfig.RETURN_URL,
                cancelUrl = PayFastConfig.CANCEL_URL,
                notifyUrl = PayFastConfig.NOTIFY_URL,
                nameFirst = userFirstName,
                nameLast = userLastName,
                emailAddress = userEmail,
                amount = String.format("%.2f", amount),
                itemName = itemName,
                itemDescription = itemDescription,
                customStr1 = bookingId,
                confirmationAddress = userEmail
            )

            val signature = payment.generateSignature(PayFastConfig.PASSPHRASE)
            val paymentUrl = buildPaymentUrl(payment, signature)

            // Open payment in Chrome Custom Tab
            openPaymentPage(paymentUrl)

        } catch (e: Exception){
            onError(e.message ?: "Unknown error")
        }
    }

    // Build PayFast payment URL with parameters
    private fun buildPaymentUrl(payment: PayFastPayment, signature: String): String {
        val params = payment.toMap().toMutableMap()
        params["signature"] = signature

        val queryString = params.entries.joinToString("&") {
            "${it.key}=${Uri.encode(it.value)}"
        }

        return "${PayFastConfig.baseUrl}?$queryString"
    }

    // Open payment page in Chrome Custom Tab
    private fun openPaymentPage(url: String) {
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()
        customTabsIntent.launchUrl(context, Uri.parse(url))
    }

    // Request card tokenization (for saving cards)
    fun requestTokenization(
        userEmail: String,
        cardAlias: String,
        onTokenReceived: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val tokenRequest = TokenizationRequest(
                merchantId = PayFastConfig.MERCHANT_ID,
                merchantKey = PayFastConfig.MERCHANT_KEY,
                returnUrl = "${PayFastConfig.RETURN_URL}?tokenize=true&alias=$cardAlias",
                cancelUrl = PayFastConfig.CANCEL_URL,
                amount = "5.00", // Minimum for tokenization
                itemName = "Card Tokenization"
            )
            // Build URL and open in Custom Tab
            val url = buildTokenizationUrl(tokenRequest)
            openPaymentPage(url)

        } catch (e: Exception) {
            onError("Failed to request tokenization: ${e.message}")
        }
    }

    private fun buildTokenizationUrl(request: TokenizationRequest): String {
        val params = mapOf(
            "merchant_id" to request.merchantId,
            "merchant_key" to request.merchantKey,
            "return_url" to request.returnUrl,
            "cancel_url" to request.cancelUrl,
            "amount" to request.amount,
            "item_name" to request.itemName
        )

        val queryString = params.entries.joinToString("&") {
            "${it.key}=${Uri.encode(it.value)}"
        }
        return "${PayFastConfig.baseUrl}?queryString"
    }

    /*
    Validate PayFast ITN (Instant Transaction Notification)
    Call this from your backend when PayFast sends a notification
     */

    fun validateITN(itnData: Map<String, String>, passphrase: String): Boolean {
        val signature = itnData["signature"] ?: return false
        val params = itnData.filterKeys { it != "signature" }.toSortedMap()

        val paramString = params.entries.joinToString("&") {
            "${it.key}=${it.value}"}
        val stringToHash = "$paramString&passphrase=$passphrase"

        val calculatedSignature = java.security.MessageDigest.getInstance("MD5")
            .digest(stringToHash.toByteArray())
            .joinToString("") { "%02x" .format(it)}

        return signature == calculatedSignature
    }
}

 */