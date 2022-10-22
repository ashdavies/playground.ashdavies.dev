data "archive_file" "source" {
  source_dir  = "${var.project_dir}/${var.source_dir}"
  output_path = "/tmp/function.zip"
  type        = "zip"
}

provider "github" {
  token = var.gh_token
  owner = var.gh_owner
}

provider "google" {
  region  = var.project_region
  project = var.project_id
}

resource "github_actions_secret" "google_service_account_id" {
  plaintext_value = google_service_account.gh_service_account.email
  secret_name     = "google_service_account_id"
  repository      = var.gh_repo_name
}

resource "github_actions_secret" "google_workload_identity" {
  plaintext_value = module.gh-oidc.provider_name
  secret_name     = "google_workload_identity"
  repository      = var.gh_repo_name
}

resource "google_service_account" "gh_service_account" {
  display_name = "GitHub Service Account"
  project      = var.project_id
  account_id   = "gh-oidc"
}

resource "google_cloudfunctions_function" "google_function_create_token" {
  source_archive_bucket = google_storage_bucket.function_bucket.name
  source_archive_object = google_storage_bucket_object.zip.name
  entry_point           = "io.ashdavies.check.AppCheckFunction"
  description           = "Creates a new AppCheck token"
  name                  = "createToken"
  runtime               = "java11"
  trigger_http          = true

  depends_on            = [
    google_storage_bucket.function_bucket,
    google_storage_bucket_object.zip
  ]
}

resource "google_project_iam_member" "service_account_token_creator" {
  member  = "serviceAccount:${google_service_account.gh_service_account.email}"
  role    = "roles/iam.serviceAccountTokenCreator"
  project = var.project_id
}

resource "google_project_iam_member" "storage_admin" {
  member  = "serviceAccount:${google_service_account.gh_service_account.email}"
  role    = "roles/storage.admin"
  project = var.project_id
}

resource "google_project_iam_member" "workload_identity_user" {
  member  = "serviceAccount:${google_service_account.gh_service_account.email}"
  role    = "roles/iam.workloadIdentityUser"
  project = var.project_id
}

resource "random_id" "bucket_prefix" {
  byte_length = 8
}

resource "google_storage_bucket" "default" {
  name          = "${random_id.bucket_prefix.hex}-bucket-tfstate"
  project       = var.project_id
  storage_class = "STANDARD"
  force_destroy = false
  location      = "EU"
  versioning {
    enabled = true
  }
}

module "gh-oidc" {
  source      = "terraform-google-modules/github-actions-runners/google//modules/gh-oidc"
  provider_id = "gh-oidc-provider"
  project_id  = var.project_id
  pool_id     = "gh-oidc-pool"
  sa_mapping = {
    (google_service_account.gh_service_account.account_id) = {
      attribute = "attribute.repository/${var.gh_owner}/${var.gh_repo_name}"
      sa_name   = google_service_account.gh_service_account.name
    }
  }
}
