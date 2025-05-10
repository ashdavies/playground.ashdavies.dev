variable "repository" {
  description = "Name of the repository."
}

variable "description" {
  description = "A description of the repository."
  default     = null
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

variable "topics" {
  description = "The list of topics of the repository."
  default     = []
  type        = list(string)
}
