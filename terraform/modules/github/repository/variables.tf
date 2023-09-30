variable "repository" {
  description = "Name of the repository."
}

variable "description" {
  description = "A description of the repository."
  default     = null
}

variable "homepage_url" {
  description = "(optional) describe your variable"
  type        = string
}

variable "labels" {
  description = "The list of GitHub issue labels to create on this repository."
  default     = []
  type        = list(object({
    name        = string
    description = string
    color       = string
  }))
}

variable "secrets" {
  description = "The list of GitHub Actions secrets to create on this repository."
  default     = []
  type        = list(object({
    plaintext_value = string
    secret_name     = string
  }))
}

variable "topics" {
  description = "A list of topics to add to the repository."
  default     = []
  type        = list(string)
}