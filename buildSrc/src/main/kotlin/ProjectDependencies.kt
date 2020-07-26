object ProjectDependencies {
    const val JUnit = "junit:junit:4.13"

    object AndroidX {
        const val Activity = "androidx.activity:activity-ktx:1.2.0-alpha06"
        const val Annotation = "androidx.annotation:annotation:1.2.0-alpha01"
        const val AppCompat = "androidx.appcompat:appcompat:1.3.0-alpha01"
        const val CardView = "androidx.cardview:cardview:1.0.0"

        object Compose {
            const val Compiler = "androidx.compose:compose-compiler:0.1.0-dev14"
            const val Runtime = "androidx.compose:compose-runtime:0.1.0-dev14"
        }

        const val ConstraintLayout = "androidx.constraintlayout:constraintlayout:2.0.0-beta8"
        const val CoordinatorLayout = "androidx.coordinatorlayout:coordinatorlayout:1.1.0"
        const val Core = "androidx.core:core-ktx:1.5.0-alpha01"
        const val DataStore = "androidx.datastore:datastore-core:1.0.0-SNAPSHOT"
        const val Fragment = "androidx.fragment:fragment-ktx:1.3.0-alpha06"

        object Lifecycle {
            const val Common = "androidx.lifecycle:lifecycle-common-java8:2.2.0"
            const val LiveData = "androidx.lifecycle:lifecycle-livedata-ktx:2.3.0-alpha05"
            const val ViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.0-alpha05"
        }

        const val Paging = "androidx.paging:paging-runtime:3.0.0-alpha02"

        object Room {
            const val Compiler = "androidx.room:room-compiler:2.3.0-alpha01"
            const val Room = "androidx.room:room-ktx:2.3.0-alpha01"
            const val Runtime = "androidx.room:room-runtime:2.3.0-alpha01"
        }

        object Navigation {
            const val Fragment = "androidx.navigation:navigation-fragment-ktx:2.3.0"
            const val Ui = "androidx.navigation:navigation-ui-ktx:2.3.0"
        }

        const val RecyclerView = "androidx.recyclerview:recyclerview:1.2.0-alpha04"

        object Ui {
            const val Animation = "androidx.ui:ui-animation:0.1.0-dev14"
            const val Foundation = "androidx.ui:ui-foundation:0.1.0-dev14"
            const val Framework = "androidx.ui:ui-framework:0.1.0-dev14"
            const val Layout = "androidx.ui:ui-layout:0.1.0-dev14"
            const val Material = "androidx.ui:ui-material:0.1.0-dev14"
            const val Tooling = "androidx.ui:ui-tooling:0.1.0-dev14"
        }

        const val ViewBinding = "androidx.databinding:viewbinding:4.2.0-alpha05"
    }

    object Dropbox {
        const val Store = "com.dropbox.mobile.store:store4:4.0.0-alpha06"
    }

    object GitHub {
        const val LifecycleFlow = "com.github.ashdavies:lifecycle-flow:0.7.2"
    }

    object Google {
        object Firebase {
            const val Common = "com.google.firebase:firebase-common-ktx:19.3.0"
            const val Analytics = "com.google.firebase:firebase-analytics:17.4.4"
            const val Firestore = "com.google.firebase:firebase-firestore-ktx:21.5.0"
        }

        const val MaterialDesign = "com.google.android.material:material:1.3.0-alpha01"
        const val OssLicences = "com.google.android.gms:play-services-oss-licenses:17.0.0"
        const val Truth = "com.google.truth:truth:1.0.1"
    }

    object JetBrains {
        object Kotlin {
            const val StdLib = "org.jetbrains.kotlin:kotlin-stdlib:1.4-M3"

            object Coroutines {
                const val Android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.7"
                const val Core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7"
                const val Test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.7"
            }
        }
    }

    object Pinterest {
        const val KtLint = "com.pinterest:ktlint:0.36.0"
    }
}
