#!/usr/bin/env bash
set -euo pipefail

# Default values
GITHUB_TOKEN="${GITHUB_TOKEN:-}"
COMMIT_MSG="${COMMIT_MSG:-}"
GIT_NAME=""
GIT_EMAIL=""

show_help() {
  echo "Usage: $0 [-t GITHUB_TOKEN] [-m COMMIT_MSG]"
  echo "  -t  GitHub Auth Token (can be set in GITHUB_TOKEN env)"
  echo "  -m  Commit message (can be set in COMMIT_MSG env)"
  exit 1
}

# Parse CLI args
while getopts "t:m:h" opt; do
  case $opt in
    t) GITHUB_TOKEN="$OPTARG" ;;
    m) COMMIT_MSG="$OPTARG" ;;
    h|*) show_help ;;
  esac
done

if [[ -z "${GITHUB_TOKEN}" ]]; then
  echo "Error: GitHub token must be provided via -t or GITHUB_TOKEN env var." >&2
  exit 2
fi

if [[ -z "${COMMIT_MSG}" ]]; then
  echo "Error: Commit message must be provided via -m or COMMIT_MSG env var." >&2
  exit 2
fi

# Ensure required tools are available
if ! command -v jq &>/dev/null; then
  echo "Error: 'jq' is required but not installed." >&2
  exit 3
fi

if ! command -v curl &>/dev/null; then
  echo "Error: 'curl' is required but not installed." >&2
  exit 3
fi

# When using a GitHub App installation token, fetch details via the installation API
INSTALLATION_JSON=$(curl -s -H "Authorization: Bearer ${GITHUB_TOKEN}" -H "Accept: application/vnd.github+json" https://api.github.com/app/installations)
INSTALL_ID=$(echo "$INSTALLATION_JSON" | jq -r '.[0].id // empty')
if [[ -z "$INSTALL_ID" ]]; then
  echo "Error: Unable to get installation id for the app token" >&2
  exit 4
fi

# Get details for the installation
INSTALL_DETAILS=$(curl -s -H "Authorization: Bearer ${GITHUB_TOKEN}" -H "Accept: application/vnd.github+json" "https://api.github.com/app/installations/${INSTALL_ID}")
ACCOUNT_TYPE=$(echo "$INSTALL_DETAILS" | jq -r '.account.type // empty')
ACCOUNT_LOGIN=$(echo "$INSTALL_DETAILS" | jq -r '.account.login // empty')
ACCOUNT_ID=$(echo "$INSTALL_DETAILS" | jq -r '.account.id // empty')

if [[ "$ACCOUNT_TYPE" == "User" ]]; then
  GIT_NAME="$ACCOUNT_LOGIN"
  GIT_EMAIL="${ACCOUNT_ID}+${ACCOUNT_LOGIN}@users.noreply.github.com"
elif [[ "$ACCOUNT_TYPE" == "Organization" ]]; then
  GIT_NAME="$ACCOUNT_LOGIN"
  GIT_EMAIL="${ACCOUNT_LOGIN}@users.noreply.github.com"
else
  echo "Error: Unable to determine account type from installation details." >&2
  exit 5
fi

if [[ -z "$GIT_NAME" || -z "$GIT_EMAIL" ]]; then
  echo "Error: Unable to determine name or email from installation details." >&2
  exit 5
fi

# Configure git
git config user.name "$GIT_NAME"
git config user.email "$GIT_EMAIL"

# If we are on the default branch, create and switch to a new branch before committing
# Determine the default branch from origin/HEAD, fallback to remote show
DEFAULT_BRANCH="$(git symbolic-ref refs/remotes/origin/HEAD 2>/dev/null | sed -e 's@^refs/remotes/origin/@@')"
if [[ -z "$DEFAULT_BRANCH" ]]; then
  DEFAULT_BRANCH="$(git remote show origin 2>/dev/null | sed -n '/HEAD branch/s/.*: //p')"
fi

CURRENT_BRANCH="$(git rev-parse --abbrev-ref HEAD 2>/dev/null || echo "")"
if [[ -z "$CURRENT_BRANCH" || "$CURRENT_BRANCH" == "HEAD" ]]; then
  CURRENT_BRANCH="$DEFAULT_BRANCH"
fi

if [[ -n "$DEFAULT_BRANCH" && "$CURRENT_BRANCH" == "$DEFAULT_BRANCH" ]]; then
  SAFE_BRANCH_NAME="auto/pr-$(date +%Y%m%d%H%M%S)"
  if [[ -n "$ACCOUNT_LOGIN" ]]; then
    SAFE_BRANCH_NAME+="-$ACCOUNT_LOGIN"
  fi
  echo "On default branch '$DEFAULT_BRANCH'. Creating and switching to new branch '$SAFE_BRANCH_NAME' before commit."
  git checkout -b "$SAFE_BRANCH_NAME"
fi

# Stage and commit changes
if git diff --cached --quiet && git diff --quiet; then
  echo "No changes to commit."
  exit 0
else
  git commit -am "$COMMIT_MSG"
fi

BRANCH=$(git rev-parse --abbrev-ref HEAD)
echo "Current branch: $BRANCH"

# Push branch
git push origin "$BRANCH"

# Create PR using GitHub CLI
if ! command -v gh &>/dev/null; then
  echo "Error: GitHub CLI (gh) is required to create a pull request." >&2
  exit 6
fi

# Authenticate gh with token
export GH_TOKEN="${GITHUB_TOKEN}"

# Check if PR already exists
if gh pr view "$BRANCH" &>/dev/null; then
  echo "PR for branch '$BRANCH' already exists."
else
  gh pr create --fill --label Automated
  echo "Pull request created for branch '$BRANCH'."
fi
