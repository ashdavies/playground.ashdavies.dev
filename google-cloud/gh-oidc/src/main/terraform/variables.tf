variable "gh_owner" {
  description = "Username of the GitHub repository owner"
  default     = "ashdavies"
}

variable "gh_repo_name" {
  description = "GitHub repository to set actions secrets"
  default     = "playground.ashdavies.dev"
}

variable "gh_token" {
  description = "GitHub personal access token"
}

variable "project_id" {
  description = "Google Cloud project id to create workload identity provider"
  default     = "playground-1a136"
}

variable "project_region" {
  description = "Google Cloud region to deploy functions"
  default     = "europe-west1"
}
