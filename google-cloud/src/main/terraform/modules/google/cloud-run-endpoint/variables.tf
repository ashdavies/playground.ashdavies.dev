variable "backend_service" {
  description = ""
}

variable "config_id" {
  description = "The id of the service configuration resource."
}

variable "container_image" {
  description = <<EOT
  Docker image name. This is most often a reference to a container located in the container
  registry, such as europe-west1-docker.pkg.dev/project/cloud-run-source-deploy/artifact.
  More info: https://kubernetes.io/docs/concepts/containers/images
  EOT
}

variable "endpoint_name" {
  description = ""
}

variable "esp_tag" {
  description = "ESPv2 version"
}

variable "gcloud_build_image" {
  description = "GCloud build image script"
}

variable "image_repository" {
  description = ""
}

variable "location" {
  description = "The location of the cloud run instance. e.g. europe-west1."
}

variable "openapi_config" {
  description = ""
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
