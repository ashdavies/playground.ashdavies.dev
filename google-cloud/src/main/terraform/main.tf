resource "google_endpoints_service" "endpoints" {
  service_name   = var.service_name
  project        = var.project_id
  openapi_config = templatefile(var.resources.openapi-v2_yaml.path, {
    backend_service_name = module.cloud-run-build.url
    cloud_run_hostname   = var.service_name
  })
}

resource "google_project_service" "main" {
  service            = google_endpoints_service.endpoints.service_name
  depends_on         = [google_endpoints_service.endpoints]
  project            = var.project_id
}

resource "github_actions_secret" "google_service_account_id" {
  plaintext_value = module.github-service-account.email
  secret_name     = "google_service_account_id"
  repository      = var.gh_repo_name
}

resource "github_actions_secret" "google_workload_identity" {
  plaintext_value = module.github-workload-identity.provider_name
  secret_name     = "google_workload_identity"
  repository      = var.gh_repo_name
}
