variable "esp_tag" {
  type        = string
  description = "ESPv2 version"
  default     = "2.40.0"
}

variable "project_id" {
  type        = string
  description = "Google Cloud project id to create workload identity provider"
}

variable "project_region" {
  type        = string
  description = "Google Cloud region to deploy functions"
}
