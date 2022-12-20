terraform {
  backend "gcs" {
    bucket  = "da4c632d-bucket-tfstate"
    prefix  = "terraform/state"
  }
}
