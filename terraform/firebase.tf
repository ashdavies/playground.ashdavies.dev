resource "google_firebase_android_app" "android_release" {
  provider     = google-beta
  project      = var.project_id
  display_name = "Android Release"
  package_name = "dev.ashdavies.playground"
  sha1_hashes = [
    "9ae708c691c74827422b33586cdc4d11535c3595",
    "e7cd022a23e47b3d09940af0cd1f85d0928d1abd"
  ]
  sha256_hashes = [
    "9e271f4a830ca768f32575191aee616c25e6c5724485cb38e9908212e7915ef9",
    "c5ecb9fa2a121660be5d8d7953c81338ef8db15b3616f241f5c6fa005da0387a"
  ]
}

resource "google_firebase_hosting_site" "main" {
  provider = google-beta
  project  = "playground-1a136"
  site_id  = "playground-1a136"
  app_id   = google_firebase_web_app.browser.app_id
}

resource "google_firebase_web_app" "browser" {
  provider     = google-beta
  project      = var.project_id
  display_name = "Browser"
}

resource "google_firebase_web_app" "desktop" {
  provider     = google-beta
  project      = var.project_id
  display_name = "Desktop"
}

data "google_firebase_android_app_config" "android_release" {
  provider = google-beta
  app_id   = google_firebase_android_app.android_release.app_id
}
