terraform {
  required_providers {
    github = {
      source  = "integrations/github"
      version = "6.5.0"
    }

    google = {
      source  = "hashicorp/google"
      version = "6.18.1"
    }
  }

  required_version = "1.10.5"
}
