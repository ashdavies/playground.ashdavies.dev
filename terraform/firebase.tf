resource "google_firebase_android_app" "android_release" {
  provider      = google-beta
  project       = var.project_id
  display_name  = "Android Release"
  package_name  = "dev.ashdavies.playground"
  sha1_hashes   = ["a75f572e40a926bb1708178c134a1e86faadaa09"]
  sha256_hashes = ["c5ecb9fa2a121660be5d8d7953c81338ef8db15b3616f241f5c6fa005da0387a"]
}

resource "google_firebase_web_app" "browser" {
  provider      = google-beta
  project       = var.project_id
  display_name  = "Browser"
}

data "google_firebase_android_app_config" "android_release" {
  provider = google-beta
  app_id   = google_firebase_android_app.android_release.app_id
}
