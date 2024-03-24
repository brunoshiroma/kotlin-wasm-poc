import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)

    alias(libs.plugins.jetbrainsCompose)
}

group = "com.brunoshiroma"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs() {
        binaries.executable()
        moduleName = "kotlin-wasm-poc"
        browser {
            testTask {
                useKarma {
                    webpackConfig.experiments = mutableSetOf("topLevelAwait")
                    useChrome()
                }
            }
            commonWebpackConfig {
                outputFileName = "kotlinWasmPoc.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(project.projectDir.path)
                    }
                }
            }
        }
    }
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
        }
    }
    dependencies {
        commonMainImplementation(npm("webpack-dev-middleware", "5.3.4"))
    }
}

compose.experimental {
    web.application {}
}