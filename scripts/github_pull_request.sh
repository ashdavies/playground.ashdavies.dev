#!/usr/bin/env bash

set -eo pipefail

show_help() {
  cat << EOF
Usage: $(basename "$0") --base-branch <BASE_BRANCH> --commit-msg <COMMIT_MSG>

Automates the workflow of creating a branch, committing staged and unstaged changes, pushing to GitHub, and creating a PR.

Arguments:
  --base-branch    The branch into which you want your code merged (required)
  --commit-msg     Git commit message for the automatic commit (required)
  --help           Show this help message

Environment:
  GITHUB_TOKEN     GitHub token with repo permissions (required)
EOF
}

# Parse arguments
while [[ $# -gt 0 ]]; do
  case "$1" in
    --base-branch)
      BASE_BRANCH="$2"
      shift 2
      ;;
    --commit-msg)
      COMMIT_MSG="$2"
      shift 2
      ;;
    --help)
      show_help
      exit 0
      ;;
    *)
      echo "Unknown argument: $1" >&2
      exit 1
      ;;
  esac
done

# Validate arguments
[[ -z "${GITHUB_TOKEN:-}" ]] && echo "GITHUB_TOKEN environment variable is required." >&2 && exit 2
[[ -z "$BASE_BRANCH" ]] && echo "--base-branch is required." >&2 && exit 2
[[ -z "$COMMIT_MSG" ]] && echo "--commit-msg is required." >&2 && exit 2

# Verify installed commands
for cmd in gh uuidgen git; do
  command -v "$cmd" >/dev/null 2>&1 || (echo "Required command '$cmd' not found." >&2 && exit 3)
done

# Add all untracked files
git add --all

# Git state check
if git diff --cached --quiet && git diff --quiet; then
  echo "No changes to commit. Exiting." >&2
  exit 0
fi

# Define repository and branch info
GIT_REPO="$(gh repo view --json nameWithOwner -q .nameWithOwner | tee /dev/stderr)"
BRANCH_NAME="auto/$(uuidgen)"; echo "Branch Name: $BRANCH_NAME" >&2 # TODO Deterministic branch name

# Determine base branch (e.g. main/master)
BASE_SHA="$(gh api "repos/$GIT_REPO/git/ref/heads/$BASE_BRANCH" --jq .object.sha | tee /dev/stderr)"

# Create anonymous git credentials
git config user.name 'Anonymous'
git config user.email '<>'

# Commit to target branch
git checkout -b "$BRANCH_NAME"
git commit -m "$COMMIT_MSG"
git push origin "$BRANCH_NAME"

# Get local tree hash
TREE_SHA="$(git log --format=%T | head -n1)"

# Create verified commit as your app with large files!
NEW_COMMIT_SHA=$(
  gh api "repos/$GIT_REPO/git/commits" \
    -f "message=$COMMIT_MSG" \
    -f "tree=$TREE_SHA" \
    -f "parents[]=$BASE_SHA" \
    --jq .sha \
    || (echo "Failed to create commit" >&2 && exit 4)
)

# Update branch reference
gh api "repos/$GIT_REPO/git/refs/heads/$BRANCH_NAME" \
  -f "sha=$NEW_COMMIT_SHA" \
  -f "force=true" \
  || (echo "Failed to update branch reference" >&2 && exit 5)

# Determine PR url
PR_URL="$(gh pr view "$BRANCH_NAME" --json url -q .url 2>/dev/null || true)"

# Create or update PR
if [[ -n "$PR_URL" ]]; then
  echo "Pull request already exists: $PR_URL" >&2
else
  echo "Creating new pull request..." >&2

  gh pr create \
    --label "Automated" \
    --base "$BASE_BRANCH" \
    --head "$BRANCH_NAME" \
    --fill \
    | tee /dev/stderr ||
    (echo "Failed to create pull request." >&2 && exit 6)

  echo "Pull request created successfully." >&2
fi

echo "✅ Workflow completed successfully." >&2
