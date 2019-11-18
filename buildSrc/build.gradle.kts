plugins {
  `kotlin-dsl`
}

repositories {
  google()
  maven("https://dl.bintray.com/kotlin/kotlin-eap")
  mavenCentral()
  jcenter()
}

dependencies {
  implementation("com.android.tools.build:gradle:4.0.0-alpha03")
}