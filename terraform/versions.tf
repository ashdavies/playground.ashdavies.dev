terraform {
  required_providers {
    docker = {
      source = "kreuzwerker/docker"
      version = "3.0.2"
    }

    github = {
      source  = "integrations/github"
      version = "5.39.0"
    }

    google = {
      source  = "hashicorp/google"
      version = "4.84.0"
    }
  }

  required_version = "1.6.1"
}
