package com.example.kleenpride.data.models

import java.security.MessageDigest

data class PayFastPayment (
    val merchantId: String,
    val merchantKey: String,
    val returnUrl: String,
    val cancelUrl: String,
    val notifyUrl: String,
    val nameFirst: String,
    val nameLast: String,
    val emailAddress: String,
    val amount: String,
    val itemName: String,
    val itemDescription: String,
    val customStr1: String = "", //booking ID
    val customStr2: String = "",
    val emailConfirmation: String = "1",
    val confirmationAddress: String = ""
) {
    fun toMap(): Map<String, String> {
        return mapOf(
            "merchant_id" to merchantId,
            "merchant_key" to merchantKey,
            "return_url" to returnUrl,
            "cancel_url" to cancelUrl,
            "notify_url" to notifyUrl,
            "name_first" to nameFirst,
            "name_last" to nameLast,
            "email_address" to emailAddress,
            "amount" to amount,
            "item_name" to itemName,
            "item_description" to itemDescription,
            "custom_str1" to customStr1,
            "custom_str2" to customStr2,
            "email_confirmation" to emailConfirmation,
            "confirmation_address" to confirmationAddress
        )
    }

    fun generateSignature(passphrase: String): String {
        val params = toMap().toSortedMap()
        val paramString = params.entries.joinToString("&") {"${it.key}=${it.value}"}
        val stringToHash = "$paramString&passphrase=$passphrase"

        return MessageDigest.getInstance("MD5")
            .digest(stringToHash.toByteArray())
            .joinToString("")
    }
}

data class PayFastResponse(
    val success: Boolean,
    val paymentId: String = "",
    val message: String = ""
)

// For tokenization
data class TokenizationRequest(
    val merchantId: String,
    val merchantKey: String,
    val returnUrl: String,
    val cancelUrl: String,
    val amount: String = "5.00", // Minimum for tokenization
    val itemName: String = "Card Tokenization"
)