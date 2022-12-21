resource "google_project_iam_member" "gh_service_account" {
  for_each = toset([
    "roles/servicemanagement.configEditor"
    "roles/iam.workloadIdentityUser",
    "roles/artifactregistry.writer",
    "roles/run.developer",
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
