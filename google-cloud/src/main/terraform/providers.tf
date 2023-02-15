data "google_client_config" "default" {
}

provider "docker" {
  registry_auth {
    password = data.google_client_config.default.access_token
    address  = "${var.project_region}-docker.pkg.dev"
    username = "oauth2accesstoken"
  }
}

provider "github" {
  token = var.gh_token
  owner = var.gh_owner
}

provider "google" {
  region  = var.project_region
  project = var.project_id
}
