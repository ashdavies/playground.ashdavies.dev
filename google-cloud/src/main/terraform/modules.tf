module "cloud-run-build" {
  docker_image = "${var.project_region}-docker.pkg.dev/${var.project_id}/cloud-run-source-deploy/playground.ashdavies.dev"
  source       = "./modules/google/cloud-run-build"
  service_name = "playground-service"
  location     = var.project_region
  project      = var.project_id
}

module "cloud-run-endpoint" {
  container_image    = "${var.project_region}-docker.pkg.dev/${var.project_id}/endpoints-release/endpoints-runtime-serverless:${var.esp_tag}-${var.service_name}-${google_endpoints_service.endpoints.config_id}"
  image_repository   = "${var.project_region}-docker.pkg.dev/${var.project_id}/endpoints-release"
  config_id          = google_endpoints_service.endpoints.config_id
  gcloud_build_image = var.resources.gcloud-build-image.path
  source             = "./modules/google/cloud-run-endpoint"
  service_name       = "playground-endpoint"
  location           = var.project_region
  project            = var.project_id
  esp_tag            = var.esp_tag
}

module "endpoint-iam-binding" {
  source             = "terraform-google-modules/iam/google//modules/cloud_run_services_iam"
  bindings           = { "roles/run.invoker" = ["allUsers"] }
  cloud_run_services = [module.cloud-run-endpoint.name]
  location           = var.project_region
  mode               = "authoritative"
  project            = var.project_id
}

module "github-api-key" {
  display_name = "Integration key (managed by Terraform)"
  source       = "./modules/google/github-api-key"
  service      = "identitytoolkit.googleapis.com"
  secret_name  = "google_project_api_key"
  repository   = var.gh_repo_name
  project      = var.project_id
  name         = "integration"
}

module "github-service-account" {
  source        = "terraform-google-modules/service-accounts/google"
  display_name  = "GitHub Service Account"
  project_id    = var.project_id
  names         = ["oidc"]
  prefix        = "gh"
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
  provider_id = "gh-oidc-provider"
  project_id  = var.project_id
  pool_id     = "gh-oidc-pool"
  sa_mapping  = {
    (module.github-service-account.service_account.account_id) = {
      attribute = "attribute.repository/${var.gh_owner}/${var.gh_repo_name}"
      sa_name   = module.github-service-account.service_account.name
    }
  }
}

module "gradle-build-cache" {
  source             = "terraform-google-modules/cloud-storage/google//modules/simple_bucket"
  name               = "playground-build-cache"
  location           = var.project_region
  project_id         = var.project_id
# public_access_prevention = "enforced"
  versioning         = false
  bucket_policy_only = true
  iam_members        = [{
    member = module.github-service-account.iam_email
    role   = "roles/storage.admin"
  }]
}
