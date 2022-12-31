resource "google_endpoints_service" "endpoints" {
  service_name   = var.service_name
  project        = var.project_id
  openapi_config = templatefile(var.resources.openapi-v2_yaml.path, {
    backend_service_name = google_cloud_run_service.service.status[0].url
    cloud_run_hostname   = var.service_name
  })
}

data "google_iam_policy" "noauth" {
  binding {
    role = "roles/run.invoker"
    members = ["allUsers"]
  }
}

resource "google_project_service" "main" {
  service            = google_endpoints_service.endpoints.service_name
  depends_on         = [google_endpoints_service.endpoints]
  project            = var.project_id
  disable_on_destroy = true
}

resource "null_resource" "openapi_proxy_image" {
  triggers = {
    config_id = google_endpoints_service.endpoints.config_id
  }

  provisioner "local-exec" {
    command = <<EOS
    bash ${var.resources.gcloud-build-image.path} \
      -c ${google_endpoints_service.endpoints.config_id} \
      -g ${local.endpoints_registry} \
      -s ${var.service_name} \
      -p ${var.project_id} \
      -v ${var.esp_tag}
    EOS
  }
}
