terraform {
  required_providers {
    github = {
      source  = "integrations/github"
      version = "6.12.0"
    }

    google = {
      source  = "hashicorp/google"
      version = "7.29.0"
    }

    google-beta = {
      source  = "hashicorp/google-beta"
      version = "7.28.0"
    }

    onepassword = {
      source  = "1Password/onepassword"
      version = "3.3.1"
    }
  }

  required_version = "1.14.9"
}
