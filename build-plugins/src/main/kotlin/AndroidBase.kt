@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

internal object AndroidBase : (Project, CommonExtension<*, *, *, *>) -> Unit {
    override fun invoke(target: Project, extension: CommonExtension<*, *, *, *>): Unit = extension.run {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = target.libs.versions.compose.get()
        }

        defaultConfig {
            compileSdk = 31
            minSdk = 23
        }
    }
}
