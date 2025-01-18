terraform {
  required_providers {
    github = {
      source  = "integrations/github"
      version = "6.5.0"
    }

    google = {
      source  = "hashicorp/google"
      version = "6.16.0"
    }
  }

  required_version = "1.10.4"
}
