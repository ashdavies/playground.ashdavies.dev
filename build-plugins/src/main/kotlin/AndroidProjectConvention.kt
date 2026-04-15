import org.gradle.api.Project
import org.gradle.api.provider.Provider

internal val Project.androidProjectConventionProvider: Provider<AndroidProjectConvention>
    get() = provider { androidProjectConvention }

internal val Project.androidProjectConvention: AndroidProjectConvention
    get() = AndroidProjectConvention(
        compileSdkVersion = libs.versions.android.compileSdk.get(),
        minSdkVersion = libs.versions.android.minSdk.get(),
    )

internal data class AndroidProjectConvention(
    val compileSdkVersion: String,
    val minSdkVersion: String,
)
