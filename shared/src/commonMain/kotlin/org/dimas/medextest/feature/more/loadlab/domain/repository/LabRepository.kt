package org.dimas.medextest.feature.more.loadlab.domain.repository

interface LabRepository {
    val pageSize: Int

    suspend fun fetchPage(page: Int): String
}
