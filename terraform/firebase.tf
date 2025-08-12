resource "google_firebase_android_app" "release" {
  provider      = google-beta
  project       = var.project_id
  display_name  = "Playground Release"
  package_name  = "dev.ashdavies.playground"
  sha1_hashes   = ["a75f572e40a926bb1708178c134a1e86faadaa09"]
  sha256_hashes = ["0442b951b2d82cb339a7e4ef9469e71e89b182c7be5efb119f587cc2b19995bc"]
}

data "google_firebase_android_app_config" "release" {
  provider = google-beta
  app_id   = google_firebase_android_app.release.app_id
}
