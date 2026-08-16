locals {
  openapi_config = templatefile(var.openapi_config, {
    backend_service_name = "http://127.0.0.1:8081"
  })
}

resource "google_project_service" "main" {
  service = google_endpoints_service.main.service_name
  project = var.project_id
}

resource "google_project_iam_custom_role" "main" {
  description = "Can create, update, and delete services necessary for the automatic deployment"
  title       = "GitHub Actions Publisher"
  role_id     = "actionsPublisher"
  permissions = [
    "apigateway.apiconfigs.create",
    "apigateway.apiconfigs.delete",
    "apigateway.apiconfigs.get",
    "apigateway.gateways.update",
    "apigateway.apis.get",
    "apigateway.gateways.get",
    "apikeys.keys.get",
    "apikeys.keys.getKeyString",
    "apikeys.keys.update",
    "artifactregistry.dockerimages.list",
    "artifactregistry.repositories.get",
    "artifactregistry.repositories.uploadArtifacts",
    "cloudbuild.builds.create",
    "firebase.clients.get",
    "firebase.projects.get",
    "firebasehosting.sites.get",
    "iam.serviceAccountKeys.create",
    "iam.serviceAccountKeys.get",
    "iam.serviceAccounts.actAs",
    "iam.serviceAccounts.get",
    "iam.serviceAccounts.getAccessToken",
    "iam.serviceAccounts.getIamPolicy",
    "iam.serviceAccounts.signBlob",
    "iam.workloadIdentityPoolProviders.get",
    "iam.workloadIdentityPoolProviders.list",
    "iam.workloadIdentityPools.get",
    "iam.workloadIdentityPools.getAttestationRules",
    "resourcemanager.projects.get",
    "run.domainmappings.get",
    "run.services.get",
    "run.services.getIamPolicy",
    "run.services.update",
    "servicemanagement.services.get",
    "servicemanagement.services.update",
    "serviceusage.apiKeys.update",
    "serviceusage.services.list",
    "serviceusage.services.use",
    "storage.buckets.create",
    "storage.buckets.getIamPolicy",
    "storage.buckets.list",
    "storage.buckets.setIamPolicy",
    "storage.objects.create",
    "storage.objects.delete",
    "storage.objects.get",
    "storage.objects.list",
  ]
}

resource "google_service_account_key" "fastlane_supply_key" {
  service_account_id = module.fastlane_service_account.service_account.name
}
