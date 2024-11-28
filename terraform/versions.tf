terraform {
  required_providers {
    github = {
      source  = "integrations/github"
      version = "6.4.0"
    }

    google = {
      source  = "hashicorp/google"
      version = "6.12.0"
    }
  }

  required_version = "1.10.0"
}
