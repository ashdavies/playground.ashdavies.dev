locals {
  api_targets = [
    "api.ashdavies.dev",
    "firebaseappcheck.googleapis.com",
    "firebaseinstallations.googleapis.com",
    "firebaseremoteconfig.googleapis.com",
    "identitytoolkit.googleapis.com",
  ]

  enabled_apis = [
    "apigateway.googleapis.com",
    "servicecontrol.googleapis.com",
    "servicemanagement.googleapis.com",
  ]
}

resource "google_api_gateway_api" "main" {
  project  = var.project_id
  provider = google-beta
  api_id   = "playground-api"
}

resource "google_api_gateway_api_config" "main" {
  api      = google_api_gateway_api.main.api_id
  project  = var.project_id
  provider = google-beta

  openapi_documents {
    document {
      contents = base64encode(local.openapi_config)
      path     = "openapi_spec.yml"
    }
  }

  lifecycle {
    create_before_destroy = true
  }
}

resource "google_api_gateway_gateway" "main" {
  api_config = google_api_gateway_api_config.main.id
  depends_on = [google_api_gateway_api_config.main]
  gateway_id = "playground-api-gateway"
  project    = var.project_id
  provider   = google-beta
  region     = var.project_region
}

resource "google_apikeys_key" "android" {
  display_name = "Android key (auto created by Firebase)"
  name         = "2a2d6e2d-f140-4546-bcb2-358042878757"
  project      = var.project_id

  restrictions {
    android_key_restrictions {
      /*allowed_applications {
        package_name     = "dev.ashdavies.playground.debug"
        sha1_fingerprint = "fab7388053ba85ca62c23824ed98b2b73ec259cf"
      }*/

      /*allowed_applications {
        package_name     = "dev.ashdavies.playground"
        sha1_fingerprint = "9ae708c691c74827422b33586cdc4d11535c3595"
      }*/

      /*allowed_applications {
        package_name     = "dev.ashdavies.playground"
        sha1_fingerprint = "e7cd022a23e47b3d09940af0cd1f85d0928d1abd"
      }*/
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

resource "google_apikeys_key" "desktop" {
  display_name = "Desktop key (auto created by Firebase)"
  name         = "eb05ae8e-f26f-4e66-b3fb-01681b414a21"
  project      = var.project_id

  restrictions {
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
  image_name    = "api.ashdavies.dev"
}

module "project_services" {
  source        = "terraform-google-modules/project-factory/google//modules/project_services"
  version       = "18.3.0"
  project_id    = var.project_id
  activate_apis = concat(local.api_targets, local.enabled_apis)
}
