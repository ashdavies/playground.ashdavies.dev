variable "project_id" {
  type        = string
  description = "The project id to create WIF pool and example SA"
  default     = "sandbox-365610"
}

variable "gh_repo_name" {
  type        = string
  description = "The GitHub repository to set actions secrets"
  default     = "ashdavies/playground"
}

variable "gh_token" {
  type        = string
  description = "The GitHub personal access token"
}
