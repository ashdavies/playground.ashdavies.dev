variable "gh_owner" {
  type        = string
  description = "Username of the GitHub repository owner"
}

variable "gh_repo_name" {
  type        = string
  description = "GitHub repository to set actions secrets"
}

variable "project_id" {
  type        = string
  description = "Google Cloud project id to create workload identity provider"
  default     = "playground-1a136"
}
