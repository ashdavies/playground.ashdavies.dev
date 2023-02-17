moved {
  to   = module.gradle-build-cache.google_storage_bucket.bucket
  from = google_storage_bucket.cache
}

moved {
  to   = module.gradle-build-cache.google_storage_bucket_iam_member.members["roles/storage.admin serviceAccount:gh-oidc@playground-1a136.iam.gserviceaccount.com"]
  from = google_storage_bucket_iam_member.cache
}
