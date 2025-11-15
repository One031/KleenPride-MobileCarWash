package com.example.kleenpride.ui.profile.payment

object PayFastConfig {
    // Sandbox for testing
    const val IS_SANDBOX = true

    // Get these from PayFast Dashboard
    const val MERCHANT_ID = "10000100" // Sandbox merchant ID
    const val MERCHANT_KEY = "46f0cd694581a" // Sandbox merchant key
    const val PASSPHRASE = "your_passphrase_here" // Set in PayFast settings

    // URLs
    const val SANDBOX_URL = "https://sandbox.payfast.co.za/eng/process"
    const val PRODUCTION_URL = "https://payfast.co.za/eng/process"

    // Your app URLS
    const val RETURN_URL = "https://yourdomain.com/payment/return"
    const val CANCEL_URL = "https://yourdomain.com/payment/cancel"
    const val NOTIFY_URL = "https://yourdomain.com/payment/notify"

    val baseUrl: String
        get() = if (IS_SANDBOX) SANDBOX_URL else PRODUCTION_URL
}