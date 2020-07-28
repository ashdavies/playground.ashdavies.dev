object ProjectDependencies {
    const val JUnit = "junit:junit:4.13"

    object AndroidX {
        const val activity = "androidx.activity:activity-ktx:1.2.0-alpha06"
        const val annotation = "androidx.annotation:annotation:1.2.0-alpha01"
        const val appCompat = "androidx.appcompat:appcompat:1.3.0-alpha01"
        const val cardView = "androidx.cardview:cardview:1.0.0"

        object Compose : DependencyGroup("androidx.compose", "0.1.0-dev14") {
            val compiler by dependency("compose-compiler")
            val runtime by dependency("compose-runtime")
        }

        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.0-beta8"
        const val coordinatorLayout = "androidx.coordinatorlayout:coordinatorlayout:1.1.0"
        const val core = "androidx.core:core-ktx:1.5.0-alpha01"
        const val dataStore = "androidx.datastore:datastore-core:1.0.0-SNAPSHOT"
        const val fragment = "androidx.fragment:fragment-ktx:1.3.0-alpha06"

        object Lifecycle : DependencyGroup("androidx.lifecycle", "2.3.0-alpha05") {
            val common by dependency("lifecycle-common-java8")
            val liveData by dependency("lifecycle-livedata-ktx")
            val viewModel by dependency("lifecycle-viewmodel-ktx")
        }

        const val paging = "androidx.paging:paging-runtime:3.0.0-alpha02"

        object Room : DependencyGroup("androidx.room", "2.3.0-alpha01") {
            val compiler by dependency("room-compiler")
            val room by dependency("room-ktx")
            val runtime by dependency("room-runtime")
        }

        object Navigation : DependencyGroup("androidx.navigation", "2.3.0") {
            val fragment by dependency("navigation-fragment-ktx")
            val ui by dependency("navigation-ui-ktx")
        }

        const val recyclerView = "androidx.recyclerview:recyclerview:1.2.0-alpha04"

        object Ui : DependencyGroup("androidx.ui", "0.1.0-dev14") {
            val animation by dependency("ui-animation")
            val foundation by dependency("ui-foundation")
            val framework by dependency("ui-framework")
            val layout by dependency("ui-layout")
            val material by dependency("ui-material")
            val tooling by dependency("ui-tooling")
        }

        const val viewBinding = "androidx.databinding:viewbinding:4.2.0-alpha05"
    }

    object Dropbox {
        const val store = "com.dropbox.mobile.store:store4:4.0.0-alpha06"
    }

    object Google {
        object Firebase : DependencyGroup("com.google.firebase") {
            val common by dependency("firebase-common-ktx", "19.3.0")
            val analytics by dependency("firebase-analytics", "17.4.4")
            val firestore by dependency("firebase-firestore-ktx", "21.5.0")
        }

        const val materialDesign = "com.google.android.material:material:1.3.0-alpha01"
        const val ossLicences = "com.google.android.gms:play-services-oss-licenses:17.0.0"
        const val truth = "com.google.truth:truth:1.0.1"
    }

    object JetBrains {
        object Kotlin {
            const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:1.3.72"

            object Coroutines : DependencyGroup("org.jetbrains.kotlinx", "1.3.7") {
                val android by dependency("kotlinx-coroutines-android")
                val core by dependency("kotlinx-coroutines-core")
                val play by dependency("kotlinx-coroutines-play-services")
                val test by dependency("kotlinx-coroutines-test")
            }
        }
    }

    object Pinterest : DependencyGroup("com.pinterest") {
        const val ktlint = "com.pinterest:ktlint:0.36.0"
    }
}
