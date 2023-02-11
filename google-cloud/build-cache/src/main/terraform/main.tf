data "google_iam_policy" "default" {
  binding {
    members = [google_service_account.default.email]
    role = "roles/storage.objectAdmin"
  }
}

resource "google_service_account" "default" {
  account_id   = "gradle-build-cache"
  display_name = "Gradle Build Cache"
  project      = var.project_id
}

resource "google_storage_bucket" "default" {
  name          = "playground-gradle-cache"
  location      = "EU"
}
