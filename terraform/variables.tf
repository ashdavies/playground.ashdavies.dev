# variable.esp_tag is deprecated
variable "esp_tag" {
  description = "ESPv2 version"
  default     = "2.40.0"
}

variable "gcloud_build_image" {
  description = "GCloud build image script"
  default     = "./gcloud_build_image"
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
  default     = "playground.ashdavies.dev"
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
