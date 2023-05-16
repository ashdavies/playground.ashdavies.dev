plugins {
    id("io.ashdavies.terraform")
}

terraform {
    showPlanOutputInConsole = false
    showInitOutputInConsole = false
    
    sourceSets.main {
        planVariables = mapOf("gh_token" to System.getenv("GITHUB_TOKEN"))
    }
}
