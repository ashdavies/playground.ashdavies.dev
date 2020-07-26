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
    implementation(ProjectDependencies.AndroidX.Annotation)
    implementation(ProjectDependencies.AndroidX.CoordinatorLayout)
    implementation(ProjectDependencies.AndroidX.Lifecycle.Common)
    implementation(ProjectDependencies.AndroidX.Lifecycle.LiveData)
    implementation(ProjectDependencies.AndroidX.Navigation.Fragment)
    implementation(ProjectDependencies.AndroidX.Room.Runtime)
    implementation(ProjectDependencies.AndroidX.ViewBinding)
    implementation(ProjectDependencies.Google.Firebase.Firestore)
    implementation(ProjectDependencies.Google.MaterialDesign)
    implementation(ProjectDependencies.JetBrains.Kotlin.StdLib)

    testImplementation(ProjectDependencies.Google.Truth)
    testImplementation(ProjectDependencies.JetBrains.Kotlin.Coroutines.Test)
    testImplementation(ProjectDependencies.JUnit)
}
