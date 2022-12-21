resource "google_endpoints_service" "endpoints" {
  service_name   = local.endpoints_path
  project        = var.project_id
  openapi_config = templatefile(var.resources.openapi-v2_json.path, {
    backend_service_name = google_cloud_run_service.service.status[0].url
    cloud_run_hostname   = local.endpoints_path
  })
}

resource "null_resource" "openapi_proxy_image" {
  triggers = {
    config_id = google_endpoints_service.endpoints.config_id
  }

  provisioner "local-exec" {
    command = <<EOS
    bash ${var.resources.gcloud-build-image.path} \
      -g ${var.project_region}-docker.pkg.dev/${var.project_id}/endpoints-release \
      -c ${google_endpoints_service.endpoints.config_id} \
      -s ${local.endpoints_path} \
      -p ${var.project_id} \
      -v ${var.esp_tag}
    EOS
  }
}
