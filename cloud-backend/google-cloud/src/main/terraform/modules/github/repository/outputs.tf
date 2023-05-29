output "sha" {
  description = "A string storing the reference's HEAD commit's SHA1."
  value       = github_branch.main.sha
}
