pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    jcenter()
  }

  resolutionStrategy {
    eachPlugin {
      if (requested.id.id == "com.android.library") useModule("com.android.tools.build:gradle:${requested.version}")
      else if (requested.id.id == "org.jetbrains.kotlin.android") useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
    }
  }
}
