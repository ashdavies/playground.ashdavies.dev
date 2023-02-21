output "config_id" {
  value = google_endpoints_service.main.config_id
}

output "name" {
  value = google_cloud_run_service.main.name
}

output "service_name" {
  value = google_endpoints_service.main.service_name
}

output "url" {
  description = <<EOT
  Holds the url that will distribute traffic over the provided traffic targets. It generally has
  the form https://{route-hash}-{project-hash}-{cluster-level-suffix}.a.run.app
  EOT
  value       = google_cloud_run_service.main.status[0].url
}
