data "archive_file" "source" {
  output_path = "/tmp/${var.function_name}.zip"
  source_file  = var.source_file
  type        = "zip"
}

locals {
    endpoints_service_name = "${var.function_name}.endpoints.${var.project_id}.cloud.goog"
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

resource "google_cloud_run_service" "endpoint" {
  depends_on = [null_resource.openapi_proxy_image]
  location   = var.project_region
  name       = var.function_name
  project    = var.project_id
  template {
    spec {
      containers {
        image = format(
          "gcr.io/%s/endpoints-runtime-serverless:%s-%s-%s",
          var.project_id,
          var.esp_tag,
          local.endpoints_service_name,
          google_endpoints_service.endpoints.config_id,
        )
      }
    }
  }
}

resource "google_endpoints_service" "endpoints" {
  service_name   = local.endpoints_service_name
  project        = var.project_id
  openapi_config = templatefile(var.resources.openapi-v2_json.path, {
    address      = google_cloudfunctions2_function.google_cloud_function.service_config[0].uri
    host         = local.endpoints_service_name
    summary      = var.function_description
    method       = var.function_method
    operation_id = var.function_name
  })
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

resource "null_resource" "openapi_proxy_image" {
   triggers = {
     config_id = google_endpoints_service.endpoints.config_id
   }

   provisioner "local-exec" {
     command = <<EOS
       bash ${var.resources.gcloud-build-image.path} \
         -c ${google_endpoints_service.endpoints.config_id} \
         -s ${local.endpoints_service_name} \
         -p ${var.project_id} \
         -v ${var.esp_tag}
     EOS
   }
 }

resource "random_id" "bucket_prefix" {
  byte_length = 8
}
