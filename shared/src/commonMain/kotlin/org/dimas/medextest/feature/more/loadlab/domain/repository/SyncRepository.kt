package org.dimas.medextest.feature.more.loadlab.domain.repository

interface SyncRepository {
    suspend fun push(recordId: Int): Int
}
