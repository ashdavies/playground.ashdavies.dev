data "onepassword_vault" "development" {
  name = "Development"
}

data "onepassword_item" "playground-keystore" {
  vault = data.onepassword_vault.development.uuid
  title = "Playground Keystore"
}
