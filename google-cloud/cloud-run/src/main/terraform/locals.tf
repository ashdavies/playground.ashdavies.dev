locals {
  cloud_run_artifact = "${var.project_region}-docker.pkg.dev/${var.project_id}/cloud-run-source-deploy/${var.cloud_run_service}"
  endpoints_path = "${var.cloud_run_service}.endpoints.${var.project_id}.cloud.goog"
}
