plugins {
    id("dev.ashdavies.android.library")
    id("dev.ashdavies.jvm")
    id("dev.ashdavies.kotlin")
    id("dev.ashdavies.wasm")

    id("kotlin-parcelize")
}

kotlin {
    android {
        namespace = "dev.ashdavies.parcelable"
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}
