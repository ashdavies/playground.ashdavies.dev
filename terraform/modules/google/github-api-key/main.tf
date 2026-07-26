resource "google_apikeys_key" "main" {
  display_name = var.display_name
  project      = var.project
  name         = var.name

  restrictions {
    dynamic "android_key_restrictions" {
      for_each = length(var.allowed_applications) > 0 ? [1] : []
      content {
        dynamic "allowed_applications" {
          for_each = var.allowed_applications
          content {
            package_name     = allowed_applications.value.package_name
            sha1_fingerprint = allowed_applications.value.sha1_fingerprint
          }
        }
      }
    }

    dynamic "api_targets" {
      for_each = var.api_targets
      content {
        service = api_targets.value
      }
    }
  }

  depends_on = [google_project_service.target]
}

resource "google_project_service" "target" {
  for_each = var.api_targets
  project  = var.project
  service  = each.value
}