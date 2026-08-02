locals {
  api_targets = [
    google_project_service.api_ashdavies_dev.service,
    google_project_service.firebaseappcheck_googleapis_com.service,
    google_project_service.firebaseinstallations_googleapis_com.service,
    google_project_service.firebaseremoteconfig_googleapis_com.service,
    google_project_service.identitytoolkit_googleapis_com.service
  ]
}

resource "google_apikeys_key" "android_debug" {
  display_name = "Android key (auto created by Firebase)"
  name         = "ecc12a8f-74fe-4f10-bede-c60d4b5db5e3"
  project      = var.project_id

  restrictions {
    android_key_restrictions {
      allowed_applications {
        package_name     = "dev.ashdavies.playground.debug"
        sha1_fingerprint = "fab7388053ba85ca62c23824ed98b2b73ec259cf"
      }
    }

    dynamic "api_targets" {
      for_each = local.api_targets
      content {
        service = api_targets.value
      }
    }
  }
}

resource "google_apikeys_key" "android_release" {
  display_name = "Android key (auto created by Firebase)"
  name         = "75dd5a3f-abd4-4f48-bb63-58ebad5ea3e8"
  project      = var.project_id

  restrictions {
    android_key_restrictions {
      allowed_applications {
        package_name     = "dev.ashdavies.playground"
        sha1_fingerprint = "9ae708c691c74827422b33586cdc4d11535c3595"
      }

      allowed_applications {
        package_name     = "dev.ashdavies.playground"
        sha1_fingerprint = "e7cd022a23e47b3d09940af0cd1f85d0928d1abd"
      }
    }

    dynamic "api_targets" {
      for_each = local.api_targets
      content {
        service = api_targets.value
      }
    }
  }
}

resource "google_apikeys_key" "browser" {
  display_name = "Browser key (auto created by Firebase)"
  name         = "ce7cc75b-bc2e-4c6c-b1f5-d7110248b16d"
  project      = var.project_id

  restrictions {
    browser_key_restrictions {
      allowed_referrers = [
        "playground.ashdavies.dev",
        "localhost"
      ]
    }

    dynamic "api_targets" {
      for_each = local.api_targets
      content {
        service = api_targets.value
      }
    }
  }
}

resource "google_cloud_run_service" "build" {
  name     = "playground-service"
  location = var.project_region

  template {
    spec {
      containers {
        image = data.google_artifact_registry_docker_image.main.self_link
      }
    }
  }

  traffic {
    latest_revision = true
    percent         = 100
  }
}

data "google_artifact_registry_docker_image" "main" {
  location      = var.project_region
  repository_id = "cloud-run-source-deploy"
  image_name    = google_project_service.api_ashdavies_dev.service
}

resource "google_project_service" "api_ashdavies_dev" {
  project = var.project_id
  service = "api.ashdavies.dev"
}

resource "google_project_service" "firebaseappcheck_googleapis_com" {
  project = var.project_id
  service = "firebaseappcheck.googleapis.com"
}

resource "google_project_service" "firebaseinstallations_googleapis_com" {
  project = var.project_id
  service = "firebaseinstallations.googleapis.com"
}

resource "google_project_service" "firebaseremoteconfig_googleapis_com" {
  project = var.project_id
  service = "firebaseremoteconfig.googleapis.com"
}

resource "google_project_service" "identitytoolkit_googleapis_com" {
  project = var.project_id
  service = "identitytoolkit.googleapis.com"
}