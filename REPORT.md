# Auth Update Report

## What Changed (Simple Overview)
- Login now issues only an access token (no refresh tokens).
- The access token is returned as a secure, HttpOnly cookie so the browser stores it automatically.
- Requests can authenticate using the cookie or a Bearer token in the Authorization header.
- Validation and error handling are centralized and return clear JSON responses.

## Files Updated or Added
- Auth flow and cookie response: [src/backend/src/main/java/com/example/demo/controller/AuthController.java](src/backend/src/main/java/com/example/demo/controller/AuthController.java)
- Login validation and access-token-only logic: [src/backend/src/main/java/com/example/demo/Service/AuthService.java](src/backend/src/main/java/com/example/demo/Service/AuthService.java)
- JWT service (access token only): [src/backend/src/main/java/com/example/demo/Security/JwtService.java](src/backend/src/main/java/com/example/demo/Security/JwtService.java)
- JWT filter (cookie or header token): [src/backend/src/main/java/com/example/demo/config/JwtFilter.java](src/backend/src/main/java/com/example/demo/config/JwtFilter.java)
- Login request validation: [src/backend/src/main/java/com/example/demo/entites/LoginRequest.java](src/backend/src/main/java/com/example/demo/entites/LoginRequest.java)
- Auth response message payload: [src/backend/src/main/java/com/example/demo/entites/AuthResponse.java](src/backend/src/main/java/com/example/demo/entites/AuthResponse.java)
- Global API error handling: [src/backend/src/main/java/com/example/demo/config/GlobalExceptionHandler.java](src/backend/src/main/java/com/example/demo/config/GlobalExceptionHandler.java)
- Error response DTO: [src/backend/src/main/java/com/example/demo/entites/ApiError.java](src/backend/src/main/java/com/example/demo/entites/ApiError.java)

## How It Works (Detailed but Simple)
### 1) Login Flow
- Endpoint: POST /auth/login
- Input: JSON with username and password.
- Behavior:
  - Validates fields (must not be blank).
  - Authenticates credentials using Spring Security.
  - Generates an access token only.
  - Writes the token into a Set-Cookie header named ACCESS_TOKEN.
  - Returns a JSON body with a friendly message and username.

### 2) Authentication for Protected Endpoints
- The JWT filter now checks for the token in two places:
  - Authorization header with Bearer <token>
  - Cookie named ACCESS_TOKEN
- If the token is valid, the request is authenticated.
- If the token is invalid or expired, the filter returns HTTP 401 with a JSON error message.

### 3) Error Handling
- Invalid input (missing username/password) returns HTTP 400 with a clear message.
- Invalid credentials return HTTP 401 with a clear message.
- Unexpected errors return HTTP 500 with a generic message.

## Manual Testing Steps
### Step A: Login and Receive Cookie
Use curl (or Postman). If you use curl, add -i to see headers.

Example:
```
curl -i -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"yourUser","password":"yourPass"}'
```

What to look for:
- Response status 200 OK
- A Set-Cookie header similar to:
  - Set-Cookie: ACCESS_TOKEN=...; Path=/; HttpOnly; SameSite=Lax
- JSON body:
```
{"message":"Login successful","username":"yourUser"}
```

### Step B: Use the Cookie to Access a Protected Route
If you use curl, pass the cookie back manually:
```
curl -i http://localhost:8080/users \
  -H "Cookie: ACCESS_TOKEN=<paste_token_value_here>"
```

Expected:
- HTTP 200 OK
- JSON list of users if authorized

### Step C: Test Invalid Login
```
curl -i -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"","password":""}'
```

Expected:
- HTTP 400
- JSON error with message about required fields

### Step D: Test Invalid/Expired Token
Pass a fake token:
```
curl -i http://localhost:8080/users \
  -H "Cookie: ACCESS_TOKEN=invalid.token.here"
```

Expected:
- HTTP 401
- JSON: {"message":"Invalid or expired token"}

## Notes
- The cookie is HttpOnly, so it is not accessible from JavaScript. This is a security feature.
- The cookie uses SameSite=Lax and Secure depends on whether the request is HTTPS.
- Access tokens expire in 5 minutes by default. You can change this in the JWT service if needed.
