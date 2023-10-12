module "project-services" {
  source        = "terraform-google-modules/project-factory/google//modules/project_services"
  version       = "14.3.0"
  project_id    = var.project
  activate_apis = [
    "apigateway.googleapis.com",
    "servicemanagement.googleapis.com",
    "servicecontrol.googleapis.com"
  ]
}

resource "google_api_gateway_api" "main" {
  display_name = var.display_name
  project      = var.project
  provider     = google-beta
  api_id       = var.api_id
}

resource "google_api_gateway_api_config" "main" {
  api           = google_api_gateway_api.main.api_id
  api_config_id = var.api_config_id
  display_name  = var.display_name
  project       = var.project
  provider      = google-beta

  openapi_documents {
    document {
      contents = var.openapi_contents
      path     = var.openapi_path
    }
  }

  lifecycle {
    create_before_destroy = true
  }
}

/*resource "google_api_gateway_api_iam_binding" "main" {
  api      = google_api_gateway_api.main.api_id
  role     = "roles/run.invoker"
  members  = ["allUsers"]
  provider = google-beta
  project  = var.project
}*/

resource "google_api_gateway_gateway" "main" {
  api_config   = google_api_gateway_api_config.main.id
  depends_on   = [google_api_gateway_api_config.main]
  display_name = var.display_name
  gateway_id   = var.gateway_id
  project      = var.project
  provider     = google-beta
  region       = var.region
}
