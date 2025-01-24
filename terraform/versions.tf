terraform {
  required_providers {
    github = {
      source  = "integrations/github"
      version = "6.5.0"
    }

    google = {
      source  = "hashicorp/google"
      version = "6.17.0"
    }
  }

  required_version = "1.10.5"
}
