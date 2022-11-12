data "archive_file" "source" {
  output_path = "/tmp/${var.function_name}.zip"
  source_file  = var.source_file
  type        = "zip"
}

resource "google_cloudfunctions2_function" "google_cloud_function" {
  description = var.function_description
  location    = var.project_region
  name        = var.function_name

  build_config {
    entry_point = var.entry_point
    runtime     = var.runtime

    source {
      storage_source {
        bucket = google_storage_bucket.function_bucket.name
        object = google_storage_bucket_object.zip.name
      }
    }
  }

  service_config {
    max_instance_count = 1
    available_memory   = "256M"
    timeout_seconds    = 60
  }
}

resource "random_id" "bucket_prefix" {
  byte_length = 8
}

resource "google_storage_bucket" "default" {
  name          = "${random_id.bucket_prefix.hex}-bucket-tfstate"
  project       = var.project_id
  storage_class = "STANDARD"
  location      = "EU"

  versioning {
    enabled = true
  }
}
