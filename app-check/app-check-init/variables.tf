variable "project_id" {
  type        = string
  description = "The project id to create WIF pool and example SA"
  default     = "playground-1a136"
}

variable "gh_repo_name" {
  type        = string
  description = "The GitHub repository to set actions secrets"
  default     = "ashdavies/playground"
}
