locals {
  openapi_config = templatefile(var.openapi_config, {
    backend_service_name = "http://127.0.0.1:8080"
  })
}

data "google_project" "main" {
  project_id = var.project_id
}

resource "google_project_service" "main" {
  service = google_endpoints_service.main.service_name
  project = var.project_id
}

resource "google_project_iam_custom_role" "actions_publisher" {
  title       = "GitHub Actions Publisher"
  description = "Managed by Terraform"
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
    "iam.roles.get",
    "iam.roles.update",
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
  stage = "BETA"
}

resource "google_project_iam_custom_role" "run_executor" {
  title       = "Cloud Run Executor"
  description = "Managed by Terraform"
  role_id     = "runExecutor"
  permissions = [
    "datastore.databases.get",
    "datastore.entities.get",
    "datastore.entities.list",
    "iam.roles.get",
    "run.instances.invoke",
    "run.jobs.run",
    "run.routes.invoke",
    "servicemanagement.services.check",
    "servicemanagement.services.get",
    "servicemanagement.services.list",
    "servicemanagement.services.report",
  ]
  stage = "BETA"
}

resource "google_service_account_key" "fastlane_supply_key" {
  service_account_id = module.fastlane_service_account.service_account.name
}
