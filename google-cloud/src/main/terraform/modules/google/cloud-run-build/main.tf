data "docker_registry_image" "main" {
  name = format("${var.location}-docker.pkg.dev/${var.project}/cloud-run-source-deploy/${var.image}")
}

resource "google_cloud_run_service" "main" {
  name                       = var.name
  location                   = var.location
  autogenerate_revision_name = true

  template {
    spec {
      containers {
        image = "${data.docker_registry_image.main.name}@${data.docker_registry_image.main.sha256_digest}"
      }
    }
  }

  traffic {
    latest_revision = var.latest_revision
    percent         = var.percent
  }

  depends_on = [data.docker_registry_image.main]
}
