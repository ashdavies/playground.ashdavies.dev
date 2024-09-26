data "google_service_account_access_token" "default" {
  scopes                 = ["userinfo-email", "cloud-platform"]
  target_service_account = module.github-service-account.email
  provider               = google.impersonated
  lifetime               = "1200s"
}

provider "github" {
  token  = var.gh_token
  owner  = var.gh_owner
}

provider "google" {
  region  = var.project_region
  project = var.project_id
  alias   = "impersonated"
}

provider "google" {
  access_token    = data.google_service_account_access_token.default.access_token
  region          = var.project_region
  project         = var.project_id
  request_timeout = "60s"
}
