terraform {
  required_providers {
    docker = {
      source = "kreuzwerker/docker"
      version = "~> 3.0.0"
    }

    github = {
      source  = "integrations/github"
      version = "~> 5.0"
    }

    google = {
      source  = "hashicorp/google"
      version = "~> 4.0"
    }

    # usage local_file is deprecated
    local = {
      source = "hashicorp/local"
    }
  }

  required_version = ">= 0.13"
}
