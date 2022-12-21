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
