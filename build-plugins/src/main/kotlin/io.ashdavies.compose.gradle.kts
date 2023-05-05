plugins {
    id("org.jetbrains.compose")
    // id("app.cash.molecule")
    kotlin("multiplatform")
}

kotlin {
    jvm()

    commonMain.dependencies {
        implementation(compose.foundation)
        implementation(compose.material3)
        implementation(compose.runtime)
        implementation(compose.uiTooling)
        implementation(compose.ui)
    }

    jvmMain.dependencies {
        implementation(compose.desktop.currentOs)
    }
}
