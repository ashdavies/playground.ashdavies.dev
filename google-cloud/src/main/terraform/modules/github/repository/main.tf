resource "github_actions_secret" "main" {
  for_each        = {
    for secret in var.secrets: secret.secret_name => secret
  }

  repository      = github_repository.main.name
  plaintext_value = each.value.plaintext_value
  secret_name     = each.value.secret_name
}

resource "github_repository" "main" {
  description            = var.description
  name                   = var.repository
  allow_merge_commit     = false
  allow_rebase_merge     = false
  delete_branch_on_merge = true
  allow_auto_merge       = true
  has_issues             = true
  topics                 = [
    "android-development",
    "multiplatform",
    "development",
    "composer",
    "android",
    "kotlin",
  ]
}

# github_branch.main is deprecated
resource "github_branch" "main" {
  branch     = github_repository.main.default_branch
  repository = github_repository.main.name
}

resource "github_issue_label" "main" {
  for_each    = {
    for label in var.labels : label.name => label
  }

  repository  = github_repository.main.name
  description = each.value.description
  color       = each.value.color
  name        = each.value.name
}
