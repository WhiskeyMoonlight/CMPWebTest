package org.dimas.medextest.compute.lab

import kotlinx.serialization.builtins.ListSerializer
import kotlin.math.roundToLong
import kotlin.random.Random

object LabPageGenerator {
    private const val OUT_OF_RANGE_SPREAD = 1.25
    private const val PATIENTS = 5_000
    private const val MONTHS = 12
    private const val DAYS = 28
    private const val HOURS = 24
    private const val CENTS = 100.0
    private val laboratories = listOf("СберЗдоровье Лаб", "Инвитро", "Гемотест", "CMD")
    private val comments = listOf(null, null, null, "Натощак", "Повторный забор", "Гемолиз — пересдать")

    fun records(page: Int, pageSize: Int, seed: Int): List<LabResult> {
        val random = Random(seed * 31 + page)
        val analytes = LabAnalyte.entries
        return List(pageSize) { index ->
            val analyte = analytes[random.nextInt(analytes.size)]
            val middle = (analyte.low + analyte.high) / 2
            val halfRange = (analyte.high - analyte.low) / 2
            val raw = middle + (random.nextDouble() * 2 - 1) * halfRange * OUT_OF_RANGE_SPREAD
            LabResult(
                id = page.toLong() * pageSize + index,
                patientId = random.nextInt(PATIENTS),
                code = analyte.code,
                value = (raw.coerceAtLeast(0.0) * CENTS).roundToLong() / CENTS,
                unit = analyte.unit,
                takenAt = isoTimestamp(random),
                laboratory = laboratories[random.nextInt(laboratories.size)],
                comment = comments[random.nextInt(comments.size)],
            )
        }
    }

    fun json(page: Int, pageSize: Int, seed: Int): String =
        LabJson.format.encodeToString(ListSerializer(LabResult.serializer()), records(page, pageSize, seed))

    private fun isoTimestamp(random: Random): String {
        val month = random.nextInt(MONTHS) + 1
        val day = random.nextInt(DAYS) + 1
        val hour = random.nextInt(HOURS)
        return "2026-${month.twoDigits()}-${day.twoDigits()}T${hour.twoDigits()}:00:00Z"
    }

    private fun Int.twoDigits(): String = toString().padStart(2, '0')
}
