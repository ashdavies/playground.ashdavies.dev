variable "op_service_account_token" {
  description = "A valid token for the 1Password Service Account."
  type        = string
}

variable "gh_owner" {
  default     = "ashdavies"
  description = "Username of the GitHub repository owner"
  type        = string
}

variable "gh_repo_name" {
  default     = "playground.ashdavies.dev"
  description = "GitHub repository to set actions secrets"
  type        = string
}

variable "gh_token" {
  description = "GitHub personal access token"
  type        = string
}

variable "openapi_config" {
  default     = "../openapi-v2.yml"
  description = "The full path of the OpenAPI configuration."
  type        = string
}

variable "project_id" {
  default     = "playground-1a136"
  description = "Google Cloud project id"
  type        = string
}

variable "project_region" {
  default     = "europe-west1"
  description = "Google Cloud project region"
  type        = string
}
