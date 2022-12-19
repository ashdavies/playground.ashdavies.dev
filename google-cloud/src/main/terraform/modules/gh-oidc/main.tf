module "gh-oidc" {
  source      = "terraform-google-modules/github-actions-runners/google//modules/gh-oidc"
  provider_id = "gh-oidc-provider"
  project_id  = var.project_id
  pool_id     = "gh-oidc-pool"
  sa_mapping  = {
    (google_service_account.gh_service_account.account_id) = {
      attribute = "attribute.repository/${var.gh_owner}/${var.gh_repo_name}"
      sa_name   = google_service_account.gh_service_account.name
    }
  }
}

resource "github_actions_secret" "google_service_account_id" {
  plaintext_value = google_service_account.gh_service_account.email
  secret_name     = "google_service_account_id"
  repository      = var.gh_repo_name
}

resource "github_actions_secret" "google_workload_identity" {
  plaintext_value = module.gh-oidc.provider_name
  secret_name     = "google_workload_identity"
  repository      = var.gh_repo_name
}

resource "google_project_iam_member" "gh_service_account" {
  for_each = toset([
    "roles/iam.serviceAccountTokenCreator",
    "roles/iam.workloadIdentityUser",
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
