import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

internal object Playground {

    const val jvmTarget = "11"

    val freeCompilerArgs = listOf(
        "-opt-in=kotlin.RequiresOptIn",
        "-Xallow-result-return-type",
        "-Xmulti-platform"
    )

    object Dependencies {

        @OptIn(ExperimentalComposeLibrary::class)
        fun KotlinDependencyHandler.compose() {
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.runtime)
            implementation(compose.uiTooling)
            implementation(compose.ui)
        }
    }
}

@Suppress("UnstableApiUsage")
internal fun CommonExtension<*, *, *, *>.configureCommon() {
    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    defaultConfig {
        compileSdk = 31
        minSdk = 21
    }

    sourceSets.configureEach {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
    }
}

@Suppress("UNUSED_VARIABLE")
internal fun KotlinMultiplatformExtension.configureKotlinMultiplatform(target: Project) = target.run {
    explicitApiWarning()
    android()
    jvm()

    sourceSets.all {
        languageSettings.optIn("kotlin.RequiresOptIn")
    }

    commonMain {
        with(Playground.Dependencies) { compose() }
        implementation(libs.bundles.arkivanov.decompose)
        implementation(libs.bundles.jetbrains.kotlinx)
        implementation(libs.oolong)
    }

    commonTest {
        implementation(libs.bundles.jetbrains.kotlin.test)
    }

    androidMain {
        implementation(libs.androidx.annotation)
        implementation(libs.androidx.core.ktx)
        implementation(libs.google.android.material)
        implementation(libs.jetbrains.kotlinx.coroutines.android)
    }

    jvmMain {
        implementation(compose.desktop.currentOs)
        implementation(libs.jetbrains.kotlinx.coroutines.swing)
    }
}

internal fun KotlinMultiplatformExtension.commonMain(block: KotlinDependencyHandler.() -> Unit) =
    dependencies("commonMain", block)

internal fun KotlinMultiplatformExtension.commonTest(block: KotlinDependencyHandler.() -> Unit) =
    dependencies("commonTest", block)

internal fun KotlinMultiplatformExtension.androidMain(block: KotlinDependencyHandler.() -> Unit) =
    dependencies("androidMain", block)

internal fun KotlinMultiplatformExtension.jvmMain(block: KotlinDependencyHandler.() -> Unit) =
    dependencies("jvmMain", block)

private fun KotlinMultiplatformExtension.dependencies(name: String, block: KotlinDependencyHandler.() -> Unit) =
    sourceSets.getByName(name).dependencies(block)
