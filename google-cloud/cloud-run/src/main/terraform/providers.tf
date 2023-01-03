data "google_client_config" "default" {
}

provider "docker" {
  registry_auth {
    password = data.google_client_config.default.access_token
    address  = "${var.project_region}-docker.pkg.dev"
    username = "oauth2accesstoken"
  }
}

provider "google" {
  region  = var.project_region
  project = var.project_id
}
