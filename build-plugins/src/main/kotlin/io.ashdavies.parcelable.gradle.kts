import org.gradle.kotlin.dsl.kotlin

pluginManager.withPlugin("com.android.library") {
    plugins { id("kotlin-parcelize") }
}

plugins {
    kotlin("multiplatform")
}

kotlin {
    commonMain.dependencies {
        implementation(project(":parcelable-support"))
    }
}
