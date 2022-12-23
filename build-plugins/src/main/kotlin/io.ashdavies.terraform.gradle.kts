plugins {
    id("org.jetbrains.gradle.terraform")
}

terraform {
    sourceSets.main {
        executeDestroyOnlyIf { project.hasProperty("tf.destroy") }
        executeApplyOnlyIf { project.hasProperty("tf.apply") }
    }

    showInitOutputInConsole = true
    showPlanOutputInConsole = true
    version = "1.3.1"
}
