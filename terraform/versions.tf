terraform {
  required_providers {
    github = {
      source  = "integrations/github"
      version = "6.3.1"
    }

    google = {
      source  = "hashicorp/google"
      version = "6.11.1"
    }
  }

  required_version = "1.9.8"
}
