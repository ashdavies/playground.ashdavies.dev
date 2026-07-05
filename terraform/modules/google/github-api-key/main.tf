resource "google_apikeys_key" "main" {
  display_name = var.display_name
  project      = var.project
  name         = var.name

  depends_on = [google_project_service.target]

  dynamic "restrictions" {
    for_each = length(var.service) > 0 ? [1] : []

    content {
      dynamic "api_targets" {
        for_each = var.service
        content {
          service = api_targets.value
        }
      }
    }
  }
}

resource "google_project_service" "target" {
  for_each = var.service
  project  = var.project
  service  = each.value
}
