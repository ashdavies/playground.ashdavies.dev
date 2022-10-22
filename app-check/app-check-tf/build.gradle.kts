plugins {
    id("org.jetbrains.gradle.terraform") version "1.4.2"
}

terraform {
    sourceSets {
        main {
            executeDestroyOnlyIf { project.properties["tf.destroy"] == "true" }
            executeApplyOnlyIf { project.properties["tf.apply"] == "true"  }

            planVariable("project_dir", projectDir)
        }
    }

    showInitOutputInConsole = true
    showPlanOutputInConsole = true
    version = "1.3.1"
}
