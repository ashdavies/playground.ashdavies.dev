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
  value       = module.github-service-account.email
}
