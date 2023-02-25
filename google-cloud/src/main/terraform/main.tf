resource "google_project_service" "main" {
  service            = module.cloud-run-endpoint.service_name
  depends_on         = [module.cloud-run-endpoint]
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

resource "github_issue_label" "test_repo" {
  repository = var.gh_repo_name
  name       = "Google Cloud"
  color      = "3367d6"
}

resource "google_project_iam_custom_role" "main" {
  description = "Can create, update, and delete services necessary for the automatic deployment"
  title       = "GitHub Actions Publisher"
  provider    = google.impersonated
  role_id     = "actionsPublisher"
  permissions = [
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

resource "google_storage_bucket_object" "openapi_config" {
  bucket  = module.runtime-resources.bucket.name
  content = local.openapi_config
  name    = "openapi_config"
}
