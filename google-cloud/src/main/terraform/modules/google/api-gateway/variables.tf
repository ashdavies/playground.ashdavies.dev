variable "api_id" {
  description = "Identifier to assign to the API."
}

variable "api_config_id" {
  description = "Identifier to assign to the API Config."
  default     = null
}

variable "display_name" {
  description = "A user-visible name for the API."
  default     = null
}

variable "gateway_id" {
  description = "Identifier to assign to the Gateway."
}

variable "openapi_contents" {
  description = "Base64 encoded content of the file."
}

variable "openapi_path" {
  description = "The file path (full or relative path)."
  default     = "openapi_spec.yml"
}

variable "project" {
  description = "The project for the resource."
}

variable "region" {
  description = "The region of the gateway for the API."
}
