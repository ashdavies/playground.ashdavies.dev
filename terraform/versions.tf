terraform {
  required_providers {
    github = {
      source  = "integrations/github"
      version = "6.10.2"
    }

    google = {
      source  = "hashicorp/google"
      version = "7.17.0"
    }

    google-beta = {
      source  = "hashicorp/google-beta"
      version = "7.16.0"
    }

    onepassword = {
      source  = "1Password/onepassword"
      version = "3.1.2"
    }
  }

  required_version = "1.14.4"
}
