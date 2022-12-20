terraform {
  backend "gcs" {
    bucket  = "eb9d47b8-bucket-tfstate"
    prefix  = "terraform/state"
  }
}
