terraform {
  backend "gcs" {
    bucket  = "playground-bucket-tfstate"
    prefix  = "eb9d47b8"
  }
}
