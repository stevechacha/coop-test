package com.chacha.dev.coop_test.domain.model

enum class CardType {
    PREPAID,
    CREDIT,
    MULTI_CURRENCY,
    DEBIT;

    companion object {
        fun from(raw: String): CardType = try {
            valueOf(raw)
        } catch (_: IllegalArgumentException) {
            PREPAID
        }
    }
}

