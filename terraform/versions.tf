terraform {
  required_providers {
    docker = {
      source = "kreuzwerker/docker"
      version = "3.0.2"
    }

    github = {
      source  = "integrations/github"
      version = "5.36.0"
    }

    google = {
      source  = "hashicorp/google"
      version = "4.82.0"
    }
  }

  required_version = "1.5.7"
}
