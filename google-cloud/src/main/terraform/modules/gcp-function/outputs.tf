output "function_uri" {
  value       = google_cloudfunctions2_function.google_cloud_function.service_config[0].uri
  description = "Google Cloud Function Service Config URI"
}

/*output "endpoint_url" {
  value = google_cloud_run_service.endpoint.status[0].url
  description = "Google Cloud Run Endpoint URL"
}*/
