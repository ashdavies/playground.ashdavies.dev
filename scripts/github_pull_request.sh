#!/usr/bin/env bash

set -eo pipefail

#---------------------------------------
# Helper functions
#---------------------------------------

show_help() {
  cat << EOF
Usage: $(basename "$0") --commit-msg <COMMIT_MSG>

Automates the workflow of creating a branch, committing staged and unstaged changes, pushing to GitHub, and creating a PR.

Arguments:
  --commit-msg     Git commit message for the automatic commit
  --help           Show this help message

Environment:
  GITHUB_TOKEN     GitHub token with repo permissions (required)
EOF
}

log() {
  echo "[$(date '+%Y-%m-%d %H:%M:%S')] $*" >&2
}

error_exit() {
  log "ERROR: $*"
  exit 1
}

#---------------------------------------
# Argument parsing
#---------------------------------------

while [[ $# -gt 0 ]]; do
  case "$1" in
    --commit-msg)
      COMMIT_MSG="$2"
      shift 2
      ;;
    --help)
      show_help
      exit 0
      ;;
    *)
      error_exit "Unknown argument: $1"
      ;;
  esac
done

#---------------------------------------
# Validation
#---------------------------------------

[[ -z "${GITHUB_TOKEN:-}" ]] && error_exit "GITHUB_TOKEN environment variable is required."
[[ -z "$COMMIT_MSG" ]] && error_exit "--commit-msg is required."

for cmd in gh jq uuidgen git; do
  command -v "$cmd" >/dev/null 2>&1 || error_exit "Required command '$cmd' not found."
done

#---------------------------------------
# Git state checks
#---------------------------------------

if git diff --cached --quiet && git diff --quiet; then
  log "No changes to commit. Exiting."
  exit 0
fi

#---------------------------------------
# Define repository and branch info
#---------------------------------------

GIT_REPO="$(gh repo view --json nameWithOwner -q .nameWithOwner)"
BRANCH_NAME="auto/$(uuidgen)"

# Determine base branch (e.g. main/master)
BASE_BRANCH="$(gh repo view --json defaultBranchRef --jq .defaultBranchRef.name)"
BASE_SHA="$(gh api "repos/$GIT_REPO/git/ref/heads/$BASE_BRANCH" --jq .object.sha)"

log "Repository: $GIT_REPO"
log "Base branch: $BASE_BRANCH"
log "Base SHA: $BASE_SHA"
log "Creating new branch: $BRANCH_NAME"

#---------------------------------------
# Create new branch reference
#---------------------------------------

jq -n --arg ref "refs/heads/$BRANCH_NAME" --arg sha "$BASE_SHA" '{ref: $ref, sha: $sha}' |
  gh api "repos/$GIT_REPO/git/refs" --input - >/dev/null ||
  error_exit "Failed to create branch reference."

#---------------------------------------
# Create blobs for changed files
#---------------------------------------

declare -A BLOBS

while IFS= read -r FILE; do
  log "Creating blob for: $FILE"
  SHA=$(jq -n --arg content "$(base64 "$FILE" | tr -d '\n')" --arg encoding "base64" \
    '{content: $content, encoding: $encoding}' |
    gh api "repos/$GIT_REPO/git/blobs" --input - --jq .sha) ||
    error_exit "Failed to create blob for $FILE."
  BLOBS["$FILE"]="$SHA"
done < <(git diff --cached --name-only)

#---------------------------------------
# Build tree entries from blobs
#---------------------------------------

TREE_ENTRIES=$(jq -n --argjson tree "$(for FILE in "${!BLOBS[@]}"; do
  printf '{"path": "%s", "mode": "100644", "type": "blob", "sha": "%s"}\n' "$FILE" "${BLOBS[$FILE]}"
done | jq -s .)" '$tree')

#---------------------------------------
# Create new tree from base
#---------------------------------------

BASE_TREE_SHA="$(gh api "repos/$GIT_REPO/git/commits/$BASE_SHA" --jq .tree.sha)"
NEW_TREE_SHA="$(jq -n --arg base "$BASE_TREE_SHA" --argjson tree "$TREE_ENTRIES" \
  '{base_tree: $base, tree: $tree}' |
  gh api "repos/$GIT_REPO/git/trees" --input - --jq .sha)" ||
  error_exit "Failed to create new tree."

#---------------------------------------
# Create new commit
#---------------------------------------

NEW_COMMIT_SHA="$(jq -n --arg msg "$COMMIT_MSG" --arg tree "$NEW_TREE_SHA" --arg parent "$BASE_SHA" \
  '{message: $msg, tree: $tree, parents: [$parent]}' |
  gh api "repos/$GIT_REPO/git/commits" --input - --jq .sha)" ||
  error_exit "Failed to create commit."

#---------------------------------------
# Update branch reference
#---------------------------------------

jq -n --arg sha "$NEW_COMMIT_SHA" '{sha: $sha, force: true}' |
  gh api "repos/$GIT_REPO/git/refs/heads/$BRANCH_NAME" --method PATCH --input - >/dev/null ||
  error_exit "Failed to update branch reference."

#---------------------------------------
# Create or update PR
#---------------------------------------

PR_URL="$(gh pr view "$BRANCH_NAME" --json url -q .url 2>/dev/null || true)"

if [[ -n "$PR_URL" ]]; then
  log "Pull request already exists: $PR_URL"
else
  log "Creating new pull request..."
  gh pr create \
    --body "Automated PR for commit: $COMMIT_MSG" \
    --label "Automated" \
    --title "$COMMIT_MSG" \
    --base "$BASE_BRANCH" \
    --head "$BRANCH_NAME" |
    tee /tmp/pr_output.log ||
    error_exit "Failed to create pull request."

  log "Pull request created successfully."
fi

log "✅ Workflow completed successfully."
