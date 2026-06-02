# Cars and photos module details

Document version: 2026-06-01
Scope: cars module, photos module, and DTO design choices
Audience: backend developers
Goal: explain what was added and why

1. Why multiple DTOs
Using multiple DTOs keeps each API focused and safe.
- It prevents over posting (clients cannot update fields they should not touch).
- It allows strong validation only when needed (create vs update).
- It keeps response data stable even if the entity changes.
- It avoids exposing internal fields like ids of related entities by default.
- It makes search filters explicit and easy to evolve.

2. Cars DTOs and field meaning
CarRequestDTO (create)
- brand: car brand name used for display and search
- mode: model name used for display and search
- year: manufacturing year
- fuelTypes: list of allowed fuels for the car
- transmissionType: MANUAL or AUTOMATIC
- mileage: current mileage in km
- dailyPrice: price per day
- weeklyPrice: price per week
- monthlyPrice: price per month
- promotionActive: flag to enable discount
- promotionRate: discount rate when promotionActive is true
- promotionStartDate: start of promotion period
- promotionEndDate: end of promotion period
- conditionStatus: EXCELLENT, GOOD, FAIR, NEEDS_REPAIR, DAMAGED
- licenseExpiryDate: expiry of vehicle license
- insuranceExpiryDate: expiry of insurance
- registrationNumber: plate or registration id
- insurancePolicyNumber: insurance policy reference
- currentStatus: initial status if admin wants to set it
- seatCapacity: number of seats
- technicalNotes: internal notes for maintenance
- description: public description for clients
- agencyId: owning agency for admin creation
- categoryId: category id for filtering and grouping

CarUpdateDTO (partial update)
- Same fields as create except agencyId
- All fields are optional to support partial updates
- categoryId allows changing the category if needed

CarStatusUpdateDTO (status only)
- currentStatus: Available, Reserved, Rented, etc
- conditionStatus: condition tracking without changing other fields

CarSearchCriteriaDTO (query filters)
- brand, mode: text filters
- minYear, maxYear: year range
- fuelType: member of fuelTypes list
- transmissionType: MANUAL or AUTOMATIC
- status: filter by VehicleStatus
- conditionStatus: filter by ConditionStatus
- minMileage, maxMileage: mileage range
- minDailyPrice, maxDailyPrice: price range
- promotionActive: filter promotions
- seatCapacity: exact match
- agencyId, categoryId: ownership and grouping filters

CarResponseDTO (read model)
- id: car id
- brand, mode, year: display fields
- fuelTypes, transmissionType, mileage: technical details
- currentStatus: current availability
- dailyPrice, weeklyPrice, monthlyPrice: pricing data
- promotionActive, promotionRate, promotionStartDate, promotionEndDate
- conditionStatus: current condition
- licenseExpiryDate, insuranceExpiryDate
- registrationNumber, insurancePolicyNumber
- averageRating: aggregate rating
- seatCapacity, technicalNotes, description
- agencyId, categoryId: related ids
- photoUrls: public list of photo URLs

3. Photos DTOs and field meaning
PhotoResponseDTO
- id: photo id
- url: Cloudinary url
- secureUrl: https url
- publicId: Cloudinary public id
- description: optional description
- createdAt: created timestamp
- carId: linked car id
- conditionReportId: linked report id if any

PhotoUpdateDTO
- description: update the photo description only

4. Controllers added
PublicCarController
- GET /public/vehicles and /public/vehicles/{carId}
AgencyCarController
- CRUD for /agencies/{agencyId}/vehicles
AdminCarController
- CRUD for /admin/vehicles
PublicPhotoController
- GET /public/vehicles/{carId}/photos
AgencyPhotoController
- POST, PATCH, DELETE for /agencies/{agencyId}/vehicles/{carId}/photos

5. Services and repositories added
CarService handles search, create, update, status changes, and deletes.
CarRepository and CarSpecifications handle database queries and filters.
CategoryRepository was added to link cars to categories.
PhotoService handles upload, update, list, and delete of photos.
PhotoRepository provides photo queries by car id.

6. Mappers added
CarCommandMapper converts create and update DTOs to Car entities.
CarResponseMapper builds response DTOs and collects photo URLs.
PhotoResponseMapper maps Photo entities to response DTOs.

7. Configuration added
Cloudinary dependency in build.gradle.
Cloudinary bean in AppConfig.
Cloudinary properties in application.yml and .env.
Public access added for GET /public/vehicles/**.
