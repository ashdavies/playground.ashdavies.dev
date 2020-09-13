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

    sourceSets {
        getByName("main")
            .java
            .srcDir("src/main/kotlin")

        getByName("test")
            .java
            .srcDirs("src/test/kotlin")
    }
}

dependencies {
    implementation(ProjectDependencies.AndroidX.annotation)
    implementation(ProjectDependencies.AndroidX.Compose.runtime)
    implementation(ProjectDependencies.AndroidX.Compose.ui)
    implementation(ProjectDependencies.AndroidX.Lifecycle.lifecycleCommonJava8)
    implementation(ProjectDependencies.AndroidX.Lifecycle.lifecycleLivedataKtx)
    implementation(ProjectDependencies.AndroidX.Navigation.navigationFragmentKtx)
    implementation(ProjectDependencies.AndroidX.Room.roomRuntime)
    implementation(ProjectDependencies.Google.Android.material)

    testImplementation(ProjectDependencies.Google.truth)
    testImplementation(ProjectDependencies.JetBrains.KotlinX.kotlinxCoroutinesTest)
    testImplementation(ProjectDependencies.JUnit)
}
