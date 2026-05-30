# Testing guide

## Prereqs
- JDK installed and JAVA_HOME set
- Gradle wrapper available in the backend root

## Build and unit tests
1. Open a terminal in the backend root folder.
2. Run `gradlew.bat test`.
3. Run `gradlew.bat build`.

## Run the app
1. Start the app with `gradlew.bat bootRun`.
2. Confirm the app starts without errors.

## Manual checks
1. Call /auth/login with a valid user to get an ACCESS_TOKEN cookie.
2. Call /bookings/my and confirm access depends on role.
3. Call /admin/agencies/pending as SUPER_ADMIN and confirm access.
4. Call /verification/confirm?token=... to validate email flow.

## Troubleshooting
- If login fails, check `app.jwt.secret` and `app.jwt.expiration-days` in the config.
- If authorization fails, verify the user role is `SUPER_ADMIN`, `AGENCY_MANAGER`, or `CLIENT`.
