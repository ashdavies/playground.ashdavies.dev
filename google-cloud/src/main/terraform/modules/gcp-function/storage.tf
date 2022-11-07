resource "google_storage_bucket" "function_bucket" {
  name                        = "${var.project_id}-${var.function_name}"
  location                    = var.project_region
  force_destroy               = true
  uniform_bucket_level_access = true
}

resource "google_storage_bucket_object" "zip" {
  name         = "src-${data.archive_file.source.output_md5}.zip"
  bucket       = google_storage_bucket.function_bucket.name
  source       = data.archive_file.source.output_path
  content_type = "application/zip"

  depends_on   = [
    google_storage_bucket.function_bucket,
    data.archive_file.source
  ]
}
