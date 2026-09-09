terraform {
  required_providers {
    cloudflare = {
      source  = "cloudflare/cloudflare"
      version = "~> 5"
    }

    github = {
      source  = "integrations/github"
      version = "6.13.0"
    }

    google = {
      source  = "hashicorp/google"
      version = "8.2.0"
    }

    google-beta = {
      source  = "hashicorp/google-beta"
      version = "7.46.1"
    }

    onepassword = {
      source  = "1Password/onepassword"
      version = "3.3.1"
    }
  }

  required_version = ">= 1.15.8"
}
