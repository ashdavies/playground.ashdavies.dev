terraform {
  required_providers {
    github = {
      source  = "integrations/github"
      version = "6.3.1"
    }

    google = {
      source  = "hashicorp/google"
      version = "6.9.0"
    }
  }

  required_version = "1.9.8"
}
