terraform {
  required_providers {
    docker = {
      source = "kreuzwerker/docker"
      version = "3.0.2"
    }

    github = {
      source  = "integrations/github"
      version = "6.0.0"
    }

    google = {
      source  = "hashicorp/google"
      version = "5.18.0"
    }
  }

  required_version = "1.7.4"
}
