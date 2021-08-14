@file:Suppress("SpellCheckingInspection")

object ProjectDependencies {

    val AnvilVersion: String
        get() = "2.3.3"

    val KotlinVersion: String?
        get() = JetBrains
            .Kotlin
            .version

    val Ktlint: String
        get() = "10.1.0"

    val BenManesVersions: String
        get() = "0.39.0"

    val SqlDelightVersion: String?
        get() = Square
            .SqlDelight
            .version

    object AndroidX : DependencyGroup("androidx") {

        val activityKtx by artifact("$group.activity", "1.3.1")
        val annotation by artifact("$group.annotation", "1.3.0-alpha01")

        object Compose : DependencyGroup("androidx.compose", "1.1.0-alpha01") {

            val foundation by artifact("$group.foundation")
            val material by artifact("$group.material")
            val runtime by artifact("$group.runtime")
            val ui by artifact("$group.ui")
            val uiTest by artifact("$group.ui")
            val uiTooling by artifact("$group.ui")
        }

        val coreKtx by artifact("$group.core", "1.7.0-alpha01")
        val datastoreCore by artifact("$group.datastore", "1.0.0")
        val fragmentKtx by artifact("$group.fragment", "1.4.0-alpha06")

        object Lifecycle : DependencyGroup("androidx.lifecycle", "2.4.0-alpha03") {

            val lifecycleCommonJava8 by artifact()
            val lifecycleLivedataKtx by artifact()
            val lifecycleViewmodelKtx by artifact()
        }

        object Navigation : DependencyGroup("androidx.navigation", "2.4.0-alpha06") {

            val navigationCompose by artifact()
            val navigationRuntimeKtx by artifact()
            val navigationUiKtx by artifact()
        }

        val pagingRuntime by artifact("$group.paging", "3.1.0-alpha03")
    }

    object ApolloGraphQl : DependencyGroup("com.apollographql.apollo", "2.5.9") {

        val apolloRuntime by artifact()
        val apolloCoroutinesSupport by artifact()
    }

    object Dropbox : DependencyGroup("com.dropbox.mobile") {

        val store4 by artifact("$group.store", "4.0.2-KT15")
    }

    object Google : DependencyGroup("com.google") {

        object Accompanist : DependencyGroup("$group.accompanist", "0.16.1") {

            val accompanistCoil by artifact(version = "0.15.0")
            val accompanistFlowlayout by artifact()
            val accompanistInsets by artifact()
            val accompanistInsetsUi by artifact()
            val accompanistPlaceholderMaterial by artifact()
            val accompanistSystemuicontroller by artifact()
        }

        object CloudFunctions : DependencyGroup("$group.cloud.functions") {

            val functionsFrameworkApi by artifact(version = "1.0.4")
            val javaFunctionInvoker by artifact("$group.invoker", "1.0.2")
        }

        object Firebase : DependencyGroup("$group.firebase") {

            val firebaseAdmin by artifact(version = "8.0.1")
            val firebaseCommonKtx by artifact(version = "20.0.0")
            val firebaseAnalytics by artifact(version = "19.0.0")
        }

        object Android : DependencyGroup("$group.android") {

            val material by artifact("$group.material", "1.5.0-alpha02")
        }
    }

    object JetBrains : DependencyGroup("org.jetbrains") {

        object Kotlin : DependencyGroup("$group.kotlin", "1.5.21") {

            val kotlinTest by artifact()
            val kotlinTestCommon by artifact()
            val kotlinTestJs by artifact()
            val kotlinTestJunit by artifact()
            val kotlinTestAnnotationsCommon by artifact()
        }

        object KotlinX : DependencyGroup("$group.kotlinx", "1.5.1") {

            val kotlinxCoroutinesAndroid by artifact()
            val kotlinxCoroutinesCore by artifact()
            val kotlinxCoroutinesTest by artifact()
            val kotlinxDatetime by artifact(version = "0.2.1")
            val kotlinxNodejs by artifact(version = "0.0.7")
            val kotlinxSerializationCore by artifact(version = "1.2.2")
            val kotlinxSerializationJson by artifact(version = "1.2.2")
        }
    }

    object Ktor : DependencyGroup("io.ktor", "1.6.2") {

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

        object SqlDelight : DependencyGroup("$group.sqldelight", "1.5.1") {

            val androidDriver by artifact()
            val coroutinesExtensions by artifact()
            val sqliteDriver by artifact()
            val runtime by artifact()
        }
    }

    object YamlKt : DependencyGroup("net.mamoe.yamlkt") {

        val yamlkt by artifact(version = "0.9.0")
    }
}
