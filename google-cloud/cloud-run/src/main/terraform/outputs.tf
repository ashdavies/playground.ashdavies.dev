output "endpoint_url" {
  value = google_cloud_run_service.endpoint.status[0].url
  description = "Google Cloud Run Endpoint URL"
}

output "service_image" {
  value = data.docker_registry_image.service.id
}
