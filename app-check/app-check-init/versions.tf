terraform {
  required_version = ">= 0.13"
  required_providers {

    google = {
      source  = "hashicorp/google"
      version = ">= 3.64, < 5.0.0"
    }
  }

  provider_meta "google" {
    module_name = "blueprints/terraform/terraform-google-github-actions-runners:gh-oidc/v3.1.0"
  }

  provider_meta "google-beta" {
    module_name = "blueprints/terraform/terraform-google-github-actions-runners:gh-oidc/v3.1.0"
  }
}
