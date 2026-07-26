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

variable "allowed_applications" {
  type        = list(object({
    package_name     = string
    sha1_fingerprint = string
  }))
  default     = []
  description = "A list of Android applications that are allowed to make API calls with this key."
}

variable "api_targets" {
  type        = set(string)
  description = <<EOT
  (Optional) A restriction for a specific service and optionally one or more specific methods.
  Requests are allowed if they match any of these restrictions. If no restrictions are specified,
  all targets are allowed.
  EOT
  default     = []
}
