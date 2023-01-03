resource "github_actions_secret" "google_project_api_key" {
  plaintext_value = google_apikeys_key.integration.key_string
  secret_name     = "google_project_api_key"
  repository      = var.gh_repo_name
}

resource "github_actions_secret" "google_service_account_id" {
  plaintext_value = google_service_account.main.email
  secret_name     = "google_service_account_id"
  repository      = var.gh_repo_name
}

resource "github_actions_secret" "google_workload_identity" {
  plaintext_value = module.gh-oidc.provider_name
  secret_name     = "google_workload_identity"
  repository      = var.gh_repo_name
}
