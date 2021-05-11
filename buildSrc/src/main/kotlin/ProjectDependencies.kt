object ProjectDependencies {

    object AndroidX : DependencyGroup("androidx") {

        val activityKtx by artifact("$group.activity", "1.3.0-alpha06")
        val annotation by artifact("$group.annotation", "1.3.0-alpha01")

        object Compose : DependencyGroup("androidx.compose", "1.0.0-beta04") {

            val foundation by artifact("$group.foundation")
            val material by artifact("$group.material")
            val runtime by artifact("$group.runtime")
            val ui by artifact("$group.ui")
            val uiTest by artifact("$group.ui")
            val uiTooling by artifact("$group.ui")
        }

        val coreKtx by artifact("$group.core", "1.6.0-alpha02")
        val datastoreCore by artifact("$group.datastore", "1.0.0")
        val fragmentKtx by artifact("$group.fragment", "1.3.2")

        object Lifecycle : DependencyGroup("androidx.lifecycle", "2.4.0-alpha01") {

            val lifecycleCommonJava8 by artifact()
            val lifecycleLivedataKtx by artifact()
            val lifecycleViewmodelKtx by artifact()
        }

        object Navigation : DependencyGroup("androidx.navigation", "2.3.5") {

            val navigationCompose by artifact(version = "1.0.0-alpha10")
            val navigationRuntimeKtx by artifact()
            val navigationUiKtx by artifact()
        }

        val pagingRuntime by artifact("$group.paging", "3.0.0-beta03")
    }

    object Dropbox : DependencyGroup("com.dropbox.mobile") {

        val store4 by artifact("$group.store", "4.0.0")
    }

    object Google : DependencyGroup("com.google") {

        object Firebase : DependencyGroup("$group.firebase") {

            val firebaseCommonKtx by artifact(version = "19.5.0")
            val firebaseAnalytics by artifact(version = "18.0.3")
        }

        object Android : DependencyGroup("$group.android") {

            val material by artifact("$group.material", "1.4.0-alpha02")
            val playServicesOssLicenses by artifact("$group.gms", "17.0.0")
        }
    }

    object JetBrains : DependencyGroup("org.jetbrains") {

        object Kotlin : DependencyGroup("$group.kotlin", "1.4.32") {

            val kotlinTest by artifact()
            val kotlinTestCommon by artifact()
            val kotlinTestJs by artifact()
            val kotlinTestJunit by artifact()
            val kotlinTestAnnotationsCommon by artifact()
        }

        object KotlinX : DependencyGroup("$group.kotlinx", "1.4.3") {

            val kotlinxCoroutinesAndroid by artifact()
            val kotlinxCoroutinesCore by artifact()
            val kotlinxCoroutinesTest by artifact()
            val kotlinxDatetime by artifact(version = "0.1.1")
            val kotlinxNodejs by artifact(version = "0.0.7")
            val kotlinxSerializationJson by artifact(version = "1.1.0")
        }
    }

    object Ktor : DependencyGroup("io.ktor", "1.5.3") {

        val ktorClientAndroid by artifact()
        val ktorClientCore by artifact()
        val ktorClientCurl by artifact()
        val ktorClientIos by artifact()
        val ktorClientJson by artifact()
        val ktorClientLogging by artifact()
        val ktorClientSerialization by artifact()
    }

    object Pinterest : DependencyGroup("com.pinterest") {

        val ktlint by artifact(version = "0.40.0")
    }

    object Square : DependencyGroup("com.squareup") {

        object SqlDelight : DependencyGroup("$group.sqldelight", "1.5.0") {

            val androidDriver by artifact()
            val coroutinesExtensions by artifact()
            val sqljsDriver by artifact()
            val sqliteDriver by artifact()
            val runtime by artifact()
        }
    }
}
