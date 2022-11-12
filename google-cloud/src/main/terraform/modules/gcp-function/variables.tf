variable "entry_point" {
  type        = string
  description = "Google Cloud Function fully qualified class name"
}

variable "function_description" {
  type        = string
  description = "Google Cloud Function description"
  default     = "Create a new Google Cloud Function"
}

variable "function_name" {
  type        = string
  description = "Google Cloud Function name"
}

variable "project_id" {
  type        = string
  description = "Google Cloud project id to create workload identity provider"
}

variable "project_region" {
  type        = string
  description = "Google Cloud region to deploy functions"
}

variable "runtime" {
  type        = string
  description = "Google Cloud Function runtime"
  default     = "java11"
}

variable "source_file" {
  type        = string
  description = "Google Cloud Function source file"
}
