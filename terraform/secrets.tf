resource "onepassword_item" "playground_secrets" {
  vault    = var.op_vault_uuid

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

    field {
      label = "Workload Identity"
      type  = "CONCEALED"
      value = module.github-workload-identity.provider_name
    }
  }
}
