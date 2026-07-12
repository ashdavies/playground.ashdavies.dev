resource "google_firebase_android_app" "android_release" {
  provider      = google-beta
  project       = var.project_id
  display_name  = "Android Release"
  package_name  = "dev.ashdavies.playground"
  sha1_hashes   = ["a75f572e40a926bb1708178c134a1e86faadaa09"]
  sha256_hashes = ["0442b951b2d82cb339a7e4ef9469e71e89b182c7be5efb119f587cc2b19995bc"]
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

moved {
  from = google_firebase_android_app.release
  to   = google_firebase_android_app.android_release
}
