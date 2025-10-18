#!/usr/bin/env bash

set -eo pipefail

show_help() {
  cat << EOF
Usage: $(basename "$0") --app-id <APP_ID> --jwt <JWT>

Generate a GitHub App installation access token using the provided App ID and JWT.
The script will use the current repository context to find the correct installation.

Arguments:
  --app-id         The App ID of the GitHub App.
  --jwt            The signed JWT for the GitHub App.
  --help           Show this help message.
EOF
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --app-id)
      APP_ID="$2"
      shift 2
      ;;
    --jwt)
      JWT="$2"
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

# Check required arguments
if [[ -z "$APP_ID" || -z "$JWT" ]]; then
  echo "Error: --app-id and --jwt are required." >&2
  show_help
  exit 1
fi

# Check for dependencies
for cmd in gh jq; do
  if ! command -v "$cmd" >/dev/null 2>&1; then
    echo "Error: Required command '$cmd' not found." >&2
    exit 2
  fi
done

# Get current repo (owner/repo)
REPO="$(gh repo view --json nameWithOwner -q .nameWithOwner)"
if [[ -z "$REPO" ]]; then
  echo "Error: Could not determine current repository (need to run within a cloned repo or GitHub Actions)." >&2
  exit 3
fi

# Get installation ID for this app in this repository using GitHub CLI
INSTALLATION_ID="$(gh api "repos/$REPO/installation" --jq '.id' --header "Authorization: Bearer $JWT")"

if [[ "$INSTALLATION_ID" == "null" || -z "$INSTALLATION_ID" ]]; then
  echo "Error: App with ID $APP_ID is not installed in $REPO" >&2
  exit 5
fi

# Request installation token using GitHub CLI
TOKEN_RESPONSE="$(gh api "app/installations/$INSTALLATION_ID/access_tokens" \
  --method POST \
  --header "Authorization: Bearer $JWT" \
  --header "Accept: application/vnd.github+json"
)"

INSTALLATION_TOKEN="$(echo "$TOKEN_RESPONSE" | jq -r .token)"
if [[ "$INSTALLATION_TOKEN" == "null" || -z "$INSTALLATION_TOKEN" ]]; then
  echo "Error: Failed to generate installation token." >&2
  echo "$TOKEN_RESPONSE" >&2
  exit 6
fi

echo "$INSTALLATION_TOKEN"
