variable "op_service_account_token" {
  description = "A valid token for the 1Password Service Account."
}

variable "op_vault" {
  description = "The UUID of the vault the item is in."
}

variable "gh_owner" {
  description = "Username of the GitHub repository owner"
  default     = "ashdavies"
}

variable "gh_repo_name" {
  description = "GitHub repository to set actions secrets"
  default     = "playground.ashdavies.dev"
}

variable "gh_token" {
  description = "GitHub personal access token"
}

variable "openapi_config" {
  description = "The full path of the OpenAPI configuration."
  default     = "../openapi-v2.yml"
}

variable "service_name" {
  description = "Google Cloud Run service name"
  default     = "api.ashdavies.dev"
}

variable "project_id" {
  description = "Google Cloud project id"
  default     = "playground-1a136"
}

variable "project_region" {
  description = "Google Cloud project region"
  default     = "europe-west1"
}

variable "resource_prefix" {
  description = "Google Cloud resource prefix"
  default     = "playground"
}
