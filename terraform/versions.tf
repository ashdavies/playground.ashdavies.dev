terraform {
  required_providers {
    github = {
      source  = "integrations/github"
      version = "6.7.5"
    }

    google = {
      source  = "hashicorp/google"
      version = "7.10.0"
    }

    google-beta = {
      source  = "hashicorp/google-beta"
      version = "7.9.0"
    }

    onepassword = {
      source  = "1Password/onepassword"
      version = "2.2.0"
    }
  }

  required_version = "1.13.5"
}
