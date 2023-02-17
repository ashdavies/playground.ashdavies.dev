import org.jetbrains.gradle.plugins.terraform.tasks.AbstractTerraformExec

plugins {
    id("io.ashdavies.terraform")
}

terraform {
    sourceSets.main {
        planVariables = mapOf("gh_token" to System.getenv("GITHUB_TOKEN"))
    }
}
