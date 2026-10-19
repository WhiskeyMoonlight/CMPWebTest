package org.dimas.medextest.core.format

private const val GROUPING_THRESHOLD = 10_000

fun formatRubles(amount: Int): String = "${groupDigits(amount)}$NBSP₽"

fun formatPriceFrom(amount: Int): String = "от$NBSP${formatRubles(amount)}"

private fun groupDigits(amount: Int): String {
    val digits = amount.toString()
    if (amount < GROUPING_THRESHOLD) return digits
    return digits.reversed().chunked(3).joinToString(NBSP.toString()).reversed()
}
