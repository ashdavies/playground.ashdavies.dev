#!/usr/bin/env bash

set -eo pipefail

show_help() {
  cat << EOF
Usage: $(basename "$0") --tag-name <TAG_NAME> --files <FILES>

Creates a GitHub release with the provided target commit

Arguments:
  --tag-name       The name of the tag (required)
  --files          Files glob pattern
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
GIT_REPO="$(gh repo view --json nameWithOwner --jq .nameWithOwner | tee /dev/stderr)"

# Create (draft) release and capture upload URL template
UPLOAD_URL="$(gh api "/repos/${GIT_REPO}/releases" \
  --field "target_commitish=${GITHUB_REF##*/}" \
  --field "tag_name=${TAG_NAME}" \
  --raw-field "generate_release_notes=true" \
  --raw-field "draft=true" \
  --jq .upload_url)"

echo "Created draft release '${TAG_NAME}' for ${GIT_REPO}" >&2

# Upload assets matching the provided glob pattern
if [[ -n "${FILES:-}" ]]; then
  for file in ${FILES}; do
    if [[ -f "$file" ]]; then
      gh api "${UPLOAD_URL%\{*}?name=$(basename "$file")" --input "$file"
      echo "Uploaded $file to release ${TAG_NAME}" >&2
    fi
  done
fi

