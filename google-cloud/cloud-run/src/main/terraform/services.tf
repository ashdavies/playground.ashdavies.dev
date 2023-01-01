resource "google_cloud_run_service" "endpoint" {
  depends_on = [null_resource.openapi_proxy_image]
  name       = "${var.resource_prefix}-endpoint"
  location   = var.project_region
  project    = var.project_id
  template {
    spec {
      containers {
        image = format(
          "%s/endpoints-runtime-serverless:%s-%s-%s",
          local.endpoints_registry, var.esp_tag, var.service_name,
          google_endpoints_service.endpoints.config_id,
        )
      }
    }
  }
}

# Needs a trigger
resource "google_cloud_run_service" "service" {
  name     = "${var.resource_prefix}-service"
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

  lifecycle {
    replace_triggered_by = [
      random_id.service_trigger.hex
    ]
  }
}

resource "google_cloud_run_service_iam_policy" "noauth-endpoints" {
  location    = google_cloud_run_service.endpoint.location
  project     = google_cloud_run_service.endpoint.project
  policy_data = data.google_iam_policy.noauth.policy_data
  service     = google_cloud_run_service.endpoint.name
}

resource "random_id" "service_trigger" {
  byte_length = 8
}
