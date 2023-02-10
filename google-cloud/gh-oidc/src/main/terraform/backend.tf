terraform {
  backend "gcs" {
    bucket  = "playground-bucket-tfstate"
    prefix  = "da4c632d"
  }
}
