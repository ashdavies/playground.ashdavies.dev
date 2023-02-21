terraform {
  required_providers {
    docker = {
      source = "kreuzwerker/docker"
      version = "~> 2.9.0"
    }

    github = {
      source  = "integrations/github"
      version = "~> 5.0"
    }

    google = {
      source  = "hashicorp/google"
      version = "~> 4.0"
    }
  }

  required_version = ">= 0.13"
}
