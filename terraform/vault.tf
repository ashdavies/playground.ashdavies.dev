data "onepassword_vault" "development" {
  name = "Development"
}

data "onepassword_item" "android_keystore" {
  vault = data.onepassword_vault.development.uuid
  title = "Android Playground Keystore"
}

data "onepassword_item" "cloudflare_terraform_token" {
  vault = data.onepassword_vault.development.uuid
  title = "Cloudflare Terraform Token"
}

data "onepassword_item" "github_developer_application" {
  vault = data.onepassword_vault.development.uuid
  title = "GitHub Developer Application"
}
