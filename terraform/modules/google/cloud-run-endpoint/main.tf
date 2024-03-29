resource "null_resource" "main" {
  provisioner "local-exec" {
    command = <<EOS
    bash ${var.gcloud_build_image} \
      -g ${var.image_repository} \
      -s ${var.endpoint_name} \
      -c ${var.config_id} \
      -p ${var.project} \
      -v ${var.esp_tag}
    EOS
  }

  triggers = {
    config_id = var.config_id
  }
}

resource "google_cloud_run_service" "main" {
  depends_on = [null_resource.main]
  name       = var.service_name
  location   = var.location
  project    = var.project

  template {
    spec {
      containers {
        image = var.container_image
      }
    }
  }
}

resource "google_endpoints_service" "main" {
  openapi_config = var.openapi_config
  service_name   = var.endpoint_name
  project        = var.project
}
