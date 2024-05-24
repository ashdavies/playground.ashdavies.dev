terraform {
  required_providers {
    docker = {
      source = "kreuzwerker/docker"
      version = "3.0.2"
    }

    github = {
      source  = "integrations/github"
      version = "6.2.1"
    }

    google = {
      source  = "hashicorp/google"
      version = "5.30.0"
    }
  }

  required_version = "1.8.4"
}
