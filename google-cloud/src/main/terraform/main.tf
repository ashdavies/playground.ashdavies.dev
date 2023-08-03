# google_project_service.main is deprecated
resource "google_project_service" "main" {
  service            = module.cloud-run-endpoint.service_name
  depends_on         = [module.cloud-run-endpoint]
  project            = var.project_id
}

resource "google_project_iam_custom_role" "main" {
  description = "Can create, update, and delete services necessary for the automatic deployment"
  title       = "GitHub Actions Publisher"
  provider    = google.impersonated
  role_id     = "actionsPublisher"
  permissions = [
    "apigateway.apiconfigs.create",
    "artifactregistry.repositories.uploadArtifacts",
    "cloudbuild.builds.create",
    "iam.serviceAccounts.actAs",
    "iam.serviceAccounts.getAccessToken",
    "iam.serviceAccounts.signBlob",
    "resourcemanager.projects.get",
    "run.services.get",
    "run.services.getIamPolicy",
    "run.services.update",
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

resource "local_file" "openapi_config" {
  content  = templatefile(var.openapi_config, {
    version_name         = substr(module.github-repository.sha, 0, 7)
    backend_service_name = module.cloud-run-build.url
  })
  filename = basename(var.openapi_config)
}
