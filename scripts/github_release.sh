#!/usr/bin/env bash

set -eo pipefail

show_help() {
  cat << EOF
Usage: $(basename "$0") --target-branch <TARGET_BRANCH> --tag-name <TAG_NAME> [--files <FILES>]

Creates a GitHub release with the provided tag name. Optionally uploads files matching the provided glob pattern.

Arguments:
  --target-branch  The branch from which you want your release created (required)
  --tag-name       The name of the tag (required)
  --files          Files glob pattern (optional)
  --help           Show this help message

Environment:
  GITHUB_TOKEN     GitHub token with repo permissions (required)
EOF
}

# Parse arguments
while [[ $# -gt 0 ]]; do
  case "$1" in
    --target-branch)
      TARGET_BRANCH="$2"
      shift 2
      ;;
    --tag-name)
      TAG_NAME="$2"
      shift 2
      ;;
    --files)
      FILES="$2"
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
[[ -z "${TARGET_BRANCH}" ]] && echo "--target-branch is required." >&2 && exit 2
[[ -z "${TAG_NAME}" ]] && echo "--tag-name is required." >&2 && exit 2
[[ -z "${GITHUB_TOKEN:-}" ]] && echo "GITHUB_TOKEN environment variable is required." >&2 && exit 2

# Verify installed commands
for cmd in gh git jq; do
  command -v "$cmd" >/dev/null 2>&1 || { echo "Required command '$cmd' not found." >&2; exit 3; }
done

# Define repository and branch info
GIT_REPO="$(gh repo view --json nameWithOwner --jq .nameWithOwner)"

RELEASE_NOTES="$(gh api "/repos/${GIT_REPO}/releases/generate-notes" \
  --raw-field "tag_name=${TAG_NAME}" \
  --raw-field "target_commitish=${TARGET_BRANCH}" \
  --verbose >&2 | jq -r .body)"

# Create (draft) release and capture upload URL template and release ID
RESPONSE="$(gh api "/repos/${GIT_REPO}/releases" \
  --raw-field "tag_name=${TAG_NAME}" \
  --raw-field "target_commitish=${TARGET_BRANCH}" \
  --raw-field "body=${RELEASE_NOTES:0:125000}" \
  --field "generate_release_notes=true" \
  --field "draft=true" \
  --verbose >&2)"

UPLOAD_URL="$(echo "$RESPONSE" | jq -r .upload_url)"
RELEASE_ID="$(echo "$RESPONSE" | jq -r .id)"

echo "Created draft release '${TAG_NAME}' (ID: ${RELEASE_ID}) for ${GIT_REPO}" >&2

# Upload assets matching the provided glob pattern
if [[ -n "${FILES:-}" ]]; then
  # Enable extended pattern matching
  shopt -s extglob nullglob
  # shellcheck disable=SC2206
  file_list=(${FILES})
  
  for file in "${file_list[@]}"; do
    if [[ -f "$file" ]]; then
      # Determine content type
      case "${file##*.}" in
        apk) CONTENT_TYPE="application/vnd.android.package-archive" ;;
        aab) CONTENT_TYPE="application/octet-stream" ;;
        *)   CONTENT_TYPE=$(file --mime-type -b "$file" 2>/dev/null || echo "application/octet-stream") ;;
      esac
      if ! gh api "${UPLOAD_URL%\{*}?name=$(basename "$file")" \
        --method POST \
        --header "Content-Type: ${CONTENT_TYPE}" \
        --input "$file"; then
        echo "Failed to upload $file" >&2
        exit 1
      fi
      echo "Uploaded $file to release ${TAG_NAME}" >&2
    fi
  done
fi

