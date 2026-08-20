resource "github_repository" "main" {
  name        = var.gh_repo_name
  description = "Playground"

  has_issues             = true
  has_projects           = false
  allow_merge_commit     = false
  allow_rebase_merge     = false
  allow_auto_merge       = true
  delete_branch_on_merge = true

  topics = [
    "compose-multiplatform",
    "kotlin-multiplatform",
  ]

  lifecycle {
    prevent_destroy = true
  }
}

resource "github_issue_label" "automated" {
  repository = var.gh_repo_name
  name       = "Automated"
  color      = "2198D8"
}

resource "github_issue_label" "dry_run" {
  repository = var.gh_repo_name
  name       = "Dry Run"
  color      = "55F756"
}

resource "github_issue_label" "feature" {
  repository = var.gh_repo_name
  name       = "Feature"
  color      = "14a88d"
}

resource "github_issue_label" "terraform_apply" {
  repository = var.gh_repo_name
  name       = "Terraform Apply"
  color      = "8123fc"
}
