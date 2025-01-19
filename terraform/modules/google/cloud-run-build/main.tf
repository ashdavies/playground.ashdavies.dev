resource "google_cloud_run_service" "main" {
  name     = var.service_name
  location = var.location

  template {
    spec {
      containers {
        image = data.google_artifact_registry_docker_image.main.self_link
      }
    }
  }

  traffic {
    latest_revision = var.latest_revision
    percent         = var.percent
  }
}

data "google_artifact_registry_docker_image" "main" {
  location      = var.location
  repository_id = var.repository_id
  image_name    = var.image_name
}
