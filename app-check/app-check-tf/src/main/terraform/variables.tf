variable "gh_owner" {
  type        = string
  description = "Username of the GitHub repository owner"
  default     = "ashdavies"
}

variable "gh_repo_name" {
  type        = string
  description = "GitHub repository to set actions secrets"
  default     = "playground"
}

variable "gh_token" {
  type        = string
  description = "GitHub personal access token"
}

variable "project_id" {
  type        = string
  description = "Google Cloud project id to create workload identity provider"
  default     = "playground-1a136"
}

variable "project_region" {
  type        = string
  description = "Google Cloud region to deploy functions"
  default     = "europe-west1"
}

variable "project_dir" {
  type        = string
  description = "Project directory set by gradle script"
}

variable "source_dir" {
  type        = string
  description = "Source directory for the compiled cloud function"
  default     = "../app-check-function/build/libs"
}
