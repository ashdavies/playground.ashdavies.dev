plugins {
    id("org.jetbrains.gradle.terraform") version "1.4.2"
}

dependencies {
    lambda(projects.appCheck.appCheckFunction)
}

terraform {
    sourceSets {
        main {
            executeDestroyOnlyIf { project.properties["tf.destroy"] == "true" }
            executeApplyOnlyIf { project.properties["tf.apply"] == "true"  }
        }
    }

    showInitOutputInConsole = true
    showPlanOutputInConsole = true
    version = "1.3.1"
}
