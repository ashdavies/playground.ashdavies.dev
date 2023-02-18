variable "docker_image" {
  description = <<EOT
  Docker image name. This is most often a reference to a container located in the container
  registry, such as europe-west1-docker.pkg.dev/project/cloud-run-source-deploy/artifact.
  More info: https://kubernetes.io/docs/concepts/containers/images
  EOT
}

variable "latest_revision" {
  description = <<EOT
  LatestRevision may be optionally provided to indicate that the latest ready Revision of the
  Configuration should be used for this traffic target. When provided LatestRevision must be true
  if RevisionName is empty; it must be false when RevisionName is non-empty.
  EOT
  default = true
}

variable "location" {
  description = "The location of the cloud run instance. e.g. europe-west1."
}

variable "percent" {
  description = "Percent specifies percent of the traffic to this Revision or Configuration."
  default     = 100
}

variable "project" {
  description = "The project for the resource."
}

variable "service_name" {
  description = <<EOT
  Name must be unique within a namespace, within a Cloud Run region. Is required when creating
  resources. Name is primarily intended for creation idempotence and configuration definition.
  Cannot be updated. More info: http://kubernetes.io/docs/user-guide/identifiers#names
  EOT
}
