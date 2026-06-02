# Cars and photos unit tests

Document version: 2026-06-01
Scope: unit tests for CarService and PhotoService
Audience: developers and reviewers
Goal: explain what each test covers and why

1. CarServiceTest
Test: createCar_requiresAgencyId
- Given: a create request without agencyId
- When: createCar is called
- Then: IllegalArgumentException is thrown with a clear message
- Why: admin creates must always provide the owning agency

Test: createCar_setsDefaultsAndLinksAgencyAndCategory
- Given: a create request with agencyId and categoryId
- When: createCar is called
- Then: currentStatus defaults to Available
- Then: fuelTypes is not null
- Then: agency and category are linked to the car
- Why: safe defaults and correct relations prevent later null checks

Test: updateCarForAgency_usesScopedRepositoryAndMapper
- Given: a car that belongs to an agency
- When: updateCarForAgency is called
- Then: the scoped repository method is used
- Then: the mapper updates the entity
- Then: the car is saved
- Why: agency scope is enforced and updates are applied through the mapper

2. PhotoServiceTest
Test: listCarPhotos_requiresExistingCar
- Given: a car id that does not exist
- When: listCarPhotos is called
- Then: ResourceNotFoundException is thrown
- Why: return a clear 404 instead of an empty list for invalid ids

Test: uploadCarPhoto_requiresFile
- Given: an empty multipart file
- When: uploadCarPhoto is called
- Then: IllegalArgumentException is thrown
- Why: prevents storing empty photo entries

Test: uploadCarPhoto_deniesWrongAgency
- Given: a car owned by a different agency
- When: uploadCarPhoto is called with another agency id
- Then: AccessDeniedException is thrown
- Why: protects assets across agencies

Test: uploadCarPhoto_uploadsToCloudinaryAndSaves
- Given: a valid car and multipart file
- When: uploadCarPhoto is called
- Then: the Cloudinary uploader returns urls and public id
- Then: the Photo entity stores these values and the description
- Why: verifies the full upload mapping logic

Test: deleteCarPhoto_requiresMatchingCar
- Given: a photo linked to a different car id
- When: deleteCarPhoto is called
- Then: ResourceNotFoundException is thrown and delete is not executed
- Why: prevents deleting photos from the wrong car

3. How to run tests
- Run all tests: ./gradlew test
- Run only these tests: ./gradlew test --tests "com.example.demo.cars.service.CarServiceTest" --tests "com.example.demo.photos.service.PhotoServiceTest"
