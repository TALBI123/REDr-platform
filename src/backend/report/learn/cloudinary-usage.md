# Cloudinary usage guide

Document version: 2026-06-01
Scope: car photo upload and delete workflow
Audience: backend developers and testers
Goal: explain how to configure and use Cloudinary in this project

1. What is Cloudinary used for here
Cloudinary stores car photos outside the database.
The database keeps only URLs and the Cloudinary public_id.
This keeps the API fast and the database small.

2. Project configuration
Cloudinary is wired as a Spring bean.
The bean is created in AppConfig using app.cloudinary.* properties.
The properties are declared in application.yml.
The values are loaded from .env via spring.config.import.

3. Required .env variables
Set the following values in .env (do not commit real secrets).
- CLOUDINARY_CLOUD_NAME
- CLOUDINARY_API_KEY
- CLOUDINARY_API_SECRET
- CLOUDINARY_FOLDER (optional, default is cars)

4. Upload flow (API)
Endpoint: POST /agencies/{agencyId}/vehicles/{carId}/photos
Auth: SUPER_ADMIN or AGENCY_MANAGER
Request type: multipart/form-data

Example curl
curl -i -b cookies.txt \
  -F "file=@C:/path/to/photo.jpg" \
  -F "description=front view" \
  http://localhost:8080/agencies/AGENCY_ID/vehicles/CAR_ID/photos

Expected result
- Cloudinary returns url, secure_url, public_id
- A Photo entity is stored with these fields
- The response returns the saved photo data

5. List and delete flow
List photos (public): GET /public/vehicles/{carId}/photos
Delete (agency): DELETE /agencies/{agencyId}/vehicles/{carId}/photos/{photoId}

6. Common errors and fixes
- 401 or 403: check your auth cookie and role
- 404: check that carId and agencyId are correct
- 400 "Photo file is required": file part is missing or empty
- 500 from Cloudinary: verify credentials and network access

7. Security and operational notes
- Keep .env outside git and never log secrets
- Add max file size limits in Spring if needed
- Accept only image content types on the API gateway or controller
- Consider adding a photo count limit per car
