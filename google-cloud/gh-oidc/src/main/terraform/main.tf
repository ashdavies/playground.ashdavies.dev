resource "google_project_iam_custom_role" "main" {
  description = "Can create, update, and delete services necessary for the automatic deployment"
  title       = "GitHub Actions Publisher"
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
    "storage.buckets.list",
    "storage.objects.create",
    "storage.objects.delete",
    "storage.objects.list",
  ]
}

resource "google_project_iam_member" "main" {
  member  = "serviceAccount:${google_service_account.main.email}"
  role    = google_project_iam_custom_role.main.id
  project = var.project_id
}

resource "google_project_iam_member" "viewer" {
  member  = "serviceAccount:${google_service_account.main.email}"
  project = var.project_id
  role    = "roles/viewer"
}

resource "google_service_account" "main" {
  display_name = "GitHub Service Account"
  project      = var.project_id
  account_id   = "gh-oidc"
}
