object ProjectDependencies {
    const val JUnit = "junit:junit:4.13"

    object AndroidX : DependencyGroup("androidx") {
        val activityKtx by artifact("$group.activity", "1.2.0-alpha08")
        val annotation by artifact("$group.annotation", "1.2.0-alpha01")

        object Compose : DependencyGroup("androidx.compose", "1.0.0-SNAPSHOT") {
            val foundation by artifact("$group.foundation")
            val material by artifact("$group.material")
            val navigation by artifact("$group.navigation")
            val runtime by artifact("$group.runtime")
            val ui by artifact( "$group.ui")
        }

        val coreKtx by artifact("$group.core", "1.5.0-alpha02")
        val datastoreCore by artifact("$group.datastore", "1.0.0-SNAPSHOT")
        val fragmentKtx by artifact("$group.fragment", "1.3.0-alpha08")

        object Lifecycle : DependencyGroup("androidx.lifecycle", "2.3.0-alpha07") {
            val lifecycleCommonJava8 by artifact()
            val lifecycleLivedataKtx by artifact()
            val lifecycleViewmodelKtx by artifact()
        }

        object Navigation : DependencyGroup("androidx.navigation", "2.3.0") {
            val navigationFragmentKtx by artifact()
            val navigationRuntimeKtx by artifact()
            val navigationUiKtx by artifact()
        }

        val pagingRuntime by artifact("$group.paging", "3.0.0-alpha06")

        object Room : DependencyGroup("androidx.room", "2.3.0-alpha02") {
            val roomCompiler by artifact()
            val roomKtx by artifact()
            val roomRuntime by artifact()
        }

        object Ui : DependencyGroup("androidx.ui", Compose.version) {
            val uiTooling by artifact()
            val uiTest by artifact()
        }
    }

    object Dropbox : DependencyGroup("com.dropbox.mobile") {
        val store4 by artifact("$group.store", "4.0.0-alpha07")
    }

    object Google : DependencyGroup("com.google") {
        object Firebase : DependencyGroup("$group.firebase") {
            val firebaseCommonKtx by artifact(version = "19.3.1")
            val firebaseAnalytics by artifact(version = "17.5.0")
        }

        object Android : DependencyGroup("$group.android") {
            val material by artifact("$group.material", "1.3.0-alpha02")
            val playServicesOssLicenses by artifact("$group.gms", "17.0.0")
        }

        val truth by artifact("$group.truth", "1.0.1")
    }

    object JetBrains : DependencyGroup("org.jetbrains") {
        object KotlinX : DependencyGroup("$group.kotlinx", "1.3.9") {
            val kotlinxCoroutinesAndroid by artifact()
            val kotlinxCoroutinesCore by artifact()
            val kotlinxCoroutinesTest by artifact()
        }
    }

    object Pinterest : DependencyGroup("com.pinterest") {
        val ktlint by artifact(version = "0.38.1")
    }

    object Square : DependencyGroup("com.squareup") {
        object Moshi : DependencyGroup("$group.moshi", "1.10.0") {
            val moshi by artifact()
            val moshiKotlinCodegen by artifact()
        }

        object Retrofit : DependencyGroup("$group.retrofit2", "2.9.0") {
            val converterMoshi by artifact()
            val retrofit by artifact()
        }
    }

    object Yaml : DependencyGroup("org.yaml") {
        val snakeYaml by artifact(version = "1.18:android")
    }
}
