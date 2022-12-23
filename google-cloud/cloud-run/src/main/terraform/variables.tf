variable "esp_tag" {
  description = "ESPv2 version"
  default     = "2.40.0"
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
