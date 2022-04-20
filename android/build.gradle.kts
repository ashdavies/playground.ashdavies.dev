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
                implementation(project(":compose-local"))
                implementation(project(":local-storage"))
                implementation(project(":playground-app"))

                implementation(libs.alialbaali.kamel)

                with(libs.arkivanov) {
                    implementation(decompose.extensions)
                    implementation(decompose)
                }

                with(libs.ktor.client) {
                    implementation(contentNegotiation)
                    implementation(core)
                    implementation(json)
                    implementation(logging)
                }

                implementation(libs.ktor.serialization.json)
                implementation(libs.kuuuurt.multiplatformPaging)
                implementation(libs.sqlDelight.coroutinesExtensions)
            }
        }

        val androidMain by getting {
            dependencies {
                with(libs.androidx) {
                    implementation(activityCompose)
                    implementation(activityKtx)
                    implementation(pagingCompose)
                    implementation(pagingRuntime)
                }

                with(libs.androidx.lifecycle) {
                    implementation(viewModelCompose)
                    implementation(viewModelKtx)
                }

                with(libs.google.accompanist) {
                    implementation(flowlayout)
                    implementation(placeholderMaterial)
                    implementation(swiperefresh)
                }

                with(libs.google.firebase) {
                    implementation(analytics)
                    implementation(commonKtx)
                }

                implementation(libs.ktor.client.cio)
            }
        }

        val jvmMain by getting {
            dependencies {
                with(libs.androidx) {
                    implementation(pagingCompose)
                    implementation(pagingRuntime)
                }
            }
        }
    }
}

fun VariantDimension.buildConfigField(): PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, String>> {
    return SystemProperty { name, value -> buildConfigField("String", name, "\"$value\"") }
}
