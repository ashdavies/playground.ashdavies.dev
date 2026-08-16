provider "cloudflare" {
  api_token = data.onepassword_item.cloudflare_terraform_token.credential
}

provider "github" {
  token = var.gh_token
  owner = var.gh_owner
}

provider "google" {
  project               = var.project_id
  region                = var.project_region
  billing_project       = var.project_id
  user_project_override = true
}

provider "google-beta" {
  project               = var.project_id
  region                = var.project_region
  billing_project       = var.project_id
  user_project_override = true
}

provider "onepassword" {
  service_account_token = var.op_service_account_token
}
