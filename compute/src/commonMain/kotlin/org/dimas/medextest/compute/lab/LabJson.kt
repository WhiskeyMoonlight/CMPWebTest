package org.dimas.medextest.compute.lab

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

object LabJson {
    val format: Json = Json { ignoreUnknownKeys = true }

    fun parsePage(json: String): List<LabResult> = format.decodeFromString(ListSerializer(LabResult.serializer()), json)
}
