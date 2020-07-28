plugins {
    id("com.android.library")

    id("kotlin-android")
    id("kotlin-kapt")
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
    implementation(ProjectDependencies.AndroidX.coordinatorLayout)
    implementation(ProjectDependencies.AndroidX.Lifecycle.common)
    implementation(ProjectDependencies.AndroidX.Lifecycle.liveData)
    implementation(ProjectDependencies.AndroidX.Navigation.fragment)
    implementation(ProjectDependencies.AndroidX.Room.runtime)
    implementation(ProjectDependencies.AndroidX.viewBinding)
    implementation(ProjectDependencies.Google.Firebase.firestore)
    implementation(ProjectDependencies.Google.materialDesign)
    implementation(ProjectDependencies.JetBrains.Kotlin.stdLib)

    testImplementation(ProjectDependencies.Google.truth)
    testImplementation(ProjectDependencies.JetBrains.Kotlin.Coroutines.test)
    testImplementation(ProjectDependencies.JUnit)
}
