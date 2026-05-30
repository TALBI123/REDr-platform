#!/usr/bin/env bash

set -u

BASE_URL="${BASE_URL:-http://localhost:8080}"
RUN_MUTATIONS="${RUN_MUTATIONS:-0}"
AGENCY_ID="${AGENCY_ID:-}"
VERIFICATION_TOKEN="${VERIFICATION_TOKEN:-}"
RESEND_EMAIL="${RESEND_EMAIL:-}"

ADMIN_EMAIL="${ADMIN_EMAIL:-admin@demo.com}"
ADMIN_PASSWORD="${ADMIN_PASSWORD:-admin123}"
MANAGER_EMAIL="${MANAGER_EMAIL:-manager@demo.com}"
MANAGER_PASSWORD="${MANAGER_PASSWORD:-manager123}"
CLIENT_EMAIL="${CLIENT_EMAIL:-client@demo.com}"
CLIENT_PASSWORD="${CLIENT_PASSWORD:-client123}"

COLOR_RED="\033[0;31m"
COLOR_GREEN="\033[0;32m"
COLOR_YELLOW="\033[0;33m"
COLOR_BLUE="\033[0;34m"
COLOR_RESET="\033[0m"

pass_count=0
fail_count=0
warn_count=0

log_info()  { echo -e "${COLOR_BLUE}[INFO]${COLOR_RESET} $1"; }
log_ok()    { echo -e "${COLOR_GREEN}[OK]${COLOR_RESET} $1"; pass_count=$((pass_count+1)); }
log_warn()  { echo -e "${COLOR_YELLOW}[WARN]${COLOR_RESET} $1"; warn_count=$((warn_count+1)); }
log_fail()  { echo -e "${COLOR_RED}[FAIL]${COLOR_RESET} $1"; fail_count=$((fail_count+1)); }

expect_status() {
  local name="$1"
  local expected="$2"
  local cmd="$3"
  local code
  code=$(eval "$cmd")
  if [[ "$code" == "$expected" ]]; then
    log_ok "$name -> $code"
  else
    log_fail "$name -> $code (expected $expected)"
  fi
}

login_role() {
  local role="$1"
  local email="$2"
  local password="$3"
  local cookie="$4"

  local code
  code=$(curl -s -o /dev/null -w "%{http_code}" -c "$cookie" \
    -H "Content-Type: application/json" \
    -d "{\"email\":\"$email\",\"password\":\"$password\"}" \
    "$BASE_URL/auth/login")

  if [[ "$code" == "200" ]]; then
    log_ok "login $role"
    return 0
  fi

  log_fail "login $role -> $code"
  return 1
}

TEMP_DIR=$(mktemp -d 2>/dev/null || echo "/tmp/endpoint-tests")
mkdir -p "$TEMP_DIR"
ADMIN_COOKIE="$TEMP_DIR/admin.cookies"
MANAGER_COOKIE="$TEMP_DIR/manager.cookies"
CLIENT_COOKIE="$TEMP_DIR/client.cookies"

log_info "Base URL: $BASE_URL"

login_role "SUPER_ADMIN" "$ADMIN_EMAIL" "$ADMIN_PASSWORD" "$ADMIN_COOKIE" || true
login_role "AGENCY_MANAGER" "$MANAGER_EMAIL" "$MANAGER_PASSWORD" "$MANAGER_COOKIE" || true
login_role "CLIENT" "$CLIENT_EMAIL" "$CLIENT_PASSWORD" "$CLIENT_COOKIE" || true

expect_status "GET /agencies without auth" "401" "curl -s -o /dev/null -w '%{http_code}' $BASE_URL/agencies"
expect_status "GET /test with client" "200" "curl -s -o /dev/null -w '%{http_code}' -b $CLIENT_COOKIE $BASE_URL/test"
expect_status "GET /agencies with client" "200" "curl -s -o /dev/null -w '%{http_code}' -b $CLIENT_COOKIE $BASE_URL/agencies"
expect_status "GET /bookings/my with client" "200" "curl -s -o /dev/null -w '%{http_code}' -b $CLIENT_COOKIE $BASE_URL/bookings/my"
expect_status "GET /bookings/my with manager" "403" "curl -s -o /dev/null -w '%{http_code}' -b $MANAGER_COOKIE $BASE_URL/bookings/my"
expect_status "GET /admin/users with admin" "200" "curl -s -o /dev/null -w '%{http_code}' -b $ADMIN_COOKIE $BASE_URL/admin/users"
expect_status "GET /admin/users with client" "403" "curl -s -o /dev/null -w '%{http_code}' -b $CLIENT_COOKIE $BASE_URL/admin/users"
expect_status "GET /admin/agencies/pending with admin" "200" "curl -s -o /dev/null -w '%{http_code}' -b $ADMIN_COOKIE $BASE_URL/admin/agencies/pending"

if [[ -n "$AGENCY_ID" ]]; then
  expect_status "GET /agencies/{id} with admin" "200" "curl -s -o /dev/null -w '%{http_code}' -b $ADMIN_COOKIE $BASE_URL/agencies/$AGENCY_ID"
  expect_status "GET /agencies/{id} with manager" "200" "curl -s -o /dev/null -w '%{http_code}' -b $MANAGER_COOKIE $BASE_URL/agencies/$AGENCY_ID"
else
  log_warn "AGENCY_ID not set, skipping /agencies/{id} tests"
fi

if [[ "$RUN_MUTATIONS" == "1" ]]; then
  log_info "RUN_MUTATIONS=1: running mutation endpoints"

  if [[ -n "$RESEND_EMAIL" ]]; then
    expect_status "POST /verification/resend" "200" "curl -s -o /dev/null -w '%{http_code}' -H 'Content-Type: application/json' -d '{\"email\":\"$RESEND_EMAIL\"}' $BASE_URL/verification/resend"
  else
    log_warn "RESEND_EMAIL not set, skipping /verification/resend"
  fi

  if [[ -n "$VERIFICATION_TOKEN" ]]; then
    expect_status "GET /verification/verify-email" "200" "curl -s -o /dev/null -w '%{http_code}' '$BASE_URL/verification/verify-email?token=$VERIFICATION_TOKEN'"
  else
    log_warn "VERIFICATION_TOKEN not set, skipping /verification/verify-email"
  fi

  if [[ -n "$AGENCY_ID" ]]; then
    expect_status "POST /admin/agencies/{id}/approve" "200" "curl -s -o /dev/null -w '%{http_code}' -b $ADMIN_COOKIE -H 'Content-Type: application/json' -d '{\"comment\":\"OK\"}' $BASE_URL/admin/agencies/$AGENCY_ID/approve"
    expect_status "POST /admin/agencies/{id}/reject" "200" "curl -s -o /dev/null -w '%{http_code}' -b $ADMIN_COOKIE -H 'Content-Type: application/json' -d '{\"reason\":\"Non conforme\"}' $BASE_URL/admin/agencies/$AGENCY_ID/reject"
    expect_status "POST /admin/agencies/{id}/suspend" "200" "curl -s -o /dev/null -w '%{http_code}' -b $ADMIN_COOKIE -H 'Content-Type: application/json' -d '{\"reason\":\"Incident\"}' $BASE_URL/admin/agencies/$AGENCY_ID/suspend"
  else
    log_warn "AGENCY_ID not set, skipping admin agency mutation tests"
  fi
else
  log_warn "RUN_MUTATIONS=0, skipping mutation endpoints"
fi

log_info "Done. Pass: $pass_count, Fail: $fail_count, Warn: $warn_count"

if [[ "$fail_count" -gt 0 ]]; then
  exit 1
fi
