import ProjectDependencies.AndroidX
import ProjectDependencies.JetBrains
import ProjectDependencies.Ktor
import ProjectDependencies.Square
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    `android-library`
    `kotlin-multiplatform`
    serialization
    sqldelight
}

android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    configurations {
        // https://youtrack.jetbrains.com/issue/KT-43944
        create("testApi", "testDebugApi", "testReleaseApi")
    }

    sourceSets.forEach { sourceSet ->
        sourceSet
            .manifest
            .srcFile("src/androidMain/AndroidManifest.xml")
    }

    defaultConfig {
        compileSdk = 30
        minSdk = 21
        targetSdk = 30
    }
}

kotlin {
    android()

    jvm {
        val main by compilations.getting {
            kotlinOptions {
                java {
                    sourceCompatibility = JavaVersion.VERSION_1_8
                    targetCompatibility = JavaVersion.VERSION_1_8
                    jvmTarget = "1.8"
                }
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(JetBrains.KotlinX.kotlinxDatetime)
                implementation(JetBrains.KotlinX.kotlinxCoroutinesCore)
                implementation(JetBrains.KotlinX.kotlinxSerializationJson)
                implementation(Ktor.ktorClientCore)
                implementation(Ktor.ktorClientJson)
                implementation(Ktor.ktorClientLogging)
                implementation(Ktor.ktorClientSerialization)
                implementation(Square.SqlDelight.coroutinesExtensions)
                implementation(Square.SqlDelight.runtime)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(JetBrains.Kotlin.kotlinTestCommon)
                implementation(JetBrains.Kotlin.kotlinTestAnnotationsCommon)
            }
        }

        val commonJvmMain by creating {
            dependsOn(commonMain)
        }

        val jvmMain by getting {
            dependsOn(commonJvmMain)

            dependencies {
                implementation(Square.SqlDelight.sqliteDriver)
            }
        }

        val androidMain by getting {
            dependsOn(commonJvmMain)

            dependencies {
                implementation(AndroidX.coreKtx)
                implementation(JetBrains.KotlinX.kotlinxCoroutinesAndroid)
                implementation(Ktor.ktorClientAndroid)
                implementation(Square.SqlDelight.androidDriver)
            }
        }

        val androidTest by getting {
            dependencies {
                implementation(JetBrains.Kotlin.kotlinTest)
                implementation(JetBrains.Kotlin.kotlinTestJunit)
            }
        }
    }
}

sqldelight {
    database("PlaygroundDatabase") {
        packageName = "io.ashdavies.playground.database"
    }
}

val KotlinSourceSet.commonJvmMain: String
    get() = "src/commonJvmMain/kotlin"

fun NamedDomainObjectContainer<*>.create(vararg names: String) {
    names.forEach { create(it) }
}

fun KotlinSourceSet.dependsOn(srcPath: String) {
    kotlin.srcDir(srcPath)
}
