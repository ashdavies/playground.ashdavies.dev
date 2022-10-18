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
  description = "Project id to create WIF pool and example SA"
  default     = "playground-1a136"
}
