resource "onepassword_item" "playground_secrets" {
  vault    = var.op_vault

  title    = "Playground Secrets"
  category = "password"

  section {
    label = "Google"

    field {
      label = "Workload Identity"
      type  = "CONCEALED"
      value = module.github-workload-identity.provider_name
    }
  }
}
