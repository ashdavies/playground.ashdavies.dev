apply(from = "${rootProject.projectDir}/../repositories.gradle.kts")

plugins {
  `kotlin-dsl`
}

dependencies {
  implementation("com.android.tools.build:gradle:4.0.0-alpha03")
}
