import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
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
        commonMain.dependencies {
            implementation(project(":shared"))

            implementation(libs.compose.ui)
        }
    }
}
val webWorker = project(":webWorker")

listOf("wasmJsProcessResources", "jsProcessResources").forEach { taskName ->
    tasks.named<ProcessResources>(taskName) {
        dependsOn(":webWorker:wasmJsBrowserDistribution", ":webWorker:jsBrowserDistribution")
        from(webWorker.layout.buildDirectory.dir("dist/wasmJs/productionExecutable")) {
            into("worker/wasm")
            exclude("*.map")
        }
        from(webWorker.layout.buildDirectory.dir("dist/js/productionExecutable")) {
            into("worker/js")
            exclude("*.map")
        }
    }
}
