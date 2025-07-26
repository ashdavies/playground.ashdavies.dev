resource "github_actions_secret" "fastlane_service_account_key" {
  repository      = var.gh_repo_name
  secret_name     = "FASTLANE_SERVICE_ACCOUNT_KEY"
  plaintext_value = google_service_account_key.fastlane_supply_key.private_key
}

resource "github_actions_secret" "firebase_android_app_id" {
  repository      = var.gh_repo_name
  secret_name     = "FIREBASE_ANDROID_APP_ID"
  plaintext_value = google_firebase_android_app.release.app_id
}

resource "github_actions_secret" "firebase_google_services" {
  repository      = var.gh_repo_name
  secret_name     = "FIREBASE_GOOGLE_SERVICES"
  plaintext_value = data.google_firebase_android_app_config.release.config_file_contents
}

resource "github_actions_secret" "google_service_account_id" {
  repository      = var.gh_repo_name
  secret_name     = "GOOGLE_SERVICE_ACCOUNT_ID"
  plaintext_value = module.github-service-account.email
}

resource "github_actions_secret" "workload_identity_provider" {
  repository      = var.gh_repo_name
  secret_name     = "WORKLOAD_IDENTITY_PROVIDER"
  plaintext_value = module.github-workload-identity.provider_name
}
