package org.dimas.medextest.feature.more.loadlab.presentation.util

import org.dimas.medextest.core.compute.ExecutionMode

internal val ExecutionMode.label: String
    get() = when (this) {
        ExecutionMode.Main -> "Main"
        ExecutionMode.Default -> "Default"
        ExecutionMode.Parallel -> "Parallel"
        ExecutionMode.Cooperative -> "Нарезка"
        ExecutionMode.Worker -> "Web Worker"
    }

internal val ExecutionMode.description: String
    get() = when (this) {
        ExecutionMode.Main -> "Всё в UI-потоке через Dispatchers.Main — антипаттерн для сравнения"
        ExecutionMode.Default -> "withContext(Dispatchers.Default), одна корутина"
        ExecutionMode.Parallel -> "async на Dispatchers.Default по числу ядер"
        ExecutionMode.Cooperative -> "Кванты по 6 мс в UI-потоке, между ними уступаем кадр"
        ExecutionMode.Worker -> "Пул Web Worker на Kotlin/Wasm, данные через postMessage"
    }
