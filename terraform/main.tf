locals {
  openapi_config = templatefile(var.openapi_config, {
    backend_service_name = module.cloud-run-build.url
  })
}

resource "google_firebase_android_app" "main" {
  provider      = google-beta
  display_name  = "Playground"
  package_name = "io.ashdavies.playground"
  sha1_hashes   = ["4761bd71b66b1cda731f4c5fef4a554785f7648b"]
  sha256_hashes = ["b0df48830f81daafc80955b552b3e85aa93c56c7a4172f56358067b071f1f686"]
}

import {
  id = "projects/playground-1a136/androidApps/1:279861227938:android:8b4e86b064ce7429c502fb"
  to = google_firebase_android_app.main
}

# google_project_service.main is deprecated
resource "google_project_service" "main" {
  service            = module.cloud-run-endpoint.service_name
  depends_on         = [module.cloud-run-endpoint]
  project            = var.project_id
}

resource "google_project_iam_custom_role" "main" {
  description = "Can create, update, and delete services necessary for the automatic deployment"
  title       = "GitHub Actions Publisher"
  provider    = google.impersonated
  role_id     = "actionsPublisher"
  permissions = [
    "apigateway.apiconfigs.create",
    "apigateway.gateways.update",
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
    "storage.buckets.create",
    "storage.buckets.getIamPolicy",
    "storage.buckets.setIamPolicy",
    "storage.buckets.list",
    "storage.objects.create",
    "storage.objects.delete",
    "storage.objects.list",
  ]
}
