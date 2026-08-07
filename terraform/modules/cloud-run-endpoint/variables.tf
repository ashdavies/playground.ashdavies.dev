variable "config_id" {
  description = "The id of the service configuration resource."
}

variable "endpoint_name" {
  description = ""
}

variable "esp_version" {
  type        = string
  description = "ESPv2 version"
  default     = "2.40.0"
}

variable "image_name" {
  description = <<EOT
  "The image name to fetch. If no digest or tag is provided, then the latest modified image will be used."
  EOT
}

variable "image_repository" {
  description = ""
}

variable "location" {
  description = "The location of the cloud run instance. e.g. europe-west1."
}

variable "openapi_config" {
  description = "The full text of the OpenAPI configuration."
}

variable "service_name" {
  description = <<EOT
  Name must be unique within a namespace, within a Cloud Run region. Is required when creating
  resources. Name is primarily intended for creation idempotence and configuration definition.
  Cannot be updated. More info: http://kubernetes.io/docs/user-guide/identifiers#names
  EOT
}

variable "project" {
  description = "The project for the resource."
}

variable "repository_id" {
  description = "The last part of the repository name to fetch from."
}
