terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "~> 4.0"
    }
    docker = {
      source = "kreuzwerker/docker"
      version = "~> 2.9.0"
    }
  }
  required_version = ">= 0.13"
}
