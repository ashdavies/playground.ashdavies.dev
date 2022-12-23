locals {
  artifact_registry = format(
    "%s-docker.pkg.dev/%s",
    var.project_region,
    var.project_id
  )

  cloud_run_artifact = format(
    "%s/cloud-run-source-deploy/%s",
    local.artifact_registry,
    var.service_name
  )

  endpoints_registry = format(
    "%s/endpoints-release",
    local.artifact_registry
  )
}
