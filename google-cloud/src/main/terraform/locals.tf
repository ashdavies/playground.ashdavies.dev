locals {
  openapi_config = templatefile(var.resources.openapi-v2_yaml.path, {
    version_name         = substr(module.github-repository.sha, 0, 7)
    backend_service_name = module.cloud-run-build.url
    cloud_run_hostname   = "playground.ashdavies.dev"
  })
}
