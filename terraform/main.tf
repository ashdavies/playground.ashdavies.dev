locals {
  openapi_config = templatefile(var.openapi_config, {
    backend_service_name = module.cloud-run-build.url
  })
}

resource "google_firebase_android_app" "main" {
  provider      = google-beta
  display_name  = "Playground"
  package_name = "io.ashdavies.playground"
  sha1_hashes   = ["2f50f8fd822a6592508b5adb58a231c729d616a3"]
  sha256_hashes = ["2ebfd15ae4682189f047b61a4faa903dc330cbf29b3a21f667595a73fb53a1ff"]
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
