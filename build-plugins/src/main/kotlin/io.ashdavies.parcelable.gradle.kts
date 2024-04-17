import org.gradle.kotlin.dsl.kotlin

pluginManager.withPlugin("com.android.library") {
    plugins { id("kotlin-parcelize") }
}

plugins {
    kotlin("multiplatform")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(project(":parcelable-support"))
    }
}
