resource "github_repository" "main" {
  description            = var.description
  name                   = var.repository
  allow_merge_commit     = false
  allow_rebase_merge     = false
  delete_branch_on_merge = true
  allow_auto_merge       = true
  topics                 = [
    "android-development",
    "multiplatform",
    "development",
    "composer",
    "android",
    "kotlin",
  ]
}

resource "github_branch" "main" {
  branch     = github_repository.main.default_branch
  repository = github_repository.main.name
}
