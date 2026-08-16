locals {
  api_targets = [
    "api.ashdavies.dev",
    "firebaseappcheck.googleapis.com",
    "firebaseinstallations.googleapis.com",
    "firebaseremoteconfig.googleapis.com",
    "identitytoolkit.googleapis.com",
  ]

  enabled_apis = [
    "servicecontrol.googleapis.com",
    "servicemanagement.googleapis.com",
  ]

  # Added automatically by Firebase
  unused_apis = [
    "firebasedatabase.googleapis.com",
    "firebasehosting.googleapis.com",
    "firebaserules.googleapis.com",
    "sqladmin.googleapis.com",
    "cloudconfig.googleapis.com",
    "datastore.googleapis.com",
    "fcmregistrations.googleapis.com",
    "firebase.googleapis.com",
    "firebaseappdistribution.googleapis.com",
    "firebaseapphosting.googleapis.com",
    "firebaseapptesters.googleapis.com",
    "firebasedataconnect.googleapis.com",
    "firebaseinappmessaging.googleapis.com",
    "firebaseml.googleapis.com",
    "firebaseremoteconfigrealtime.googleapis.com",
    "firebasestorage.googleapis.com",
    "firebasevertexai.googleapis.com",
    "firestore.googleapis.com",
    "logging.googleapis.com",
    "mlkit.googleapis.com",
    "play.googleapis.com",
    "securetoken.googleapis.com",
  ]
}


resource "google_apikeys_key" "android" {
  display_name = "Android key (auto created by Firebase)"
  name         = "2a2d6e2d-f140-4546-bcb2-358042878757"
  project      = var.project_id

  restrictions {
    android_key_restrictions {
      allowed_applications {
        package_name     = "dev.ashdavies.playground.debug"
        sha1_fingerprint = "fab7388053ba85ca62c23824ed98b2b73ec259cf"
      }

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

resource "google_apikeys_key" "android_firebase" {
  display_name = "Android key (auto created by Firebase)"
  name         = "e08dafd4-574e-4bd0-bdf7-d6120fbc0e9d"
  project      = var.project_id

  restrictions {
    dynamic "api_targets" {
      for_each = concat(local.api_targets, local.unused_apis)
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

resource "google_endpoints_service" "main" {
  openapi_config = local.openapi_config
  service_name   = "api.ashdavies.dev"
  project        = var.project_id
}

resource "google_cloud_run_service" "main" {
  name     = "playground-service"
  location = var.project_region
  project  = var.project_id

  metadata {
    annotations = {
      "run.googleapis.com/launch-stage" = "BETA"
    }
  }

  template {
    spec {
      containers {
        name  = "gateway"
        image = "gcr.io/endpoints-release/endpoints-runtime-serverless:2"

        args = [
          "--service=api.ashdavies.dev",
          "--rollout_strategy=managed",
          "--listener_port=8080",
          "--backend=http://127.0.0.1:8081"
        ]

        env {
          name  = "ENDPOINTS_SERVICE_NAME"
          value = "api.ashdavies.dev"
        }

        ports {
          container_port = 8080
        }
      }

      containers {
        name  = "backend"
        image = data.google_artifact_registry_docker_image.main.self_link

        env {
          name  = "PORT"
          value = "8081"
        }
      }
    }
  }

  traffic {
    latest_revision = true
    percent         = 100
  }

  lifecycle {
    prevent_destroy = true
  }
}

resource "google_cloud_run_domain_mapping" "main" {
  name     = "api.ashdavies.dev"
  location = google_cloud_run_service.main.location
  project  = var.project_id

  metadata {
    namespace = var.project_id
  }

  spec {
    route_name = google_cloud_run_service.main.name
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
