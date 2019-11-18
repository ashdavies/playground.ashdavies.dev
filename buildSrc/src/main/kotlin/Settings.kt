import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.initialization.Settings

fun Settings.repositories(block: RepositoryHandler.() -> Unit) {
  pluginManagement {
    repositories.gradlePluginPortal()
    repositories.block()
  }

  gradle.allprojects {
    buildscript.repositories.block()
    repositories.block()
  }
}