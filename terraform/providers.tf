data "google_service_account_access_token" "default" {
  provider               = google.impersonation
  target_service_account = module.github-service-account.email
  scopes                 = ["userinfo-email", "cloud-platform"]
  lifetime               = "1200s"
}

provider "onepassword" {
  service_account_token = var.op_service_account_token
}

provider "github" {
  token = var.gh_token
  owner = var.gh_owner
}

provider "google" {
  project = var.project_id
  region  = var.project_region
  alias   = "impersonation"
}

provider "google" {
  project         = var.project_id
  access_token    = data.google_service_account_access_token.default.access_token
}

provider "google-beta" {
  project      = var.project_id
  access_token = data.google_service_account_access_token.default.access_token
}
