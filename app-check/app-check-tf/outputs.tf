output "pool_name" {
  description = "Workload Identity Pool name"
  value       = module.gh-oidc.pool_name
}

output "provider_name" {
  description = "Workload Identity Provider name"
  value       = module.gh-oidc.provider_name
}

output "sa_email" {
  description = "Google Service Account email"
  value       = google_service_account.sa.email
}
