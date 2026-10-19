package org.dimas.medextest.compute.job

import org.dimas.medextest.compute.scan.ScanPixels
import org.dimas.medextest.compute.scan.ScanStats

internal class ScanBandStepper(private val job: ScanBandJob) : Stepper {
    private val window = 2 * job.radius + 1
    private val area = window * window
    private val rows = Array(window) { IntArray(job.width) }
    private val columnSums = IntArray(job.width)
    private val sourceRow = IntArray(job.width)
    private val histogram = IntArray(ScanStats.LEVELS)
    private var checksum = 0
    private var nextRow = job.rowFrom
    private var primed = false

    override val isDone: Boolean get() = nextRow >= job.rowTo

    override fun step() {
        check(!isDone) { "Stepper is already done" }
        if (!primed) prime()
        val y = nextRow
        var rowSum = 0
        for (x in 0 until job.width) {
            val value = columnSums[x] / area
            histogram[value]++
            rowSum += value
        }
        checksum += (y + 1) * rowSum
        slideWindow(y)
        nextRow++
    }

    override fun outcome(): ComputeOutcome {
        check(isDone) { "Stepper is not done yet" }
        return ComputeOutcome.Scan(
            ScanStats(
                pixels = job.width.toLong() * (job.rowTo - job.rowFrom),
                histogram = histogram.toList(),
                checksum = checksum,
            ),
        )
    }

    private fun prime() {
        for (offset in -job.radius..job.radius) {
            val slot = rows[slotOf(job.rowFrom + offset)]
            blurRow(job.rowFrom + offset, slot)
            for (x in 0 until job.width) columnSums[x] += slot[x]
        }
        primed = true
    }

    private fun slideWindow(y: Int) {
        if (y + 1 >= job.rowTo) return
        val leaving = rows[slotOf(y - job.radius)]
        for (x in 0 until job.width) columnSums[x] -= leaving[x]
        blurRow(y + job.radius + 1, leaving)
        for (x in 0 until job.width) columnSums[x] += leaving[x]
    }

    private fun slotOf(virtualRow: Int): Int = (virtualRow - (job.rowFrom - job.radius)) % window

    private fun blurRow(virtualRow: Int, target: IntArray) {
        val y = virtualRow.coerceIn(0, job.height - 1)
        val lastX = job.width - 1
        for (x in 0..lastX) sourceRow[x] = ScanPixels.value(x, y, job.seed)
        var sum = 0
        for (offset in -job.radius..job.radius) sum += sourceRow[offset.coerceIn(0, lastX)]
        for (x in 0..lastX) {
            target[x] = sum
            sum += sourceRow[(x + job.radius + 1).coerceAtMost(lastX)] - sourceRow[(x - job.radius).coerceAtLeast(0)]
        }
    }
}
