import org.jetbrains.gradle.plugins.terraform.TerraformPlugin
import org.jetbrains.gradle.plugins.terraform.tasks.AbstractTerraformExec
import org.jetbrains.gradle.plugins.terraform.tasks.TerraformPlan

plugins {
    id("io.ashdavies.terraform")
}

terraform {
    showPlanOutputInConsole = false
    showInitOutputInConsole = false

    val main by sourceSets.getting {
        planVariables = mapOf("gh_token" to System.getenv("GITHUB_TOKEN"))
    }

    val terraformShowText by tasks.registering(TerraformShowText::class) {
        outputTextPlanFile = project.file("${main.baseBuildDir}/plan.txt")
        sourcesDirectory = main.runtimeExecutionDirectory
        dependsOn(tasks.withType<TerraformPlan>())
        inputPlanFile = main.outputBinaryPlan
        group = TerraformPlugin.TASK_GROUP
        dataDir = main.dataDir
    }
}

abstract class TerraformShowText : AbstractTerraformExec() {

    @get:InputFile
    var inputPlanFile by project.objects.property<File>()

    @get:OutputFile
    var outputTextPlanFile by project.objects.property<File>()

    override fun ExecSpec.customizeExec() {
        standardOutput = outputTextPlanFile.outputStream()
    }

    override fun getTerraformArguments(): List<String> {
        return listOf("show", "-no-color", inputPlanFile.absolutePath)
    }
}
