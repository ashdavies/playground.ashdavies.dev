plugins {
    id("org.jetbrains.compose")
    kotlin("multiplatform")
}

compose {
    val composeCompiler = libs.compose.compiler.get()
    kotlinCompilerPlugin.set("$composeCompiler")
}

kotlin {
    jvm()
}
