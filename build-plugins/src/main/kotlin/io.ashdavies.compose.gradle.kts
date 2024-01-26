plugins {
    id("org.jetbrains.compose")
    // id("app.cash.molecule")
    kotlin("multiplatform")
}

commonExtension {
    buildFeatures {
        compose = true
    }

    composeOptions {
        val composeCompilerVersion = libs.versions.compose.compiler.get()
        kotlinCompilerExtensionVersion = composeCompilerVersion
    }
}

compose {
    val composeCompiler = libs.compose.compiler.get()
    kotlinCompilerPlugin.set("$composeCompiler")
}

kotlin {
    jvm()
}

pluginManager.withPlugin("com.android.library") {
    plugins { id("kotlin-parcelize") }
}
