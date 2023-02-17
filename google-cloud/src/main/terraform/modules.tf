module "endpoint-iam-binding" {
  source             = "terraform-google-modules/iam/google//modules/cloud_run_services_iam"
  project            = google_cloud_run_service.endpoint.project
  cloud_run_services = [google_cloud_run_service.endpoint.name]
  bindings           = { "roles/run.invoker" = ["allUsers"] }
  location           = var.project_region
  mode               = "authoritative"
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
