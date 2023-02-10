data "google_service_account_access_token" "default" {
  target_service_account = "terraform-admin@${var.project_id}.iam.gserviceaccount.com"
  scopes                 = ["userinfo-email", "cloud-platform"]
  lifetime               = "300s"
  provider               = google
}

provider "github" {
  token = var.gh_token
  owner = var.gh_owner
}

provider "google" {
  region  = var.project_region
  project = var.project_id
  scopes  = [
    "https://www.googleapis.com/auth/cloud-platform",
    "https://www.googleapis.com/auth/userinfo.email",
  ]
}

provider "google" {
  access_token = data.google_service_account_access_token.default.access_token
  alias        = "impersonation"
}
