plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation("com.android.tools.build:gradle:${libs.versions.google.android.get()}")
}

gradlePlugin {
    plugins.register("android-manifest") {
        implementationClass = "AndroidManifestPlugin"
        id = name
    }
}
