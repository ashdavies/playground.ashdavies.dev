resource "github_actions_secret" "workload_identity_provider" {
  repository      = var.gh_repo_name
  secret_name     = "WORKLOAD_IDENTITY_PROVIDER"
  plaintext_value = module.github-workload-identity.provider_name
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

  section {
    label = "Google"

    field {
      label = "Service Account ID"
      type  = "CONCEALED"
      value = module.github-service-account.email
    }
  }
}
