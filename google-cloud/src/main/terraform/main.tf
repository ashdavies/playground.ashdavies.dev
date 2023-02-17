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

/* gh-actions */

resource "github_actions_secret" "google_service_account_id" {
  plaintext_value = module.github-service-account.email
  secret_name     = "google_service_account_id"
  repository      = var.gh_repo_name
}

/*resource "github_actions_secret" "google_services_passphrase" {
  plaintext_value = var.google_services_passphrase
  secret_name     = "google_services_passphrase"
  repository      = var.gh_repo_name
}*/

resource "github_actions_secret" "google_workload_identity" {
  plaintext_value = module.github-workload-identity.provider_name
  secret_name     = "google_workload_identity"
  repository      = var.gh_repo_name
}

/*resource "github_actions_secret" "mobile_sdk_app_id" {
  plaintext_value = var.mobile_sdk_app_id
  secret_name     = "mobile_sdk_app_id"
  repository      = var.gh_repo_name
}*/
