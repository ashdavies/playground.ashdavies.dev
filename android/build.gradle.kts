import com.android.build.api.dsl.VariantDimension
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty

plugins {
    `multiplatform-application`
}

android {
    defaultConfig {
        val googleClientId by buildConfigField()
        val playgroundApiKey by buildConfigField()
    }
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":auth-oauth"))
                implementation(project(":local-storage"))
                implementation(project(":playground-app"))

                implementation(libs.alialbaali.kamel)
                implementation(libs.bundles.arkivanov.decompose)
                implementation(libs.kuuuurt.multiplatform.paging)
                implementation(libs.sqldelight.coroutines.extensions)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.bundles.androidx.activity)
                implementation(libs.bundles.androidx.paging)
                implementation(libs.bundles.androidx.viewmodel)

                with(libs.google.accompanist) {
                    implementation(flowlayout)
                    implementation(insetsUi)
                    implementation(placeholderMaterial)
                    implementation(swiperefresh)
                }

                implementation(libs.bundles.google.firebase)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.bundles.androidx.paging)
            }
        }
    }
}

fun VariantDimension.buildConfigField(): PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, String>> {
    return SystemProperty { name, value -> buildConfigField("String", name, "\"$value\"") }
}
