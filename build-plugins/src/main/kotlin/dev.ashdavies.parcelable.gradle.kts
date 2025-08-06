project.withAndroidPlugin {
    plugins { id("kotlin-parcelize") }
}

plugins {
    kotlin("multiplatform")
}

kotlin {
    androidTarget {
        compilerOptions {
            val additionalAnnotation = "org.jetbrains.kotlin.parcelize:additionalAnnotation"
            val parcelizeAnnotation = "io.ashdavies.parcelable.Parcelize"

            freeCompilerArgs.addParameter("plugin:$additionalAnnotation=$parcelizeAnnotation")
        }
    }

    sourceSets.commonMain.dependencies {
        implementation(project(":parcelable-support"))
    }
}

private fun ListProperty<String>.addParameter(value: String) {
    addAll("-P", value)
}
