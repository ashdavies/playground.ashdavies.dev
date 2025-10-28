resource "github_actions_secret" "main" {
  plaintext_value = google_apikeys_key.main.key_string
  secret_name     = var.secret_name
  repository      = var.repository
}

resource "google_apikeys_key" "main" {
  display_name = var.display_name
  project      = var.project
  name         = var.name

  depends_on   = [
    google_project_service.apikeys,
    google_project_service.target,
  ]

  dynamic "restrictions" {
    for_each = var.service[*]

    content {
      api_targets {
        service = var.service
      }
    }
  }
}

resource "google_project_service" "apikeys" {
  service = "apikeys.googleapis.com"
  project = var.project
}

resource "google_project_service" "target" {
  for_each = toset(var.service != null ? [var.service] : [])
  project = var.project
  service = var.service
}
