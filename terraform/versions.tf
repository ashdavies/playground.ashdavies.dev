terraform {
  required_providers {
    github = {
      source  = "integrations/github"
      version = "6.6.0"
    }

    google = {
      source  = "hashicorp/google"
      version = "6.44.0"
    }

    google-beta = {
      source  = "hashicorp/google-beta"
      version = "6.44.0"
    }

    onepassword = {
      source  = "1Password/onepassword"
      version = "2.1.2"
    }
  }

  required_version = "1.12.2"
}
