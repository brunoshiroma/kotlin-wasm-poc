import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

group = "com.brunoshiroma"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
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
        val wasmJsMain by getting {
            dependencies {
                implementation(npm("body-parser", "1.20.3"))
                implementation(npm("ws", "8.17.1"))
                implementation(npm("node-forge", "1.3.2"))
                implementation(npm("http-proxy-middleware", "2.0.7"))

            }
        }
    }
    dependencies {
<<<<<<< Updated upstream
        commonMainImplementation(npm("webpack-dev-middleware", "7.4.2"))
=======
        commonMainImplementation(npm("webpack-dev-middleware", "7.4.5"))
>>>>>>> Stashed changes
    }
}

compose.experimental {
    web.application {}
}