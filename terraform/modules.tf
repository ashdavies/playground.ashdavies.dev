module "api-gateway" {
  source           = "./modules/google/api-gateway"
  api_id           = "playground-api"
  gateway_id       = "playground-api-gateway"
  openapi_contents = base64encode(local.openapi_config)
  project          = var.project_id
  region           = var.project_region
}

module "cloud-run-build" {
  image_name    = "api.ashdavies.dev"
  location      = var.project_region
  project       = var.project_id
  repository_id = "cloud-run-source-deploy"
  service_name  = "playground-service"
  source        = "./modules/google/cloud-run-build"
}

# module.cloud-run-endpoint is deprecated
module "cloud-run-endpoint" {
  source           = "./modules/google/cloud-run-endpoint"
  config_id        = module.cloud-run-endpoint.config_id
  image_name       = "endpoints-runtime-serverless"
  repository_id    = "endpoints-release"
  endpoint_name    = "api.ashdavies.dev"
  image_repository = "${var.project_region}-docker.pkg.dev/${var.project_id}/endpoints-release"
  location         = var.project_region
  openapi_config   = local.openapi_config
  project          = var.project_id
  service_name     = "playground-endpoint"
}

# module endpoint-iam-binding is deprecated
module "endpoint-iam-binding" {
  source             = "terraform-google-modules/iam/google//modules/cloud_run_services_iam"
  version            = "8.1.0"
  bindings           = { "roles/run.invoker" = ["allUsers"] }
  cloud_run_services = [module.cloud-run-endpoint.name]
  location           = var.project_region
  mode               = "authoritative"
  project            = var.project_id
}

module "github-api-key" {
  display_name = "Integration key (managed by Terraform)"
  name         = "integration"
  project      = var.project_id
  repository   = var.gh_repo_name
  service      = "identitytoolkit.googleapis.com"
  source       = "./modules/google/github-api-key"
  secret_name  = "integration_api_key"
}

moved {
  from = module.github-repository.github_repository.main
  to   = github_repository.main
}

module "github-service-account" {
  source       = "terraform-google-modules/service-accounts/google"
  version      = "4.5.4"
  providers    = { google = google.impersonated }
  display_name = "GitHub Service Account"
  names        = ["oidc"]
  prefix       = "gh"
  project_id   = var.project_id
  project_roles = [
    "${var.project_id}=>${google_project_iam_custom_role.main.id}",
    "${var.project_id}=>roles/serviceusage.serviceUsageConsumer",
    "${var.project_id}=>roles/iam.serviceAccountTokenCreator",
    "${var.project_id}=>roles/iam.workloadIdentityPoolAdmin",
    "${var.project_id}=>roles/storage.objectAdmin",
    "${var.project_id}=>roles/viewer"
  ]
}

module "github-workload-identity" {
  source      = "terraform-google-modules/github-actions-runners/google//modules/gh-oidc"
  version     = "5.0.0"
  pool_id     = "gh-oidc-pool"
  project_id  = var.project_id
  provider_id = "gh-oidc-provider"
  sa_mapping = {
    (module.github-service-account.service_account.account_id) = {
      attribute = "attribute.repository/${var.gh_owner}/${var.gh_repo_name}"
      sa_name   = module.github-service-account.service_account.name
    }
  }
}

module "gradle-build-cache" {
  source     = "terraform-google-modules/cloud-storage/google//modules/simple_bucket"
  version    = "~> 11.0"

  name       = "playground-build-cache"
  project_id = var.project_id
  location   = var.project_region

  bucket_policy_only = true
  iam_members = [
    {
      member = module.github-service-account.iam_email
      role   = "roles/storage.admin"
    }
  ]
  lifecycle_rules = [
    {
      action = {
        type = "Delete"
      }
      condition = {
        age = 30
      }
    }
  ]
  public_access_prevention = "enforced"
  versioning               = false
}
