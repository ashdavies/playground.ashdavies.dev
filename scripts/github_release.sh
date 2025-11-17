#!/usr/bin/env bash

set -eo pipefail

show_help() {
  cat << EOF
Usage: $(basename "$0") --tag-name <TAG_NAME> [--files <FILES>]

Creates a GitHub release with the provided tag name. Optionally uploads files matching the provided glob pattern.

Arguments:
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
[[ -z "${TAG_NAME}" ]] && echo "--tag-name is required." >&2 && exit 2
[[ -z "${GITHUB_TOKEN:-}" ]] && echo "GITHUB_TOKEN environment variable is required." >&2 && exit 2

# Verify installed commands
for cmd in gh git; do
  command -v "$cmd" >/dev/null 2>&1 || { echo "Required command '$cmd' not found." >&2; exit 3; }
done

# Define repository and branch info
GIT_REPO="$(gh repo view --json nameWithOwner --jq .nameWithOwner)"

# Get target commitish (use GITHUB_REF if available, otherwise use current branch)
if [[ -n "${GITHUB_REF:-}" ]]; then
  TARGET_COMMITISH="${GITHUB_REF##*/}"
else
  TARGET_COMMITISH="$(git rev-parse --abbrev-ref HEAD)"
fi

# Create (draft) release and capture upload URL template
UPLOAD_URL="$(gh api "/repos/${GIT_REPO}/releases" \
  --field "target_commitish=${TARGET_COMMITISH}" \
  --field "tag_name=${TAG_NAME}" \
  --raw-field "generate_release_notes=true" \
  --raw-field "draft=true" \
  --jq .upload_url)"

echo "Created draft release '${TAG_NAME}' for ${GIT_REPO}" >&2

# Upload assets matching the provided glob pattern
if [[ -n "${FILES:-}" ]]; then
  # Enable extended pattern matching
  shopt -s extglob nullglob
  
  # Expand the glob pattern into an array
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
      
      gh api "${UPLOAD_URL%\{*}?name=$(basename "$file")" \
        --method POST \
        --header "Content-Type: ${CONTENT_TYPE}" \
        --input "$file"
      echo "Uploaded $file to release ${TAG_NAME}" >&2
    fi
  done
fi

