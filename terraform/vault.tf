data "onepassword_vault" "development" {
  name = "Development"
}

data "onepassword_item" "android_release_keystore" {
  vault = data.onepassword_vault.development.uuid
  title = "Android Release Keystore"
}

data "onepassword_item" "github_developer_application" {
  vault = data.onepassword_vault.development.uuid
  title = "Github Developer Application"
}
