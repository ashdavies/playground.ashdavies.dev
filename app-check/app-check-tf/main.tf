provider "github" {
  token = var.gh_token
  owner = "ashdavies"
}

resource "google_service_account" "sa" {
  display_name = "GitHub Service Account"
  project    = var.project_id
  account_id = "gh-oidc"
}

resource "google_project_iam_member" "project" {
  member  = "serviceAccount:${google_service_account.sa.email}"
  role    = "roles/storage.admin"
  project = var.project_id
}

resource "github_actions_secret" "google_service_account_id" {
  plaintext_value = google_service_account.sa.email
  secret_name     = "google_service_account_id"
  repository      = var.gh_repo_name
}

resource "github_actions_secret" "google_workload_identity" {
  plaintext_value = module.gh-oidc.provider_name
  secret_name     = "google_workload_identity"
  repository      = var.gh_repo_name
}

module "gh-oidc" {
  source      = "terraform-google-modules/github-actions-runners/google//modules/gh-oidc"
  provider_id = "gh-oidc-provider"
  project_id  = var.project_id
  pool_id     = "gh-oidc-pool"
  sa_mapping = {
    (google_service_account.sa.account_id) = {
      sa_name   = google_service_account.sa.name
      attribute = "attribute.repository/user/repo"
    }
  }
}
