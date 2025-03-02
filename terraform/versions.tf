terraform {
  required_providers {
    github = {
      source  = "integrations/github"
      version = "6.6.0"
    }

    google = {
      source  = "hashicorp/google"
      version = "6.23.0"
    }
  }

  required_version = "1.11.0"
}
