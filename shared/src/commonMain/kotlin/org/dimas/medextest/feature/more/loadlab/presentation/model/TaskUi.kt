package org.dimas.medextest.feature.more.loadlab.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class TaskUi(
    val id: Int,
    val title: String,
    val details: String,
    val status: TaskUiStatus,
    val progress: Float,
    val elapsed: String,
    val result: String?,
    val perf: String?,
)
