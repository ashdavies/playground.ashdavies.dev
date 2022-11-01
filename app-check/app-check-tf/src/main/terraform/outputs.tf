output "function_uri" {
  value       = google_cloudfunctions2_function.google_function_create_token.service_config[0].uri
  description = ""
}

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
  value       = google_service_account.gh_service_account.email
}

