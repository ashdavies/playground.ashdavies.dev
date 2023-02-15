resource "google_storage_bucket" "cache" {
  name                        = "playground-build-cache"
  location                    = var.project_region
  public_access_prevention    = "enforced"
  uniform_bucket_level_access = true
}

resource "google_storage_bucket_iam_member" "cache" {
  member = google_service_account.main.member
  bucket = google_storage_bucket.cache.name
  role   = "roles/storage.admin"
}
