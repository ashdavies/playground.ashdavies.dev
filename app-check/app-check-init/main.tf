resource "google_service_account" "sa" {
  project    = var.project_id
  account_id = "gh-oidc"
}

resource "google_project_iam_member" "project" {
  project = var.project_id
  role    = "roles/storage.admin"
  member  = "serviceAccount:${google_service_account.sa.email}"
}

/*resource "github_actions_secret" "google_service_account_id" {
  repository      = "ashdavies/playground"
  secret_name     = "google_service_account_id"
  plaintext_value = google_service_account.sa.email
}*/

/*resource "github_actions_secret" "google_workload_identity" {
  repository      = "ashdavies/playground"
  secret_name     = "google_workload_identity"
  plaintext_value = ""
}*/

module "gh-oidc" {
  source      = "terraform-google-modules/github-actions-runners/google//modules/gh-oidc"
  project_id  = var.project_id
  pool_id     = "gh-oidc-pool"
  provider_id = "gh-oidc-provider"
  sa_mapping = {
    (google_service_account.sa.account_id) = {
      sa_name   = google_service_account.sa.name
      attribute = "attribute.repository/user/repo"
    }
  }
}
