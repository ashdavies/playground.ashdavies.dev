plugins {
    id("org.jetbrains.compose")
    // id("app.cash.molecule")
    kotlin("multiplatform")
}

compose {
    val composeCompiler = libs.jetbrains.compose.compiler.get()
    kotlinCompilerPlugin.set("$composeCompiler")
}

kotlin {
    jvm()
}
