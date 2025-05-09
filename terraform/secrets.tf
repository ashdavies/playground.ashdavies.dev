resource "onepassword_item" "playground_secrets" {
  vault    = var.op_vault_uuid

  title    = "Playground Secrets"
  category = "secure_note"

  section {
    label = "Google"

    field {
      label = "Workload Identity"
      type  = "CONCEALED"
      value = module.github-workload-identity.provider_name
    }
  }
}
