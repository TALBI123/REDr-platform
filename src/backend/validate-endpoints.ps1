$BaseUrl = $env:BASE_URL
if ([string]::IsNullOrWhiteSpace($BaseUrl)) { $BaseUrl = "http://localhost:8080" }

$RunMutations = $env:RUN_MUTATIONS
if ([string]::IsNullOrWhiteSpace($RunMutations)) { $RunMutations = "0" }

$AgencyId = $env:AGENCY_ID
$VerificationToken = $env:VERIFICATION_TOKEN
$ResendEmail = $env:RESEND_EMAIL

$AdminEmail = $env:ADMIN_EMAIL; if ([string]::IsNullOrWhiteSpace($AdminEmail)) { $AdminEmail = "admin@demo.com" }
$AdminPassword = $env:ADMIN_PASSWORD; if ([string]::IsNullOrWhiteSpace($AdminPassword)) { $AdminPassword = "admin123" }
$ManagerEmail = $env:MANAGER_EMAIL; if ([string]::IsNullOrWhiteSpace($ManagerEmail)) { $ManagerEmail = "manager@demo.com" }
$ManagerPassword = $env:MANAGER_PASSWORD; if ([string]::IsNullOrWhiteSpace($ManagerPassword)) { $ManagerPassword = "manager123" }
$ClientEmail = $env:CLIENT_EMAIL; if ([string]::IsNullOrWhiteSpace($ClientEmail)) { $ClientEmail = "client@demo.com" }
$ClientPassword = $env:CLIENT_PASSWORD; if ([string]::IsNullOrWhiteSpace($ClientPassword)) { $ClientPassword = "client123" }

$PassCount = 0
$FailCount = 0
$WarnCount = 0

function Log-Info($msg) { Write-Host "[INFO] $msg" -ForegroundColor Cyan }
function Log-Ok($msg) { Write-Host "[OK] $msg" -ForegroundColor Green; $script:PassCount++ }
function Log-Warn($msg) { Write-Host "[WARN] $msg" -ForegroundColor Yellow; $script:WarnCount++ }
function Log-Fail($msg) { Write-Host "[FAIL] $msg" -ForegroundColor Red; $script:FailCount++ }

function Invoke-Status($name, $expected, $args) {
  $code = & curl.exe @args
  if ($code -eq $expected) {
    Log-Ok "$name -> $code"
  } else {
    Log-Fail "$name -> $code (expected $expected)"
  }
}

function Login-Role($role, $email, $password, $cookiePath) {
  $payload = "{\"email\":\"$email\",\"password\":\"$password\"}"
  $code = & curl.exe -s -o NUL -w "%{http_code}" -c $cookiePath -H "Content-Type: application/json" -d $payload "$BaseUrl/auth/login"
  if ($code -eq 200) {
    Log-Ok "login $role"
    return $true
  }
  Log-Fail "login $role -> $code"
  return $false
}

$TempDir = Join-Path $env:TEMP "endpoint-tests"
New-Item -ItemType Directory -Force -Path $TempDir | Out-Null
$AdminCookie = Join-Path $TempDir "admin.cookies"
$ManagerCookie = Join-Path $TempDir "manager.cookies"
$ClientCookie = Join-Path $TempDir "client.cookies"

Log-Info "Base URL: $BaseUrl"

Login-Role "SUPER_ADMIN" $AdminEmail $AdminPassword $AdminCookie | Out-Null
Login-Role "AGENCY_MANAGER" $ManagerEmail $ManagerPassword $ManagerCookie | Out-Null
Login-Role "CLIENT" $ClientEmail $ClientPassword $ClientCookie | Out-Null

Invoke-Status "GET /agencies without auth" 401 @("-s","-o","NUL","-w","%{http_code}","$BaseUrl/agencies")
Invoke-Status "GET /test with client" 200 @("-s","-o","NUL","-w","%{http_code}","-b",$ClientCookie,"$BaseUrl/test")
Invoke-Status "GET /agencies with client" 200 @("-s","-o","NUL","-w","%{http_code}","-b",$ClientCookie,"$BaseUrl/agencies")
Invoke-Status "GET /bookings/my with client" 200 @("-s","-o","NUL","-w","%{http_code}","-b",$ClientCookie,"$BaseUrl/bookings/my")
Invoke-Status "GET /bookings/my with manager" 403 @("-s","-o","NUL","-w","%{http_code}","-b",$ManagerCookie,"$BaseUrl/bookings/my")
Invoke-Status "GET /admin/users with admin" 200 @("-s","-o","NUL","-w","%{http_code}","-b",$AdminCookie,"$BaseUrl/admin/users")
Invoke-Status "GET /admin/users with client" 403 @("-s","-o","NUL","-w","%{http_code}","-b",$ClientCookie,"$BaseUrl/admin/users")
Invoke-Status "GET /admin/agencies/pending with admin" 200 @("-s","-o","NUL","-w","%{http_code}","-b",$AdminCookie,"$BaseUrl/admin/agencies/pending")

if (-not [string]::IsNullOrWhiteSpace($AgencyId)) {
  Invoke-Status "GET /agencies/{id} with admin" 200 @("-s","-o","NUL","-w","%{http_code}","-b",$AdminCookie,"$BaseUrl/agencies/$AgencyId")
  Invoke-Status "GET /agencies/{id} with manager" 200 @("-s","-o","NUL","-w","%{http_code}","-b",$ManagerCookie,"$BaseUrl/agencies/$AgencyId")
} else {
  Log-Warn "AGENCY_ID not set, skipping /agencies/{id} tests"
}

if ($RunMutations -eq "1") {
  Log-Info "RUN_MUTATIONS=1: running mutation endpoints"

  if (-not [string]::IsNullOrWhiteSpace($ResendEmail)) {
    Invoke-Status "POST /verification/resend" 200 @("-s","-o","NUL","-w","%{http_code}","-H","Content-Type: application/json","-d","{\"email\":\"$ResendEmail\"}","$BaseUrl/verification/resend")
  } else {
    Log-Warn "RESEND_EMAIL not set, skipping /verification/resend"
  }

  if (-not [string]::IsNullOrWhiteSpace($VerificationToken)) {
    Invoke-Status "GET /verification/verify-email" 200 @("-s","-o","NUL","-w","%{http_code}","$BaseUrl/verification/verify-email?token=$VerificationToken")
  } else {
    Log-Warn "VERIFICATION_TOKEN not set, skipping /verification/verify-email"
  }

  if (-not [string]::IsNullOrWhiteSpace($AgencyId)) {
    Invoke-Status "POST /admin/agencies/{id}/approve" 200 @("-s","-o","NUL","-w","%{http_code}","-b",$AdminCookie,"-H","Content-Type: application/json","-d","{\"comment\":\"OK\"}","$BaseUrl/admin/agencies/$AgencyId/approve")
    Invoke-Status "POST /admin/agencies/{id}/reject" 200 @("-s","-o","NUL","-w","%{http_code}","-b",$AdminCookie,"-H","Content-Type: application/json","-d","{\"reason\":\"Non conforme\"}","$BaseUrl/admin/agencies/$AgencyId/reject")
    Invoke-Status "POST /admin/agencies/{id}/suspend" 200 @("-s","-o","NUL","-w","%{http_code}","-b",$AdminCookie,"-H","Content-Type: application/json","-d","{\"reason\":\"Incident\"}","$BaseUrl/admin/agencies/$AgencyId/suspend")
  } else {
    Log-Warn "AGENCY_ID not set, skipping admin agency mutation tests"
  }
} else {
  Log-Warn "RUN_MUTATIONS=0, skipping mutation endpoints"
}

Log-Info "Done. Pass: $PassCount, Fail: $FailCount, Warn: $WarnCount"
if ($FailCount -gt 0) { exit 1 }
