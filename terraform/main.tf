locals {
  openapi_config = templatefile(var.openapi_config, {
    backend_service_name = module.cloud_run_build.url
  })
}

# google_project_service.main is deprecated
resource "google_project_service" "main" {
  service    = module.cloud_run_endpoint.service_name
  depends_on = [module.cloud_run_endpoint]
  project    = var.project_id
}

resource "google_project_iam_custom_role" "main" {
  description = "Can create, update, and delete services necessary for the automatic deployment"
  title       = "GitHub Actions Publisher"
  provider    = google.impersonation
  role_id     = "actionsPublisher"
  permissions = [
    "apigateway.apiconfigs.create",
    "apigateway.gateways.update",
    "artifactregistry.repositories.uploadArtifacts",
    "cloudbuild.builds.create",
    "iam.serviceAccounts.actAs",
    "iam.serviceAccounts.getAccessToken",
    "iam.serviceAccounts.signBlob",
    "resourcemanager.projects.get",
    "run.services.get",
    "run.services.getIamPolicy",
    "run.services.update",
    "servicemanagement.services.create",
    "servicemanagement.services.delete",
    "servicemanagement.services.get",
    "servicemanagement.services.update",
    "serviceusage.services.list",
    "storage.buckets.create",
    "storage.buckets.getIamPolicy",
    "storage.buckets.setIamPolicy",
    "storage.buckets.list",
    "storage.objects.create",
    "storage.objects.delete",
    "storage.objects.list",
  ]
}

resource "google_service_account_key" "fastlane_supply_key" {
  service_account_id = module.fastlane_service_account.service_account.name
}
