output "function_uri" {
  value       = google_cloudfunctions2_function.google_cloud_function.service_config[0].uri
  description = ""
}
