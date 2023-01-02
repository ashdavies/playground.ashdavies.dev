resource "google_project_service" "apikeys" {
  service            = "apikeys.googleapis.com"
  project            = var.project_id
}

resource "google_project_service" "identitytoolkit" {
  service            = "identitytoolkit.googleapis.com"
  project            = var.project_id
}
