terraform {
  required_providers {
    docker = {
      source = "kreuzwerker/docker"
      version = "3.0.2"
    }

    github = {
      source  = "integrations/github"
      version = "5.40.0"
    }

    google = {
      source  = "hashicorp/google"
      version = "5.3.0"
    }
  }

  required_version = "1.6.2"
}
