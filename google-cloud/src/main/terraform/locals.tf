locals {
  openapi_config = templatefile(var.resources.openapi-v2_yaml.path, {
    backend_service_name = module.cloud-run-build.url
    cloud_run_hostname   = "playground.ashdavies.dev"
  })
}
