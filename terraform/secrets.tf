resource "github_actions_secret" "workload_identity_provider" {
  repository      = var.gh_repo_name
  secret_name     = "WORKLOAD_IDENTITY_PROVIDER"
  plaintext_value = module.github-workload-identity.provider_name
}

resource "github_actions_secret" "google_service_account_id" {
  repository      = var.gh_repo_name
  secret_name     = "GOOGLE_SERVICE_ACCOUNT_ID"
  plaintext_value = module.github-service-account.email
}

resource "onepassword_item" "playground_secrets" {
  vault = var.op_vault_uuid

  title    = "Playground Secrets"
  category = "secure_note"

  section {
    label = "Firebase"

    field {
      label = "Android App ID"
      type  = "CONCEALED"
      value = google_firebase_android_app.release.app_id
    }
  }
}
