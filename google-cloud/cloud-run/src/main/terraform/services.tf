resource "google_cloud_run_service" "endpoint" {
  depends_on = [null_resource.openapi_proxy_image]
  name       = "${var.service_name}-endpoint"
  location   = var.project_region
  project    = var.project_id
  template {
    spec {
      containers {
        image = format(
          "gcr.io/%s/endpoints-runtime-serverless:%s-%s-%s",
          var.project_id, var.esp_tag, local.endpoints_path,
          google_endpoints_service.endpoints.config_id,
        )
      }
    }
  }
}

resource "google_cloud_run_service" "service" {
  name     = "${var.service_name}-service"
  location = var.project_region

  template {
    spec {
      containers {
        image = local.cloud_run_artifact
      }
    }
  }

  traffic {
    latest_revision = true
    percent         = 100
  }
}

resource "google_cloud_run_service_iam_policy" "noauth-endpoints" {
  location    = google_cloud_run_service.endpoint.location
  project     = google_cloud_run_service.endpoint.project
  policy_data = data.google_iam_policy.noauth.policy_data
  service     = google_cloud_run_service.endpoint.name
}
