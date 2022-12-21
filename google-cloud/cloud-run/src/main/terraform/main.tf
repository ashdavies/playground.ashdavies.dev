// https://cloud.google.com/docs/terraform/best-practices-for-terraform

// Assume Docker images have already been deployed

// Deploy Cloud Service

// Deploy Endpoint Placeholder

// Redeploy Cloud Service...

// Apply auth configuration

resource "google_cloud_run_service" "service" {
  name     = "${var.cloud_run_service}-service"
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

resource "google_endpoints_service" "endpoints" {
  service_name   = local.endpoints_path
  project        = var.project_id
  openapi_config = templatefile(var.resources.openapi-v2_json.path, {
    backend_service_name = google_cloud_run_service.service.status[0].url
    cloud_run_hostname   = local.endpoints_path
  })
}
