output "endpoints_url" {
  value = google_cloud_run_service.endpoint.status[0].url
  description = "Google Cloud Run Endpoint URL"
}
