provider "github" {
  token = var.gh_token
  owner = var.gh_owner
}

provider "google" {
  region  = var.project_region
  project = var.project_id
}

module "create-token" {
  source_file          = var.resources.app-check-function-all_jar.path
  function_description = "Create a new Firebase App Check token"
  entry_point          = "io.ashdavies.check.AppCheckFunction"
  source               = "./modules/gcp-function"
  project_region       = var.project_region
  project_id           = var.project_id
  function_name        = "create-token"
}

module "gh-oidc" {
  source       = "./modules/gh-oidc"
  gh_repo_name = var.gh_repo_name
  project_id   = var.project_id
  gh_owner     = var.gh_owner
}
