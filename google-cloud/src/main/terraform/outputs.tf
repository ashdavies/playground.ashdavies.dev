output "endpoint_url" {
  value = google_cloud_run_service.endpoint.status[0].url
  description = "Google Cloud Run Endpoint URL"
}

output "pool_name" {
  description = "Workload Identity Pool name"
  value       = module.github-workload-identity.pool_name
}

output "provider_name" {
  description = "Workload Identity Provider name"
  value       = module.github-workload-identity.provider_name
}

output "sa_email" {
  description = "Google Service Account email"
  value       = google_service_account.main.email
}
