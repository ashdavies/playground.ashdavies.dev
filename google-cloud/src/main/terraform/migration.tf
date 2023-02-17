moved {
  from = module.gh-oidc
  to   = module.github-workload-identity
}
moved {
  to   = module.gradle-build-cache.google_storage_bucket.bucket
  from = google_storage_bucket.cache
}

moved {
  to   = module.gradle-build-cache.google_storage_bucket_iam_member.members["roles/storage.admin serviceAccount:gh-oidc@playground-1a136.iam.gserviceaccount.com"]
  from = google_storage_bucket_iam_member.cache
}
