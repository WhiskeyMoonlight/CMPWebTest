package org.dimas.medextest.core.format

private const val ML_IN_LITER = 1000

fun formatLiters(milliliters: Int): String {
    val whole = milliliters / ML_IN_LITER
    val fraction = (milliliters % ML_IN_LITER).toString().padStart(3, '0').trimEnd('0')
    return if (fraction.isEmpty()) whole.toString() else "$whole,$fraction"
}
