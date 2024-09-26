terraform {
  required_providers {
    github = {
      source  = "integrations/github"
      version = "6.3.0"
    }

    google = {
      source  = "hashicorp/google"
      version = "5.44.0"
    }
  }

  required_version = "1.9.6"
}
