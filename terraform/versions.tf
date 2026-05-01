terraform {
  required_providers {
    github = {
      source  = "integrations/github"
      version = "6.12.1"
    }

    google = {
      source  = "hashicorp/google"
      version = "7.30.0"
    }

    google-beta = {
      source  = "hashicorp/google-beta"
      version = "7.29.0"
    }

    onepassword = {
      source  = "1Password/onepassword"
      version = "3.3.1"
    }
  }

  required_version = "1.15.0"
}
