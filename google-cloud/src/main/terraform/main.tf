resource "google_endpoints_service" "endpoints" {
  service_name   = var.service_name
  project        = var.project_id
  openapi_config = templatefile(var.resources.openapi-v2_yaml.path, {
    backend_service_name = google_cloud_run_service.service.status[0].url
    cloud_run_hostname   = var.service_name
  })
}

data "google_iam_policy" "noauth" {
  binding {
    role = "roles/run.invoker"
    members = ["allUsers"]
  }
}

resource "google_project_iam_custom_role" "main" {
  description = "Can create, update, and delete services necessary for the automatic deployment"
  title       = "GitHub Actions Publisher"
  role_id     = "actionsPublisher"
  permissions = [
    "artifactregistry.repositories.uploadArtifacts",
    "cloudbuild.builds.create",
    "iam.serviceAccounts.actAs",
    "iam.serviceAccounts.getAccessToken",
    "iam.serviceAccounts.signBlob",
    "resourcemanager.projects.get",
    "run.services.get",
    "run.services.getIamPolicy",
    "run.services.update",
    "servicemanagement.services.get",
    "servicemanagement.services.update",
    "serviceusage.services.list",
    "storage.buckets.list",
    "storage.objects.create",
    "storage.objects.delete",
    "storage.objects.list",
  ]
}

resource "google_project_iam_member" "main" {
  member  = "serviceAccount:${google_service_account.main.email}"
  role    = google_project_iam_custom_role.main.id
  project = var.project_id
}

resource "google_project_iam_member" "viewer" {
  member  = "serviceAccount:${google_service_account.main.email}"
  project = var.project_id
  role    = "roles/viewer"
}

resource "google_project_service" "main" {
  service            = google_endpoints_service.endpoints.service_name
  depends_on         = [google_endpoints_service.endpoints]
  project            = var.project_id
}

resource "google_service_account" "main" {
  display_name = "GitHub Service Account"
  project      = var.project_id
  account_id   = "gh-oidc"
}

resource "null_resource" "openapi_proxy_image" {
  triggers = {
    config_id = google_endpoints_service.endpoints.config_id
  }

  provisioner "local-exec" {
    command = <<EOS
    bash ${var.resources.gcloud-build-image.path} \
      -c ${google_endpoints_service.endpoints.config_id} \
      -g ${local.endpoints_registry} \
      -s ${var.service_name} \
      -p ${var.project_id} \
      -v ${var.esp_tag}
    EOS
  }
}
