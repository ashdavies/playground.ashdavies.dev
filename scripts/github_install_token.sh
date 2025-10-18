#!/usr/bin/env bash

set -eo pipefail

show_help() {
  cat << EOF
Usage: $(basename "$0") --app-token <APP_TOKEN>

Generate a GitHub App installation access token using the provided App ID and JWT token.
The script will use the current repository context to find the correct installation.

Arguments:
  --app-token      JWT for the GitHub App, signed using the RS256 algorithm.
  --help           Show this help message.
EOF
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --app-token)
      APP_TOKEN="$2"
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

if [[ -z "$APP_TOKEN" ]]; then
  echo "Error: --app-token is required." >&2
  show_help
  exit 1
fi

for cmd in gh jq; do
  if ! command -v "$cmd" >/dev/null 2>&1; then
    echo "Error: Required command '$cmd' not found." >&2
    exit 2
  fi
done

GITHUB_REPO="$(gh repo view --json nameWithOwner -q .nameWithOwner)"
if [[ -z "$GITHUB_REPO" ]]; then
  echo "Error: Could not determine current repository (need to run within a cloned repo or GitHub Actions)." >&2
  exit 3
fi

INSTALLATION_ID="$(
  gh api "repos/$GITHUB_REPO/installation" --jq '.id' \
    --header "Accept: application/vnd.github+json" \
    --header "Authorization: Bearer $APP_TOKEN")"

if [[ "$INSTALLATION_ID" == "null" || -z "$INSTALLATION_ID" ]]; then
  echo "Error: Installation not found in $GITHUB_REPO" >&2
  exit 4
fi

TOKEN_RESPONSE="$(gh api "app/installations/$INSTALLATION_ID/access_tokens" \
  --header "Accept: application/vnd.github+json" \
  --header "Authorization: Bearer $APP_TOKEN" \
  --method POST \
)"

INSTALLATION_TOKEN="$(echo "$TOKEN_RESPONSE" | jq -r .token)"
if [[ "$INSTALLATION_TOKEN" == "null" || -z "$INSTALLATION_TOKEN" ]]; then
  echo "Error: Failed to generate installation token." >&2
  echo "$TOKEN_RESPONSE" >&2
  exit 5
fi

echo "$INSTALLATION_TOKEN"
