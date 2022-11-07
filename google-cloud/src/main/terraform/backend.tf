terraform {
  backend "gcs" {
    bucket  = "e8497b5efcc3f6c3-bucket-tfstate"
    prefix  = "terraform/state"
  }
}
