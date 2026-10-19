import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        webMain.dependencies {
            implementation(project(":compute"))
            implementation(libs.wrappers.browser)
        }
        matching { it.name.startsWith("web") || it.name.startsWith("js") || it.name.startsWith("wasmJs") }.configureEach {
            languageSettings.optIn("kotlin.js.ExperimentalWasmJsInterop")
        }
    }
}
