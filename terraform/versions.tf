terraform {
  required_providers {
    github = {
      source  = "integrations/github"
      version = "6.7.0"
    }

    google = {
      source  = "hashicorp/google"
      version = "7.7.0"
    }

    google-beta = {
      source  = "hashicorp/google-beta"
      version = "7.8.0"
    }

    onepassword = {
      source  = "1Password/onepassword"
      version = "2.1.2"
    }
  }

  required_version = "1.13.4"
}
