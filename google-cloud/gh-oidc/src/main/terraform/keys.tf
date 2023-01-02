resource "google_apikeys_key" "integration" {
  display_name = "Integration key (managed by Terraform)"
  depends_on   = [google_project_service.apikeys]
  project      = var.project_id
  name         = "integration"

  restrictions {
    api_targets {
      service = "identitytoolkit.googleapis.com"
      //methods = ["accounts:signInWithCustomToken"]
    }
  }
}
