variable "esp_tag" {
  description = "ESPv2 version"
  default     = "2.40.0"
}

variable "project_id" {
  description = "Google Cloud project id to create workload identity provider"
  default     = "playground-1a136"
}

variable "project_region" {
  description = "Google Cloud region to deploy functions"
  default     = "europe-west1"
}
