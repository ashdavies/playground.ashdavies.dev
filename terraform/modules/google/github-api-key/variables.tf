variable "service" {
  description = <<EOT
  (Optional) A restriction for a specific service. It should be the canonical service name, for
  example: translate.googleapis.com. You can use gcloud services list to get a list of services
  that are enabled in the project. Requests are allowed if they match any of these restrictions.
  If no restrictions are specified, all targets are allowed.
  EOT
  default     = null
}

variable "display_name" {
  description = "Human-readable display name of this API key. Modifiable by user."
}

variable "name" {
  description = <<EOT
  The resource name of the key. The name must be unique within the project, must conform with
  RFC-1034, is restricted to lower-cased letters, and has a maximum length of 63 characters. In
  another words, the name must match the regular expression: `[a-z]([a-z0-9-]{0,61}[a-z0-9])?`.
  EOT
}

variable "project" {
  description = "The project for the resource."
}

variable "repository" {
  description = "Name of the repository."
}

variable "secret_name" {
  description = "Name of the secret."
}
