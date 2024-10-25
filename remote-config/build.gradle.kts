plugins {
    id("io.ashdavies.compose")
    id("io.ashdavies.default")
}

android {
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    dependencies {
        coreLibraryDesugaring(libs.android.tools.desugarjdk)
    }

    namespace = "io.ashdavies.config"
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.gitlive.firebase.config)
    }
}
