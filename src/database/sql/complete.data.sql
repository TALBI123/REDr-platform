-- =====================================================
-- SCRIPT COMPLET POUR POSTGRESQL - AVEC DONNÉES POUR FILTRAGE
-- =====================================================

DO $$
DECLARE
    -- UUIDs des agences
    agency_casa UUID := '123e4567-e89b-12d3-a456-426614174001';
    agency_rabat UUID := '123e4567-e89b-12d3-a456-426614174002';
    agency_marrakech UUID := '123e4567-e89b-12d3-a456-426614174003';
    agency_tanger UUID := '123e4567-e89b-12d3-a456-426614174004';
    agency_fes UUID := '123e4567-e89b-12d3-a456-426614174005';
    
    -- UUIDs des catégories
    cat_eco UUID;
    cat_compact UUID;
    cat_suv UUID;
    cat_luxe UUID;
    
    -- UUIDs des voitures
    car_ids UUID[];

BEGIN

-- =====================================================
-- 1. AGENCES
-- =====================================================

INSERT INTO agencies (id, name, city, phone, email, status, approval_date, inscription_date, rating, logo_url, iban, description, address)
SELECT agency_casa, 'SpeedCar Maroc', 'Casablanca', '+212522123456', 'contact@speedcar.ma', 'APPROVED', CURRENT_DATE, CURRENT_DATE, 4.8, 'https://res.cloudinary.com/demo/image/upload/v1/logos/speedcar', 'MA6401234567890123456789', 'Location de voitures de luxe et économiques à Casablanca', '123 Bd Mohammed V, Casablanca'
WHERE NOT EXISTS (SELECT 1 FROM agencies WHERE name = 'SpeedCar Maroc');

INSERT INTO agencies (id, name, city, phone, email, status, approval_date, inscription_date, rating, logo_url, iban, description, address)
SELECT agency_rabat, 'Rabat Rent Car', 'Rabat', '+212537789012', 'contact@rabatrent.ma', 'APPROVED', CURRENT_DATE, CURRENT_DATE, 4.5, 'https://res.cloudinary.com/demo/image/upload/v1/logos/rabatrent', 'MA6409876543210987654321', 'Service premium à Rabat', '45 Avenue Mohamed V, Rabat'
WHERE NOT EXISTS (SELECT 1 FROM agencies WHERE name = 'Rabat Rent Car');

INSERT INTO agencies (id, name, city, phone, email, status, approval_date, inscription_date, rating, logo_url, iban, description, address)
SELECT agency_marrakech, 'Marrakech Car Rental', 'Marrakech', '+212524456789', 'contact@marrakechcar.ma', 'APPROVED', CURRENT_DATE, CURRENT_DATE, 4.9, 'https://res.cloudinary.com/demo/image/upload/v1/logos/marrakechcar', 'MA6405556667778889990001', 'Location de voitures à Marrakech - Prix compétitifs', '789 Rue de la Koutoubia, Marrakech'
WHERE NOT EXISTS (SELECT 1 FROM agencies WHERE name = 'Marrakech Car Rental');

INSERT INTO agencies (id, name, city, phone, email, status, approval_date, inscription_date, rating, logo_url, iban, description, address)
SELECT agency_tanger, 'Tangier Drive', 'Tanger', '+212539345678', 'contact@tangierdrive.ma', 'APPROVED', CURRENT_DATE, CURRENT_DATE, 4.3, 'https://res.cloudinary.com/demo/image/upload/v1/logos/tangierdrive', 'MA6401112223334445556667', 'Location de voitures à Tanger', '321 Avenue des FAR, Tanger'
WHERE NOT EXISTS (SELECT 1 FROM agencies WHERE name = 'Tangier Drive');

INSERT INTO agencies (id, name, city, phone, email, status, approval_date, inscription_date, rating, logo_url, iban, description, address)
SELECT agency_fes, 'Fes Auto Location', 'Fès', '+212535901234', 'contact@fesauto.ma', 'PENDING', NULL, CURRENT_DATE, NULL, 'https://res.cloudinary.com/demo/image/upload/v1/logos/fesauto', 'MA6409998887776665554443', 'Agence en attente d approbation', '567 Rue Talaa Kebira, Fès'
WHERE NOT EXISTS (SELECT 1 FROM agencies WHERE name = 'Fes Auto Location');

-- =====================================================
-- 2. CATÉGORIES
-- =====================================================

INSERT INTO categories (id, name, description)
SELECT gen_random_uuid() INTO cat_eco, 'Economique', 'Petites voitures économiques - Parfaites pour la ville'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Economique');

INSERT INTO categories (id, name, description)
SELECT gen_random_uuid() INTO cat_compact, 'Compacte', 'Voitures compactes - Idéal pour les trajets courts et moyens'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Compacte');

INSERT INTO categories (id, name, description)
SELECT gen_random_uuid() INTO cat_suv, 'SUV', 'SUV et 4x4 - Pour les familles et les aventures'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'SUV');

INSERT INTO categories (id, name, description)
SELECT gen_random_uuid() INTO cat_luxe, 'Luxe', 'Voitures de luxe - Confort et élégance'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Luxe');

-- Récupérer les IDs des catégories existantes
SELECT id INTO cat_eco FROM categories WHERE name = 'Economique';
SELECT id INTO cat_compact FROM categories WHERE name = 'Compacte';
SELECT id INTO cat_suv FROM categories WHERE name = 'SUV';
SELECT id INTO cat_luxe FROM categories WHERE name = 'Luxe';

-- =====================================================
-- 3. VOITURES (25 voitures pour tests de filtrage)
-- =====================================================

-- SpeedCar Maroc - Casablanca (8 voitures)
INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), '208', 'Peugeot', 2023, 'MANUAL', 15000, 'Available', 250, 1500, 5000, false, NULL, NULL, NULL, 'EXCELLENT', '2026-12-31', '2026-06-30', '1234-A-78', 5, 'Peugeot 208 neuve', agency_casa, cat_eco;

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Clio', 'Renault', 2024, 'MANUAL', 5000, 'Available', 230, 1400, 4800, true, 15, CURRENT_DATE, '2026-07-31', 'EXCELLENT', '2027-12-31', '2026-12-31', '5678-B-12', 5, 'Renault Clio - Promotion 15%', agency_casa, cat_eco;

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Duster', 'Dacia', 2022, 'MANUAL', 45000, 'Reserved', 400, 2400, 8000, false, NULL, NULL, NULL, 'GOOD', '2026-10-31', '2026-05-30', '9101-C-45', 5, 'Dacia Duster - SUV abordable', agency_casa, cat_suv;

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Megane', 'Renault', 2023, 'AUTOMATIC', 8000, 'Available', 350, 2100, 7000, false, NULL, NULL, NULL, 'EXCELLENT', '2027-01-31', '2026-08-31', '1213-D-67', 5, 'Renault Megane - Confort', agency_casa, cat_compact;

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), '5008', 'Peugeot', 2023, 'AUTOMATIC', 5000, 'Rented', 550, 3300, 11000, false, NULL, NULL, NULL, 'EXCELLENT', '2027-02-28', '2026-09-30', '1415-E-89', 7, 'Peugeot 5008 - Familial', agency_casa, cat_suv;

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Classe A', 'Mercedes', 2024, 'AUTOMATIC', 2000, 'Available', 1200, 7200, 24000, true, 20, CURRENT_DATE, '2026-08-31', 'EXCELLENT', '2028-12-31', '2027-06-30', '1617-F-01', 5, 'Mercedes Classe A - Luxe', agency_casa, cat_luxe;

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Captur', 'Renault', 2024, 'AUTOMATIC', 3000, 'AvailableSoon', 380, 2280, 7600, false, NULL, NULL, NULL, 'EXCELLENT', '2027-11-30', '2026-11-30', '1819-G-23', 5, 'Renault Captur', agency_casa, cat_compact;

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Hilux', 'Toyota', 2021, 'MANUAL', 85000, 'Maintenance', 500, 3000, 10000, false, NULL, NULL, NULL, 'FAIR', '2026-09-30', '2026-05-15', '2021-H-45', 5, 'Toyota Hilux', agency_casa, cat_suv;

-- Rabat Rent Car (6 voitures)
INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'i10', 'Hyundai', 2023, 'MANUAL', 18000, 'Available', 220, 1300, 4500, false, NULL, NULL, NULL, 'EXCELLENT', '2026-12-31', '2026-07-31', '2223-I-67', 5, 'Hyundai i10', agency_rabat, cat_eco;

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Civic', 'Honda', 2023, 'AUTOMATIC', 12000, 'Available', 450, 2700, 9000, true, 10, CURRENT_DATE, '2026-09-30', 'EXCELLENT', '2027-06-30', '2026-12-31', '2425-J-89', 5, 'Honda Civic', agency_rabat, cat_compact;

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'X1', 'BMW', 2024, 'AUTOMATIC', 1000, 'Available', 950, 5700, 19000, false, NULL, NULL, NULL, 'EXCELLENT', '2028-12-31', '2027-06-30', '2627-K-01', 5, 'BMW X1', agency_rabat, cat_luxe;

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'C3', 'Citroen', 2022, 'MANUAL', 35000, 'Reserved', 200, 1200, 4000, false, NULL, NULL, NULL, 'GOOD', '2026-11-30', '2026-06-15', '2829-L-23', 5, 'Citroen C3', agency_rabat, cat_eco;

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Kangoo', 'Renault', 2023, 'MANUAL', 25000, 'Available', 300, 1800, 6000, false, NULL, NULL, NULL, 'GOOD', '2027-03-31', '2026-09-30', '3031-M-45', 7, 'Renault Kangoo - Utilitaire', agency_rabat, cat_suv;

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Fiesta', 'Ford', 2024, 'MANUAL', 4000, 'Available', 260, 1560, 5200, true, 12, CURRENT_DATE, '2026-10-31', 'EXCELLENT', '2027-12-31', '2027-06-30', '3243-N-67', 5, 'Ford Fiesta', agency_rabat, cat_eco;

-- Marrakech Car Rental (6 voitures)
INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'A3', 'Audi', 2024, 'AUTOMATIC', 2000, 'Available', 800, 4800, 16000, false, NULL, NULL, NULL, 'EXCELLENT', '2028-12-31', '2027-06-30', '3456-O-89', 5, 'Audi A3', agency_marrakech, cat_luxe;

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Corsa', 'Opel', 2022, 'MANUAL', 35000, 'AvailableSoon', 240, 1440, 4800, false, NULL, NULL, NULL, 'GOOD', '2026-11-30', '2026-06-15', '3637-P-01', 5, 'Opel Corsa', agency_marrakech, cat_eco;

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Qashqai', 'Nissan', 2023, 'AUTOMATIC', 15000, 'Rented', 500, 3000, 10000, false, NULL, NULL, NULL, 'EXCELLENT', '2027-08-31', '2026-08-31', '3839-Q-23', 5, 'Nissan Qashqai', agency_marrakech, cat_suv;

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Polo', 'Volkswagen', 2024, 'MANUAL', 3000, 'Available', 280, 1680, 5600, true, 10, CURRENT_DATE, '2026-09-30', 'EXCELLENT', '2027-12-31', '2027-03-31', '4041-R-45', 5, 'Volkswagen Polo', agency_marrakech, cat_compact;

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'C4', 'Citroen', 2023, 'AUTOMATIC', 20000, 'Maintenance', 350, 2100, 7000, false, NULL, NULL, NULL, 'FAIR', '2027-05-31', '2026-05-31', '4243-S-67', 5, 'Citroen C4', agency_marrakech, cat_compact;

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Series 3', 'BMW', 2024, 'AUTOMATIC', 1000, 'Available', 1100, 6600, 22000, false, NULL, NULL, NULL, 'EXCELLENT', '2029-12-31', '2027-12-31', '4445-T-89', 5, 'BMW Serie 3', agency_marrakech, cat_luxe;

-- Tangier Drive (5 voitures)
INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Picanto', 'Kia', 2023, 'MANUAL', 7000, 'Available', 210, 1260, 4200, true, 12, CURRENT_DATE, '2026-09-30', 'EXCELLENT', '2027-12-31', '2026-12-31', '4647-U-01', 5, 'Kia Picanto', agency_tanger, cat_eco;

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Tucson', 'Hyundai', 2023, 'AUTOMATIC', 12000, 'Reserved', 600, 3600, 12000, false, NULL, NULL, NULL, 'EXCELLENT', '2027-01-31', '2026-08-31', '4849-V-23', 5, 'Hyundai Tucson', agency_tanger, cat_suv;

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Rio', 'Kia', 2022, 'MANUAL', 40000, 'Available', 220, 1320, 4400, false, NULL, NULL, NULL, 'GOOD', '2026-10-31', '2026-05-31', '5051-W-45', 5, 'Kia Rio', agency_tanger, cat_eco;

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Leon', 'Seat', 2023, 'AUTOMATIC', 18000, 'Available', 320, 1920, 6400, false, NULL, NULL, NULL, 'EXCELLENT', '2027-04-30', '2026-10-31', '5253-X-67', 5, 'Seat Leon', agency_tanger, cat_compact;

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Sportage', 'Kia', 2024, 'AUTOMATIC', 5000, 'Available', 550, 3300, 11000, true, 15, CURRENT_DATE, '2026-11-30', 'EXCELLENT', '2028-12-31', '2027-06-30', '5455-Y-89', 5, 'Kia Sportage', agency_tanger, cat_suv;

-- =====================================================
-- 4. CAR_FUEL_TYPES
-- =====================================================

INSERT INTO car_fuel_types (car_id, fuel_type)
SELECT c.id, 'GASOLINE' FROM cars c WHERE c.brand IN ('Peugeot', 'Renault', 'Citroen', 'Ford', 'Opel', 'Seat', 'Kia') AND c.mode NOT IN ('Duster', '5008', 'Hilux', 'Kangoo');

INSERT INTO car_fuel_types (car_id, fuel_type)
SELECT c.id, 'DIESEL' FROM cars c WHERE c.mode IN ('Duster', '5008', 'Hilux', 'Kangoo', 'Qashqai', 'Tucson', 'Sportage');

INSERT INTO car_fuel_types (car_id, fuel_type)
SELECT c.id, 'HYBRID' FROM cars c WHERE c.brand IN ('Toyota', 'Honda') OR c.mode = 'Clio';

INSERT INTO car_fuel_types (car_id, fuel_type)
SELECT c.id, 'ELECTRIC' FROM cars c WHERE c.brand = 'BMW' AND c.mode = 'i3';

-- =====================================================
-- 5. PHOTOS (2-3 photos par voiture)
-- =====================================================

INSERT INTO photos (id, url, secure_url, public_id, description, car_id, created_at_override)
SELECT gen_random_uuid(), 
       'https://res.cloudinary.com/demo/image/upload/v1/cars/' || c.mode || '-front',
       'https://res.cloudinary.com/demo/image/upload/v1/cars/' || c.mode || '-front',
       'cars/' || c.mode || '-front',
       c.mode || ' - Vue avant',
       c.id,
       CURRENT_TIMESTAMP
FROM cars c
WHERE NOT EXISTS (SELECT 1 FROM photos p WHERE p.car_id = c.id AND p.description LIKE '%avant%');

INSERT INTO photos (id, url, secure_url, public_id, description, car_id, created_at_override)
SELECT gen_random_uuid(),
       'https://res.cloudinary.com/demo/image/upload/v1/cars/' || c.mode || '-interior',
       'https://res.cloudinary.com/demo/image/upload/v1/cars/' || c.mode || '-interior',
       'cars/' || c.mode || '-interior',
       c.mode || ' - Intérieur',
       c.id,
       CURRENT_TIMESTAMP
FROM cars c
WHERE NOT EXISTS (SELECT 1 FROM photos p WHERE p.car_id = c.id AND p.description LIKE '%Intérieur%');

-- =====================================================
-- 6. UTILISATEURS
-- =====================================================

-- SUPER ADMIN
INSERT INTO users (id, first_name, last_name, email, password, password_updated_at, inscription_date, role, account_status, failed_login_attempts, lock_until, email_verified_at, user_type)
SELECT gen_random_uuid(), 'Youssef', 'Admin', 'admin@carrental.ma', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'SUPER_ADMIN', 'ACTIVE', 0, NULL, CURRENT_TIMESTAMP, 'SUPER_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@carrental.ma');

INSERT INTO admins (id, permissions, admin_level)
SELECT u.id, 'ALL', 1
FROM users u
WHERE u.email = 'admin@carrental.ma'
AND NOT EXISTS (SELECT 1 FROM admins a WHERE a.id = u.id);

-- AGENCY MANAGERS (5)
INSERT INTO users (id, first_name, last_name, email, password, password_updated_at, inscription_date, role, account_status, failed_login_attempts, lock_until, email_verified_at, user_type)
SELECT gen_random_uuid(), 'Karim', 'Benjelloun', 'karim@speedcar.ma', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'AGENCY_MANAGER', 'ACTIVE', 0, NULL, CURRENT_TIMESTAMP, 'AGENCY_MANAGER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'karim@speedcar.ma');

INSERT INTO agency_managers (id, phone, national_id, digital_signature, responsability_level, licence_number, approved_at, approved_by_admin_id, agency_id)
SELECT u.id, '+212622123456', 'BE123456', 'sig_karim_001', 1, 'LIC-001-2024', CURRENT_TIMESTAMP, (SELECT id FROM users WHERE email = 'admin@carrental.ma'), agency_casa
FROM users u
WHERE u.email = 'karim@speedcar.ma'
AND NOT EXISTS (SELECT 1 FROM agency_managers am WHERE am.id = u.id);

-- (Répéter pour autres managers...)

-- =====================================================
-- 7. RÉSERVATIONS (20 réservations avec différents statuts)
-- =====================================================

-- Réservations CONFIRMÉES (5)
INSERT INTO reservations (id, start_date, end_date, creation_date, total_amount, status, deposit_amount, pickup_location, return_location, client_id, car_id)
SELECT gen_random_uuid(), 
       CURRENT_DATE + INTERVAL '2 days', 
       CURRENT_DATE + INTERVAL '7 days', 
       CURRENT_TIMESTAMP - INTERVAL '3 days',
       1500, 'CONFIRMED', 300, 'Casablanca Centre', 'Casablanca Centre',
       (SELECT id FROM users WHERE email = 'ahmed.benali@gmail.com'),
       (SELECT id FROM cars WHERE registration_number = '1234-A-78' LIMIT 1);

INSERT INTO reservations (id, start_date, end_date, creation_date, total_amount, status, deposit_amount, pickup_location, return_location, client_id, car_id)
SELECT gen_random_uuid(),
       CURRENT_DATE + INTERVAL '5 days',
       CURRENT_DATE + INTERVAL '12 days',
       CURRENT_TIMESTAMP - INTERVAL '5 days',
       2800, 'CONFIRMED', 500, 'Rabat Centre', 'Rabat Centre',
       (SELECT id FROM users WHERE email = 'khadija.mouradi@yahoo.com'),
       (SELECT id FROM cars WHERE registration_number = '2223-I-67' LIMIT 1);

-- Réservations COMPLÉTÉES (5)
INSERT INTO reservations (id, start_date, end_date, creation_date, completed_at, total_amount, status, deposit_amount, pickup_location, return_location, actual_return_time, client_id, car_id)
SELECT gen_random_uuid(),
       CURRENT_DATE - INTERVAL '10 days',
       CURRENT_DATE - INTERVAL '3 days',
       CURRENT_TIMESTAMP - INTERVAL '15 days',
       CURRENT_TIMESTAMP - INTERVAL '3 days',
       2100, 'COMPLETED', 400, 'Marrakech Centre', 'Marrakech Centre',
       CURRENT_TIMESTAMP - INTERVAL '3 days',
       (SELECT id FROM users WHERE email = 'youssef.tazi@hotmail.com'),
       (SELECT id FROM cars WHERE registration_number = '2425-J-89' LIMIT 1);

-- Réservations CANCELLÉES (5)
INSERT INTO reservations (id, start_date, end_date, creation_date, total_amount, status, deposit_amount, pickup_location, return_location, cancellation_reason, client_id, car_id)
SELECT gen_random_uuid(),
       CURRENT_DATE + INTERVAL '15 days',
       CURRENT_DATE + INTERVAL '22 days',
       CURRENT_TIMESTAMP - INTERVAL '2 days',
       3600, 'CANCELLED', 700, 'Tanger Port', 'Tanger Port',
       'Changement de plans',
       (SELECT id FROM users WHERE email = 'nadia.chraibi@gmail.com'),
       (SELECT id FROM cars WHERE registration_number = '4849-V-23' LIMIT 1);

-- Réservations PENDING (5)
INSERT INTO reservations (id, start_date, end_date, creation_date, total_amount, status, deposit_amount, pickup_location, return_location, client_id, car_id)
SELECT gen_random_uuid(),
       CURRENT_DATE + INTERVAL '20 days',
       CURRENT_DATE + INTERVAL '27 days',
       CURRENT_TIMESTAMP,
       4200, 'PENDING', 800, 'Casablanca Airport', 'Casablanca Airport',
       (SELECT id FROM users WHERE email = 'leila.bennis@gmail.com'),
       (SELECT id FROM cars WHERE registration_number = '1617-F-01' LIMIT 1);

-- =====================================================
-- 8. CONTRATS
-- =====================================================

INSERT INTO contracts (id, contract_number, creation_date, start_date, end_date, status, total_amount, deposit_amount, payment_status, terms, signed_by_client, signed_by_agency, reservation_id)
SELECT gen_random_uuid(),
       'CTR-' || to_char(CURRENT_DATE, 'YYYY') || '-' || LPAD(ROW_NUMBER() OVER ()::TEXT, 4, '0'),
       r.creation_date, r.start_date, r.end_date,
       CASE WHEN r.status = 'COMPLETED' THEN 'EXPIRED' ELSE 'ACTIVE' END,
       r.total_amount, r.deposit_amount,
       CASE WHEN r.status IN ('CONFIRMED', 'COMPLETED') THEN 'PAID' ELSE 'PENDING' END,
       'Terms and conditions standard',
       true, true, r.id
FROM reservations r
WHERE NOT EXISTS (SELECT 1 FROM contracts c WHERE c.reservation_id = r.id);

-- =====================================================
-- 9. PAIEMENTS (Multiples par réservation)
-- =====================================================

-- Acomptes (deposits)
INSERT INTO payments (id, type, status, date_envoi, amount, reservation_id, client_id)
SELECT gen_random_uuid(), 'DEPOSIT', 'SUCCESS', r.creation_date, r.deposit_amount, r.id, r.client_id
FROM reservations r
WHERE r.status IN ('CONFIRMED', 'COMPLETED');

-- Paiements finaux
INSERT INTO payments (id, type, status, date_envoi, amount, reservation_id, client_id)
SELECT gen_random_uuid(), 'FULL', 'SUCCESS', r.start_date - INTERVAL '1 day', r.total_amount - r.deposit_amount, r.id, r.client_id
FROM reservations r
WHERE r.status = 'COMPLETED';

-- Paiements échoués
INSERT INTO payments (id, type, status, date_envoi, amount, reservation_id, client_id)
SELECT gen_random_uuid(), 'DEPOSIT', 'FAILED', r.creation_date + INTERVAL '1 day', r.deposit_amount, r.id, r.client_id
FROM reservations r
WHERE r.status = 'CANCELLED';

-- =====================================================
-- 10. NOTIFICATIONS
-- =====================================================

INSERT INTO notifications (id, type, contenu, date_envoi, statut, user_id)
SELECT gen_random_uuid(), 'RESERVATION_CONFIRMED', 'Votre réservation #' || LEFT(r.id::TEXT, 8) || ' a été confirmée', r.creation_date + INTERVAL '1 hour', 'DELIVERED', r.client_id
FROM reservations r
WHERE r.status = 'CONFIRMED';

INSERT INTO notifications (id, type, contenu, date_envoi, statut, user_id)
SELECT gen_random_uuid(), 'PAYMENT_RECEIVED', 'Paiement reçu pour la réservation #' || LEFT(r.id::TEXT, 8), p.date_envoi, 'DELIVERED', r.client_id
FROM reservations r
JOIN payments p ON p.reservation_id = r.id
WHERE p.type = 'FULL' AND p.status = 'SUCCESS';

-- =====================================================
-- 11. CONDITION REPORTS
-- =====================================================

-- Reports de prise en charge (PICKUP)
INSERT INTO condition_reports (report_number, date_environ, completed_at, comment, type, is_signed_by_customer, vehicle_status_before, vehicle_status_after, fuel_level_before, fuel_level_after, reservation_id, car_id)
SELECT 'CR-PICKUP-' || to_char(CURRENT_DATE, 'YYYYMMDD') || '-' || ROW_NUMBER() OVER (),
       r.start_date, r.start_date,
       'Véhicule en excellent état au départ',
       'PICKUP', true, 'Available', 'Rented', 'FULL', 'HALF',
       r.id, r.car_id
FROM reservations r
WHERE r.status = 'COMPLETED';

-- Reports de retour (RETURN)
INSERT INTO condition_reports (report_number, date_environ, completed_at, comment, type, is_signed_by_customer, vehicle_status_before, vehicle_status_after, fuel_level_before, fuel_level_after, reservation_id, car_id)
SELECT 'CR-RETURN-' || to_char(CURRENT_DATE, 'YYYYMMDD') || '-' || ROW_NUMBER() OVER (),
       r.end_date, r.end_date,
       CASE WHEN random() > 0.7 THEN 'Petites rayures sur la carrosserie' ELSE 'Retour en parfait état' END,
       'RETURN', true, 'Rented', 'Available', 'HALF', 'HALF',
       r.id, r.car_id
FROM reservations r
WHERE r.status = 'COMPLETED';

-- =====================================================
-- 12. EXECUTIONS
-- =====================================================

INSERT INTO executions (actual_return_time, actual_pickup_date, delay_in_days, status, reservation_id)
SELECT r.end_date + (random() * INTERVAL '2 days'),
       r.start_date,
       CASE WHEN r.end_date < CURRENT_DATE THEN 0 ELSE floor(random() * 3) END,
       CASE WHEN r.end_date < CURRENT_DATE THEN 'COMPLETED' ELSE 'ONGOING' END,
       r.id
FROM reservations r
WHERE r.status IN ('COMPLETED', 'CONFIRMED');

-- =====================================================
-- 13. SYSTEM MAINTENANCE
-- =====================================================

INSERT INTO system_maintenance (start_date_time, end_date_time, status, type, description, reason, agencies_notified, notification_days_before)
VALUES 
    (CURRENT_TIMESTAMP + INTERVAL '7 days', CURRENT_TIMESTAMP + INTERVAL '8 days', 'PLANNED', 'DATABASE_UPDATE', 'Mise à jour majeure de la base de données', 'Optimisation des performances', true, 7),
    (CURRENT_TIMESTAMP + INTERVAL '14 days', CURRENT_TIMESTAMP + INTERVAL '14 days 2 hours', 'PLANNED', 'SECURITY_PATCH', 'Patch de sécurité critique', 'Mise à jour de sécurité', true, 14),
    (CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '4 days', 'COMPLETED', 'BACKUP', 'Sauvegarde complète du système', 'Backup hebdomadaire', true, 2),
    (CURRENT_TIMESTAMP + INTERVAL '21 days', CURRENT_TIMESTAMP + INTERVAL '22 days', 'CANCELLED', 'SERVER_RESTART', 'Redémarrage du serveur', 'Planifié annulé', false, NULL),
    (CURRENT_TIMESTAMP + INTERVAL '30 days', CURRENT_TIMESTAMP + INTERVAL '30 days 4 hours', 'PLANNED', 'DEPLOYMENT', 'Déploiement de la version 2.0', 'Nouvelles fonctionnalités', true, 14),
    (CURRENT_TIMESTAMP + INTERVAL '1 day', CURRENT_TIMESTAMP + INTERVAL '1 day 1 hour', 'PLANNED', 'CERTIFICATE_RENEW', 'Renouvellement des certificats SSL', 'Expiration des certificats', true, 1);

-- =====================================================
-- 14. EMAIL VERIFICATION TOKENS
-- =====================================================

INSERT INTO email_verification_token (token, email, expires_at, created_at)
SELECT gen_random_uuid()::TEXT, u.email, CURRENT_TIMESTAMP + INTERVAL '7 days', CURRENT_TIMESTAMP
FROM users u
WHERE u.email_verified_at IS NULL
AND NOT EXISTS (SELECT 1 FROM email_verification_token ev WHERE ev.email = u.email);

-- =====================================================
-- 15. VUES POUR FILTRAGE (Optionnel)
-- =====================================================

-- Vue pour les voitures disponibles avec promotion
CREATE OR REPLACE VIEW available_cars_with_promo AS
SELECT c.*, a.name as agency_name, a.city, cat.name as category_name,
       CASE WHEN c.promotion_active = true THEN c.daily_price * (1 - c.promotion_rate/100) ELSE c.daily_price END as discounted_price
FROM cars c
JOIN agencies a ON c.agency_id = a.id
JOIN categories cat ON c.category_id = cat.id
WHERE c.current_status = 'Available'
AND (c.promotion_active = false OR (c.promotion_start_date <= CURRENT_DATE AND c.promotion_end_date >= CURRENT_DATE));

-- Vue pour les réservations avec détails
CREATE OR REPLACE VIEW reservation_details AS
SELECT r.id, r.start_date, r.end_date, r.status as reservation_status,
       r.total_amount, r.deposit_amount,
       u.email as client_email, u.first_name as client_first_name, u.last_name as client_last_name,
       c.mode as car_mode, c.brand as car_brand, c.registration_number,
       a.name as agency_name, a.city as agency_city,
       p.status as payment_status, p.amount as paid_amount,
       co.status as contract_status
FROM reservations r
JOIN users u ON r.client_id = u.id
JOIN cars c ON r.car_id = c.id
JOIN agencies a ON c.agency_id = a.id
LEFT JOIN payments p ON p.reservation_id = r.id AND p.type = 'DEPOSIT'
LEFT JOIN contracts co ON co.reservation_id = r.id;

-- Vue pour les statistiques par agence
CREATE OR REPLACE VIEW agency_statistics AS
SELECT a.id, a.name, a.city, a.rating,
       COUNT(DISTINCT c.id) as total_cars,
       COUNT(DISTINCT CASE WHEN c.current_status = 'Available' THEN c.id END) as available_cars,
       COUNT(DISTINCT CASE WHEN c.current_status = 'Rented' THEN c.id END) as rented_cars,
       COUNT(DISTINCT CASE WHEN c.current_status = 'Reserved' THEN c.id END) as reserved_cars,
       COUNT(DISTINCT r.id) as total_reservations,
       COUNT(DISTINCT CASE WHEN r.status = 'COMPLETED' THEN r.id END) as completed_reservations,
       COALESCE(SUM(CASE WHEN p.type = 'FULL' AND p.status = 'SUCCESS' THEN p.amount END), 0) as total_revenue
FROM agencies a
LEFT JOIN cars c ON c.agency_id = a.id
LEFT JOIN reservations r ON r.car_id = c.id
LEFT JOIN payments p ON p.reservation_id = r.id
GROUP BY a.id, a.name, a.city, a.rating;

END $$;

-- =====================================================
-- AFFICHAGE DES STATISTIQUES FINALES
-- =====================================================

SELECT '=== STATISTIQUES FINALES ===' as info;
SELECT 'Agences: ' || COUNT(*) as count FROM agencies;
SELECT 'Catégories: ' || COUNT(*) as count FROM categories;
SELECT 'Voitures: ' || COUNT(*) as count FROM cars;
SELECT 'Voitures par statut:' as info;
SELECT current_status, COUNT(*) FROM cars GROUP BY current_status;
SELECT 'Réservations par statut:' as info;
SELECT status, COUNT(*) FROM reservations GROUP BY status;
SELECT 'Paiements par statut:' as info;
SELECT status, COUNT(*) FROM payments GROUP BY status;
SELECT 'Utilisateurs par rôle:' as info;
SELECT role, COUNT(*) FROM users GROUP BY role;
SELECT 'Notre agence avec le plus de voitures:' as info;
SELECT a.name, COUNT(c.id) as car_count
FROM agencies a
LEFT JOIN cars c ON c.agency_id = a.id
GROUP BY a.name
ORDER BY car_count DESC
LIMIT 1;