moved {
  from = module.gh-oidc
  to   = module.github-workload-identity
}

moved {
  to   = module.github-service-account.google_service_account.service_accounts["oidc"]
  from = google_service_account.main
}

moved {
  to   = module.github-service-account.google_project_iam_member.project-roles["oidc-projects/playground-1a136/roles/actionsPublisher"]
  from = google_project_iam_member.main
}

moved {
  to   = module.github-service-account.google_project_iam_member.project-roles["oidc-roles/viewer"]
  from = google_project_iam_member.viewer
}

moved {
  to   = module.gradle-build-cache.google_storage_bucket.bucket
  from = google_storage_bucket.cache
}

moved {
  to   = module.gradle-build-cache.google_storage_bucket_iam_member.members["roles/storage.admin serviceAccount:gh-oidc@playground-1a136.iam.gserviceaccount.com"]
  from = google_storage_bucket_iam_member.cache
}
