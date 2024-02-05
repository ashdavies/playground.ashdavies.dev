plugins {
    id("io.ashdavies.kotlin")
}

kotlin {
    js { browser() } // https://github.com/google/ksp/issues/980#issuecomment-1245928734

    commonMain.dependencies {
        implementation(libs.kotlinx.serialization.core)
    }
}
