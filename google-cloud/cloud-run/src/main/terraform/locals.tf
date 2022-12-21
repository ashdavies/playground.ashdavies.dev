locals {
  cloud_run_artifact = "${var.project_region}-docker.pkg.dev/${var.project_id}/cloud-run-source-deploy/${var.service_name}"
  endpoints_path = "${var.service_name}.endpoints.${var.project_id}.cloud.goog"
}
