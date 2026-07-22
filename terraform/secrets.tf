locals {
  github_app_id = { for it in data.onepassword_item.github_developer_application.section[0].field : it.label => sensitive(it.value) }["app id"]
  keystore_password = {for it in data.onepassword_item.android_keystore.section[0].field :it.label => sensitive(it.value)}["password"]
}

resource "github_actions_secret" "main" {
  for_each = {
    BROWSER_API_KEY              = module.browser_api_key.key_string
    BROWSER_APP_ID               = google_firebase_web_app.browser.app_id
    FASTLANE_SERVICE_ACCOUNT_KEY = google_service_account_key.fastlane_supply_key.private_key
    FIREBASE_GOOGLE_SERVICES     = data.google_firebase_android_app_config.android_release.config_file_contents
    GH_APP_ID                    = local.github_app_id
    GH_PRIVATE_KEY               = base64encode(data.onepassword_item.github_developer_application.private_key)
    GOOGLE_SERVICE_ACCOUNT_ID    = module.github_service_account.email
    KEYSTORE_FILE                = data.onepassword_item.android_keystore.file[0].content_base64
    KEYSTORE_PASSWORD            = local.keystore_password
    OP_SERVICE_ACCOUNT_TOKEN     = var.op_service_account_token
    WORKLOAD_IDENTITY_PROVIDER   = module.github_workload_identity.provider_name
  }

  repository      = var.gh_repo_name
  secret_name     = each.key
  value           = each.value
}
