locals {
  openapi_config = templatefile(var.openapi_config, {
    backend_service_name = module.cloud-run-build.url
  })
}

resource "google_firebase_android_app" "debug" {
  provider      = google-beta
  project       = var.project_id
  display_name  = "Tally Debug"
  package_name  = "io.ashdavies.tally.debug"
  sha1_hashes   = ["2f50f8fd822a6592508b5adb58a231c729d616a3"]
  sha256_hashes = ["2ebfd15ae4682189f047b61a4faa903dc330cbf29b3a21f667595a73fb53a1ff"]
}

resource "google_firebase_android_app" "release" {
  provider      = google-beta
  project       = var.project_id
  display_name  = "Tally Release"
  package_name  = "io.ashdavies.tally"
  sha1_hashes   = ["a75f572e40a926bb1708178c134a1e86faadaa09"]
  sha256_hashes = ["0442b951b2d82cb339a7e4ef9469e71e89b182c7be5efb119f587cc2b19995bc"]
}

# google_project_service.main is deprecated
resource "google_project_service" "main" {
  service    = module.cloud-run-endpoint.service_name
  depends_on = [module.cloud-run-endpoint]
  project    = var.project_id
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
    "servicemanagement.services.create",
    "servicemanagement.services.delete",
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
