output "sha256_digest" {
  description = "The sha256 digest of the image."
  value       = data.docker_registry_image.main.sha256_digest
}

output "url" {
  description = <<EOT
  Holds the url that will distribute traffic over the provided traffic targets. It generally has
  the form https://{route-hash}-{project-hash}-{cluster-level-suffix}.a.run.app
  EOT
  value       = google_cloud_run_service.main.status[0].url
}

output "debug_docker" {
  description = "data.docker_registry_image.main"
  value       = data.docker_registry_image.main
}

output "debug_cloud_run" {
  description = "data.google_cloud_run_service"
  value       = google_cloud_run_service.main
}
