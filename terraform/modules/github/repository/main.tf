resource "github_repository" "main" {
  description            = var.description
  name                   = var.repository
  allow_merge_commit     = false
  allow_rebase_merge     = false
  delete_branch_on_merge = true
  allow_auto_merge       = true
  has_issues             = true
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
