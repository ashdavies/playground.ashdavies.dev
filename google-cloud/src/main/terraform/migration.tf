moved {
  from = module.gh-oidc
  to   = module.github-workload-identity
}

moved {
  to   = module.github-api-key.github_actions_secret.main
  from = github_actions_secret.google_project_api_key
}

moved {
  to   = module.github-api-key.google_apikeys_key.main
  from = google_apikeys_key.integration
}

moved {
  to   = module.github-api-key.google_project_service.apikeys
  from = google_project_service.apikeys
}

moved {
  to   = module.github-api-key.google_project_service.target["identitytoolkit.googleapis.com"]
  from = google_project_service.identitytoolkit
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

moved {
  to   = module.cloud-run-build.google_cloud_run_service.main
  from = google_cloud_run_service.service
}
