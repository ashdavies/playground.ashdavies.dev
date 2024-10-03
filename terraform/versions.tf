terraform {
  required_providers {
    github = {
      source  = "integrations/github"
      version = "6.3.0"
    }

    google = {
      source  = "hashicorp/google"
      version = "5.44.1"
    }
  }

  required_version = "1.9.7"
}
