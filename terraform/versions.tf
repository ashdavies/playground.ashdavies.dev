terraform {
  required_providers {
    github = {
      source  = "integrations/github"
      version = "6.8.3"
    }

    google = {
      source  = "hashicorp/google"
      version = "7.13.0"
    }

    google-beta = {
      source  = "hashicorp/google-beta"
      version = "7.13.0"
    }

    onepassword = {
      source  = "1Password/onepassword"
      version = "2.2.1"
    }
  }

  required_version = "1.14.1"
}
