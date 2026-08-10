data "google_service_account_access_token" "default" {
  target_service_account = module.github_service_account.email
  scopes                 = ["userinfo-email", "cloud-platform"]
  lifetime               = "1200s"
}

provider "cloudflare" {
  api_token = data.onepassword_item.cloudflare_terraform_token.credential
}

provider "github" {
  token = var.gh_token
  owner = var.gh_owner
}

provider "google-beta" {
  project      = var.project_id
}

provider "onepassword" {
  service_account_token = var.op_service_account_token
}
