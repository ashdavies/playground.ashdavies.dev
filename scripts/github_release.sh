#!/usr/bin/env bash

set -eou pipefail

show_help() {
  cat << EOF
Usage: $(basename "$0") --target-branch <TARGET_BRANCH> --tag-name <TAG_NAME> [--files <FILES>]

Creates a GitHub release with the provided tag name. Optionally uploads files matching the provided glob pattern.

Arguments:
  --target-branch  The branch from which you want your release created (required)
  --tag-name       The name of the tag (required)
  --files          Files glob pattern(s), can be space-separated or repeated (optional)
  --help           Show this help message

Environment:
  GITHUB_TOKEN     GitHub token with repo permissions (required)
EOF
};

TARGET_BRANCH=
TAG_NAME=
FILES=()

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
      shift

      while [[ $# -gt 0 && ! "$1" =~ ^-- ]]; do
        FILES+=("$1")
        shift
      done
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

LATEST_TAG_NAME="$(gh api "repos/${GIT_REPO}/releases/latest" --jq .tag_name)"
RELEASE_NOTES="$(gh api "/repos/${GIT_REPO}/releases/generate-notes" \
  --raw-field "tag_name=v${TAG_NAME}" \
  --raw-field "target_commitish=${TARGET_BRANCH}" \
  --raw-field "previous_tag_name=${LATEST_TAG_NAME}" \
  | jq -r .body)"

# Create (draft) release and capture upload URL template and release ID
UPLOAD_URL="$(gh api "/repos/${GIT_REPO}/releases" \
  --raw-field "tag_name=v${TAG_NAME}" \
  --raw-field "body=${RELEASE_NOTES:0:125000}" \
  --raw-field "name=v${TAG_NAME}" \
  --field "draft=true" \
  --jq .upload_url)"

echo "Created draft release v${TAG_NAME}" >&2

# Upload assets matching the provided glob pattern(s)
for file in "${FILES[@]}"; do
  if [[ -f "$file" ]]; then
    case "${file##*.}" in
      apk) CONTENT_TYPE="application/vnd.android.package-archive" ;;
      aab) CONTENT_TYPE="application/octet-stream" ;;
      *)   CONTENT_TYPE=$(file --mime-type -b "$file" 2>/dev/null || echo "application/octet-stream") ;;
    esac

    BASENAME=$(basename "$file")
    if ! gh api "${UPLOAD_URL%\{*}?name=${BASENAME}" \
      --header "Content-Type: ${CONTENT_TYPE}" \
      --input "$file" >/dev/null; then
      echo "Failed to upload $file" >&2
      exit 1
    fi

    echo "Uploaded ${BASENAME}" >&2
  fi
done
