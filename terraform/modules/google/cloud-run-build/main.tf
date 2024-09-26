resource "google_cloud_run_service" "main" {
  name                       = var.service_name
  location                   = var.location
  autogenerate_revision_name = true

  template {
    spec {
      containers {
        image = "${var.docker_image}:latest"
      }
    }
  }

  traffic {
    latest_revision = var.latest_revision
    percent         = var.percent
  }
}
