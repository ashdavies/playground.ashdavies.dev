#!/usr/bin/env bash

set -eo pipefail

show_help() {
    cat <<EOF
Usage: $0 --issuer <issuer> --private-key <private_key>

Options:
  --issuer            The JWT issuer (iss claim).
  --private-key       The base64-encoded private key.
  --help              Show this help message.

Description:
  Generates a JWT signed with RS256 using the provided issuer and private key.
EOF
}

while [[ $# -gt 0 ]]; do
    case "$1" in
        --issuer)
            ISS="$2"
            shift 2
            ;;
        --private-key)
            PRIVATE_KEY="$2"
            shift 2
            ;;
        --help)
            show_help
            exit 0
            ;;
        *)
            echo "Unknown option: $1" >&2
            show_help
            exit 1
            ;;
    esac
done

# Check required arguments
if [[ -z "${ISS}" || -z "${PRIVATE_KEY}" ]]; then
    echo "Error: --issuer and --private-key are required." >&2
    show_help
    exit 1
fi

# Check for dependencies
for cmd in jq openssl; do
  if ! command -v "$cmd" >/dev/null 2>&1; then
    echo "Error: Required command '$cmd' not found." >&2
    exit 2
  fi
done

IAT=$(date +%s)
EXP=$((IAT + 540))

HEADER=$(printf '{"alg":"RS256","typ":"JWT"}' | openssl base64 -A | tr -d '=' | tr '/+' '_-')
PAYLOAD=$(printf '{"iat":%d,"exp":%d,"iss":"%s"}' "$IAT" "$EXP" "$ISS" | openssl base64 -A | tr -d '=' | tr '/+' '_-')
JWT=$(printf '%s.%s' "$HEADER" "$PAYLOAD")

SIGNATURE=$(printf '%s' "$JWT" | \
  openssl dgst -sha256 -sign <(printf '%s' "$PRIVATE_KEY" | base64 --decode) | \
  openssl base64 -A | tr -d '=' | tr '/+' '_-')

printf '%s.%s' "$JWT" "$SIGNATURE"
