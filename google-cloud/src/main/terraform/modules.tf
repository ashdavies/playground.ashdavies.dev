module "gradle-build-cache" {
  source                   = "terraform-google-modules/cloud-storage/google//modules/simple_bucket"
  name                     = "playground-build-cache"
  location                 = var.project_region
  project_id               = var.project_id
# public_access_prevention = "enforced"
  versioning               = false
  bucket_policy_only       = true
  iam_members              = [{
    member = google_service_account.main.member
    role   = "roles/storage.admin"
  }]
}

module "gh-oidc" {
  source      = "terraform-google-modules/github-actions-runners/google//modules/gh-oidc"
  provider_id = "gh-oidc-provider"
  project_id  = var.project_id
  pool_id     = "gh-oidc-pool"
  sa_mapping  = {
    (google_service_account.main.account_id) = {
      attribute = "attribute.repository/${var.gh_owner}/${var.gh_repo_name}"
      sa_name   = google_service_account.main.name
    }
  }
}
