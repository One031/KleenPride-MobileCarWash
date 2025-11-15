package com.example.kleenpride.ui.profile.payment

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

@Composable
fun PayFastCardList(
    cards: List<PaymentCard>,
    selectedCardIdToken: String?,
    onSelect: (PaymentCard) -> Unit,
    onDelete: (PaymentCard) -> Unit
) {
    Column {
        cards.forEach { card ->
            PaymentCardItem(
                cardAlias = card.cardAlias,
                selected = card.token == selectedCardIdToken,
                onSelect = { onSelect(card) },
                onDelete = { onDelete(card) }
            )
        }
    }
}