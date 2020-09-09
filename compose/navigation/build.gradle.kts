plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    setCompileSdkVersion(29)

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    defaultConfig {
        setMinSdkVersion(21)
        setTargetSdkVersion(29)
    }

    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }
}

dependencies {
    implementation(ProjectDependencies.AndroidX.Compose.runtime)
    implementation(ProjectDependencies.AndroidX.Compose.ui)
    implementation(ProjectDependencies.AndroidX.Navigation.runtime)
}
