/* cloud-build */

data "docker_registry_image" "service" {
  name = format(
    "%s-docker.pkg.dev/%s/cloud-run-source-deploy/%s",
    var.project_region,
    var.project_id,
    var.service_name,
  )
}

resource "google_cloud_run_service" "service" {
  name                       = "${var.resource_prefix}-service"
  location                   = var.project_region
  autogenerate_revision_name = true

  template {
    spec {
      containers {
        image = "${data.docker_registry_image.service.name}@${data.docker_registry_image.service.sha256_digest}"
      }
    }
  }

  traffic {
    latest_revision = true
    percent         = 100
  }

  depends_on = [data.docker_registry_image.service]
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

/* cloud-endpoints (terraform-google-iam/cloud_run_service_iam) */

data "google_iam_policy" "noauth" {
  binding {
    role = "roles/run.invoker"
    members = ["allUsers"]
  }
}

resource "github_actions_secret" "google_project_api_key" {
  plaintext_value = google_apikeys_key.integration.key_string
  secret_name     = "google_project_api_key"
  repository      = var.gh_repo_name
}

resource "google_cloud_run_service" "endpoint" {
  depends_on = [null_resource.openapi_proxy_image]
  name       = "${var.resource_prefix}-endpoint"
  location   = var.project_region
  project    = var.project_id
  template {
    spec {
      containers {
        image = format(
          "%s/endpoints-runtime-serverless:%s-%s-%s",
          local.endpoints_registry, var.esp_tag, var.service_name,
          google_endpoints_service.endpoints.config_id,
        )
      }
    }
  }
}

resource "google_cloud_run_service_iam_policy" "noauth-endpoints" {
  location    = google_cloud_run_service.endpoint.location
  project     = google_cloud_run_service.endpoint.project
  policy_data = data.google_iam_policy.noauth.policy_data
  service     = google_cloud_run_service.endpoint.name
}

resource "google_endpoints_service" "endpoints" {
  service_name   = var.service_name
  project        = var.project_id
  openapi_config = templatefile(var.resources.openapi-v2_yaml.path, {
    backend_service_name = google_cloud_run_service.service.status[0].url
    cloud_run_hostname   = var.service_name
  })
}

resource "google_project_service" "main" {
  service            = google_endpoints_service.endpoints.service_name
  depends_on         = [google_endpoints_service.endpoints]
  project            = var.project_id
}

locals {
  endpoints_registry = format(
    "%s-docker.pkg.dev/%s/endpoints-release",
    var.project_region,
    var.project_id
  )
}

/* gh-oidc (terraform-google-modules/github-actions-runners) */

resource "google_service_account" "main" {
  display_name = "GitHub Service Account"
  project      = var.project_id
  account_id   = "gh-oidc"
}

/* gh-actions */

resource "github_actions_secret" "google_service_account_id" {
  plaintext_value = google_service_account.main.email
  secret_name     = "google_service_account_id"
  repository      = var.gh_repo_name
}

/*resource "github_actions_secret" "google_workload_identity" {
  plaintext_value = var.google_services_passphrase
  secret_name     = "google_services_passphrase"
  repository      = var.gh_repo_name
}*/

resource "github_actions_secret" "google_workload_identity" {
  plaintext_value = module.gh-oidc.provider_name
  secret_name     = "google_workload_identity"
  repository      = var.gh_repo_name
}

/*resource "github_actions_secret" "google_workload_identity" {
  plaintext_value = var.mobile_sdk_app_id
  secret_name     = "mobile_sdk_app_id"
  repository      = var.gh_repo_name
}*/

resource "google_apikeys_key" "integration" {
  display_name = "Integration key (managed by Terraform)"
  depends_on   = [google_project_service.apikeys]
  project      = var.project_id
  name         = "integration"

  restrictions {
    api_targets {
      // methods = ["accounts.signInWithCustomToken"]
      service = "identitytoolkit.googleapis.com"
    }
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

resource "google_project_service" "apikeys" {
  service = "apikeys.googleapis.com"
  project = var.project_id
}

/* cloud-run */

resource "google_project_service" "identitytoolkit" {
  service            = "identitytoolkit.googleapis.com"
  project            = var.project_id
}

/* gradle-cache (terraform-google-modules/simple_bucket) */

resource "google_storage_bucket" "cache" {
  name                        = "playground-build-cache"
  location                    = var.project_region
  public_access_prevention    = "enforced"
  uniform_bucket_level_access = true
}

resource "google_storage_bucket_iam_member" "cache" {
  member = google_service_account.main.member
  bucket = google_storage_bucket.cache.name
  role   = "roles/storage.admin"
}
