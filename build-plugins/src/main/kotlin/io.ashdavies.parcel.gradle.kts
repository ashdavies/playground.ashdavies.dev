import org.gradle.kotlin.dsl.kotlin

pluginManager.withPlugin("com.android.application") {
    plugins { id("kotlin-parcelize") }
}

plugins {
    kotlin("multiplatform")
}

kotlin {
    commonMain.dependencies {
        implementation(project(":parcel-support"))
    }
}
