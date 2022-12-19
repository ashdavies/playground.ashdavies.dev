plugins {
    id("org.jetbrains.gradle.terraform") version "1.4.2"
}

dependencies {
    lambda(projects.appCheck.appCheckFunction)
    lambda(projects.eventsAggregator)
    lambda(projects.cloudRun)
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
