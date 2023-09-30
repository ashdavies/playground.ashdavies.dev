resource "github_actions_secret" "main" {
  for_each        = {
    for secret in var.secrets: secret.secret_name => secret
  }

  repository      = github_repository.main.name
  plaintext_value = each.value.plaintext_value
  secret_name     = each.value.secret_name
}

resource "github_repository" "main" {
  name                   = var.repository
  description            = var.description
  homepage_url           = var.homepage_url
  has_issues             = true
  has_discussions        = false
  has_wiki               = false
  allow_merge_commit     = false
  allow_rebase_merge     = false
  allow_auto_merge       = true
  delete_branch_on_merge = true
  topics                 = var.topics
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
