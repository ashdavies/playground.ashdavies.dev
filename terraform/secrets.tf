locals {
  keystore_fields = merge([
    for section in data.onepassword_item.playground-keystore.section : merge([
      for field in section.field : { replace(field.label, "/\\s+/", "_") = sensitive(field.value) }
    ]...)
  ]...)

  keystore_properties = <<-EOT
  key.alias=${ local.keystore_fields["key_alias"] }
  key.password=${ local.keystore_fields["key_password"] }
  store.file=keystore.jks
  store.password=${ local.keystore_fields["keystore_password"] }
  EOT
}

resource "github_actions_secret" "main" {
  for_each = {
    FASTLANE_SERVICE_ACCOUNT_KEY = google_service_account_key.fastlane_supply_key.private_key
    FIREBASE_ANDROID_APP_ID      = google_firebase_android_app.release.app_id
    FIREBASE_GOOGLE_SERVICES     = data.google_firebase_android_app_config.release.config_file_contents
    GOOGLE_SERVICE_ACCOUNT_ID    = module.github-service-account.email
    RELEASE_KEYSTORE_FILE        = data.onepassword_item.playground-keystore.file[0].content_base64
    RELEASE_KEYSTORE_PROPERTIES  = base64encode(local.keystore_properties)
    WORKLOAD_IDENTITY_PROVIDER   = module.github-workload-identity.provider_name
  }

  repository      = var.gh_repo_name
  secret_name     = each.key
  plaintext_value = each.value
}
