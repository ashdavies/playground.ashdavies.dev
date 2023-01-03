locals {
  endpoints_registry = format(
    "%s-docker.pkg.dev/%s/endpoints-release",
    var.project_region,
    var.project_id
  )
}
