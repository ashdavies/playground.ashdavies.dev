resource "google_firebase_android_app" "android_release" {
  provider      = google-beta
  project       = var.project_id
  display_name  = "Android Release"
  package_name  = "dev.ashdavies.playground"
  sha256_hashes = [
    "9e271f4a830ca768f32575191aee616c25e6c5724485cb38e9908212e7915ef9",
    "c5ecb9fa2a121660be5d8d7953c81338ef8db15b3616f241f5c6fa005da0387a"
  ]
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
