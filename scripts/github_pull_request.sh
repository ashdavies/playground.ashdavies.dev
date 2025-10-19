#!/usr/bin/env bash

set -eo pipefail

show_help() {
  cat << EOF
Usage: $(basename "$0") --app-token <APP_TOKEN> --commit-msg <COMMIT_MSG>

Automates the workflow of creating a branch, committing staged and unstaged changes, pushing to GitHub, and creating a PR.

Arguments:
  --app-token      JWT for the GitHub App, signed using the RS256 algorithm.
  --github-token   GitHub token, requires permission to create pull requests.
  --commit-msg     Git commit message for the automatic commit.
  --help           Show this help message.
EOF
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --app-token)
      APP_TOKEN="$2"
      shift 2
      ;;
    --github-token)
      GITHUB_TOKEN="$2"
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
      show_help
      exit 1
      ;;
  esac
done

if [[ -z "$APP_TOKEN" || -z "$COMMIT_MSG" ]]; then
  echo "Error: --app-token and --commit-msg are required." >&2
  show_help
  exit 1
fi

for cmd in gh jq uuidgen; do
  if ! command -v "$cmd" >/dev/null 2>&1; then
    echo "Error: Required command '$cmd' not found." >&2
    exit 2
  fi
done

if git diff --cached --quiet && git diff --quiet; then
  echo "No changes to commit."
  exit 0
fi

GITHUB_APP_SLUG=$(gh api app --header "Authorization: Bearer $APP_TOKEN" --jq ".slug")
GIT_NAME="${GITHUB_APP_SLUG}[bot]"

GITHUB_APP_USER_ID=$(gh api "users/${GIT_NAME}" --jq ".id")
GIT_EMAIL="${GITHUB_APP_USER_ID}+${GIT_NAME}@users.noreply.github.com"
GIT_BRANCH=$(git rev-parse --abbrev-ref HEAD)

if [[ -z "$GIT_NAME" || -z "$GIT_EMAIL" ]]; then
  echo "Error: Unable to determine GitHub user name or email from token" >&2
  exit 3
fi

git config user.name "$GIT_NAME"
git config user.email "$GIT_EMAIL"

git checkout -b "auto/$(uuidgen)"
git commit -am "$COMMIT_MSG"

git push --set-upstream origin "$GIT_BRANCH"

GITHUB_PR_URL=$(gh pr view "$GIT_BRANCH" --json url -q .url 2>/dev/null)

if [[ -n "$GITHUB_PR_URL" ]]; then
  echo "Pull request updated: $GITHUB_PR_URL"
else
  gh pr create --fill --label "Automated"
fi
