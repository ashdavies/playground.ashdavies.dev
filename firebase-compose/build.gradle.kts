plugins {
    id("io.ashdavies.default")
}

android {
    namespace = "io.ashdavies.firebase.compose"
}

kotlin {
    commonMain.dependencies {
        implementation(projects.composeLocals)
    }

    androidMain.dependencies {
        with(libs.google.firebase) {
            implementation(dependencies.platform(bom))
            implementation(analytics)
            implementation(common.ktx)
        }
    }
}
