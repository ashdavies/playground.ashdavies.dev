resource "google_project_iam_member" "gh_service_account" {
  for_each = toset([
    "roles/iam.serviceAccountTokenCreator",
    "roles/iam.workloadIdentityUser",
    "roles/artifactregistry.writer",
    "roles/iam.serviceAccountUser",
    "roles/run.developer",
    "roles/storage.admin",
  ])

  member  = "serviceAccount:${google_service_account.gh_service_account.email}"
  project = var.project_id
  role    = each.key
}

resource "google_service_account" "gh_service_account" {
  display_name = "GitHub Service Account"
  project      = var.project_id
  account_id   = "gh-oidc"
}
