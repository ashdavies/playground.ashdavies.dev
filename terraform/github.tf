resource "github_repository" "main" {
  name                   = var.gh_repo_name
  description            = "Playground"

  has_projects           = false
  allow_merge_commit     = false
  allow_rebase_merge     = false
  allow_auto_merge       = true
  delete_branch_on_merge = true

  topics                 = [
    "compose-multiplatform",
    "kotlin-multiplatform",
  ]
}

resource "github_issue_labels" "main" {
  repository  = var.gh_repo_name

  label {
    name        = "Terraform"
    color       = "e99695"
    description = "Execute terraform workflow"
  }
}
