terraform {
  required_providers {
    docker = {
      source = "kreuzwerker/docker"
      version = "3.0.2"
    }

    github = {
      source  = "integrations/github"
      version = "5.45.0"
    }

    google = {
      source  = "hashicorp/google"
      version = "5.13.0"
    }
  }

  required_version = "1.7.1"
}
