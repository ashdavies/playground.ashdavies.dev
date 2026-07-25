#!/usr/bin/env bash

set -euo pipefail

if [[ "${1:-}" == "-h" || "${1:-}" == "--help" ]]; then
  echo "Usage: $0 <PATCH_NUMBER>"
  echo "Outputs the formatted version name (MAJOR.MINOR.PATCH) based on the latest git tag."
  exit 0
fi

if [[ -z "${1:-}" ]]; then
  echo "Error: Missing required parameter <PATCH_NUMBER>." >&2
  exit 1
fi

RAW_TAG=$(git describe --tags --match "v*" --abbrev=0 2>/dev/null || echo "v0.0.0")
LATEST="${RAW_TAG#v}"

MAJOR=$(echo "$LATEST" | cut -d '.' -f1)
MINOR=$(echo "$LATEST" | cut -d '.' -f2)

MAJOR="${MAJOR:-0}"
MINOR="${MINOR:-0}"
PATCH="$1"

VERSION_NAME="${MAJOR}.${MINOR}.${PATCH}"
TAG_NAME="v${VERSION_NAME}"

if git rev-parse -q --verify "refs/tags/${TAG_NAME}" >/dev/null; then
  echo "Error: Version '${VERSION_NAME}' (tag '${TAG_NAME}') already exists!" >&2
  exit 1
fi

echo "${VERSION_NAME}"