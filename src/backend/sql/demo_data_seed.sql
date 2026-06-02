-- =====================================================
-- FILE: dev_seed_data.sql
-- AUTHOR: Your Team
-- DATE: 2026-06-02
-- PURPOSE: Seed data for development and testing
-- VERSION: 1.0.0
-- 
-- DESCRIPTION: 
--   Inserts test data including:
--   - 5 agencies (4 approved, 1 pending)
--   - 25+ cars with various statuses
--   - Users (admin, managers, clients)
--   - Reservations for testing filters
--
-- USAGE: 
--   psql -d database_name -f dev_seed_data.sql
--   OR run in Supabase SQL Editor
-- =====================================================

-- =====================================================
-- SCRIPT COMPLET POUR SUPABASE - AVEC TOUTES LES VOITURES
-- =====================================================

-- Activer l'extension UUID si nécessaire
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- =====================================================
-- 1. AGENCES
-- =====================================================

INSERT INTO agencies (id, name, city, phone, email, status, approval_date, inscription_date, rating, logo_url, iban, description, address)
VALUES 
    ('123e4567-e89b-12d3-a456-426614174001'::uuid, 'SpeedCar Maroc', 'Casablanca', '+212522123456', 'contact@speedcar.ma', 'APPROVED', CURRENT_DATE, CURRENT_DATE, 4.8, 'https://res.cloudinary.com/demo/image/upload/v1/logos/speedcar', 'MA6401234567890123456789', 'Location de voitures de luxe et économiques à Casablanca', '123 Bd Mohammed V, Casablanca')
ON CONFLICT (name) DO NOTHING;

INSERT INTO agencies (id, name, city, phone, email, status, approval_date, inscription_date, rating, logo_url, iban, description, address)
VALUES 
    ('123e4567-e89b-12d3-a456-426614174002'::uuid, 'Rabat Rent Car', 'Rabat', '+212537789012', 'contact@rabatrent.ma', 'APPROVED', CURRENT_DATE, CURRENT_DATE, 4.5, 'https://res.cloudinary.com/demo/image/upload/v1/logos/rabatrent', 'MA6409876543210987654321', 'Service premium à Rabat', '45 Avenue Mohamed V, Rabat')
ON CONFLICT (name) DO NOTHING;

INSERT INTO agencies (id, name, city, phone, email, status, approval_date, inscription_date, rating, logo_url, iban, description, address)
VALUES 
    ('123e4567-e89b-12d3-a456-426614174003'::uuid, 'Marrakech Car Rental', 'Marrakech', '+212524456789', 'contact@marrakechcar.ma', 'APPROVED', CURRENT_DATE, CURRENT_DATE, 4.9, 'https://res.cloudinary.com/demo/image/upload/v1/logos/marrakechcar', 'MA6405556667778889990001', 'Location de voitures à Marrakech - Prix compétitifs', '789 Rue de la Koutoubia, Marrakech')
ON CONFLICT (name) DO NOTHING;

INSERT INTO agencies (id, name, city, phone, email, status, approval_date, inscription_date, rating, logo_url, iban, description, address)
VALUES 
    ('123e4567-e89b-12d3-a456-426614174004'::uuid, 'Tangier Drive', 'Tanger', '+212539345678', 'contact@tangierdrive.ma', 'APPROVED', CURRENT_DATE, CURRENT_DATE, 4.3, 'https://res.cloudinary.com/demo/image/upload/v1/logos/tangierdrive', 'MA6401112223334445556667', 'Location de voitures à Tanger', '321 Avenue des FAR, Tanger')
ON CONFLICT (name) DO NOTHING;

INSERT INTO agencies (id, name, city, phone, email, status, approval_date, inscription_date, rating, logo_url, iban, description, address)
VALUES 
    ('123e4567-e89b-12d3-a456-426614174005'::uuid, 'Fes Auto Location', 'Fès', '+212535901234', 'contact@fesauto.ma', 'PENDING', NULL, CURRENT_DATE, NULL, 'https://res.cloudinary.com/demo/image/upload/v1/logos/fesauto', 'MA6409998887776665554443', 'Agence en attente d approbation', '567 Rue Talaa Kebira, Fès')
ON CONFLICT (name) DO NOTHING;

-- =====================================================
-- 2. CATÉGORIES
-- =====================================================

INSERT INTO categories (id, name, description)
SELECT gen_random_uuid(), 'Economique', 'Petites voitures économiques - Parfaites pour la ville'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Economique');

INSERT INTO categories (id, name, description)
SELECT gen_random_uuid(), 'Compacte', 'Voitures compactes - Idéal pour les trajets courts et moyens'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Compacte');

INSERT INTO categories (id, name, description)
SELECT gen_random_uuid(), 'SUV', 'SUV et 4x4 - Pour les familles et les aventures'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'SUV');

INSERT INTO categories (id, name, description)
SELECT gen_random_uuid(), 'Luxe', 'Voitures de luxe - Confort et élégance'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Luxe');

INSERT INTO categories (id, name, description)
SELECT gen_random_uuid(), 'Utilitaire', 'Vans et utilitaires - Pour les grands volumes'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Utilitaire');

-- =====================================================
-- 3. VOITURES (25+ VOITURES)
-- =====================================================

DO $$
DECLARE
    cat_eco UUID;
    cat_compact UUID;
    cat_suv UUID;
    cat_luxe UUID;
    cat_util UUID;
    agency_casa UUID := '123e4567-e89b-12d3-a456-426614174001'::uuid;
    agency_rabat UUID := '123e4567-e89b-12d3-a456-426614174002'::uuid;
    agency_marrakech UUID := '123e4567-e89b-12d3-a456-426614174003'::uuid;
    agency_tanger UUID := '123e4567-e89b-12d3-a456-426614174004'::uuid;
    agency_fes UUID := '123e4567-e89b-12d3-a456-426614174005'::uuid;
BEGIN
    -- Récupérer les IDs des catégories
    SELECT id INTO cat_eco FROM categories WHERE name = 'Economique';
    SELECT id INTO cat_compact FROM categories WHERE name = 'Compacte';
    SELECT id INTO cat_suv FROM categories WHERE name = 'SUV';
    SELECT id INTO cat_luxe FROM categories WHERE name = 'Luxe';
    SELECT id INTO cat_util FROM categories WHERE name = 'Utilitaire';

-- =====================================================
-- SPEEDCAR MAROC - CASABLANCA (8 voitures)
-- =====================================================

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
VALUES 
    (gen_random_uuid(), '208', 'Peugeot', 2023, 'MANUAL', 15000, 'Available', 250, 1500, 5000, false, NULL, NULL, NULL, 'EXCELLENT', '2026-12-31', '2026-06-30', '1234-A-78', 5, 'Peugeot 208 neuve - Économique et fiable', agency_casa, cat_eco),
    (gen_random_uuid(), 'Clio', 'Renault', 2024, 'MANUAL', 5000, 'Available', 230, 1400, 4800, true, 15, CURRENT_DATE, '2026-07-31', 'EXCELLENT', '2027-12-31', '2026-12-31', '5678-B-12', 5, 'Renault Clio - Promotion été 15%', agency_casa, cat_eco),
    (gen_random_uuid(), 'Duster', 'Dacia', 2022, 'MANUAL', 45000, 'Reserved', 400, 2400, 8000, false, NULL, NULL, NULL, 'GOOD', '2026-10-31', '2026-05-30', '9101-C-45', 5, 'Dacia Duster - SUV robuste pour routes marocaines', agency_casa, cat_suv),
    (gen_random_uuid(), 'Megane', 'Renault', 2023, 'AUTOMATIC', 8000, 'Available', 350, 2100, 7000, false, NULL, NULL, NULL, 'EXCELLENT', '2027-01-31', '2026-08-31', '1213-D-67', 5, 'Renault Megane - Confort et élégance', agency_casa, cat_compact),
    (gen_random_uuid(), '5008', 'Peugeot', 2023, 'AUTOMATIC', 5000, 'Rented', 550, 3300, 11000, false, NULL, NULL, NULL, 'EXCELLENT', '2027-02-28', '2026-09-30', '1415-E-89', 7, 'Peugeot 5008 - Idéal pour les familles', agency_casa, cat_suv),
    (gen_random_uuid(), 'Classe A', 'Mercedes', 2024, 'AUTOMATIC', 2000, 'Available', 1200, 7200, 24000, true, 20, CURRENT_DATE, '2026-08-31', 'EXCELLENT', '2028-12-31', '2027-06-30', '1617-F-01', 5, 'Mercedes Classe A - Luxe et performance', agency_casa, cat_luxe),
    (gen_random_uuid(), 'Captur', 'Renault', 2024, 'AUTOMATIC', 3000, 'AvailableSoon', 380, 2280, 7600, false, NULL, NULL, NULL, 'EXCELLENT', '2027-11-30', '2026-11-30', '1819-G-23', 5, 'Renault Captur - Style urbain', agency_casa, cat_compact),
    (gen_random_uuid(), 'Hilux', 'Toyota', 2021, 'MANUAL', 85000, 'Maintenance', 500, 3000, 10000, false, NULL, NULL, NULL, 'FAIR', '2026-09-30', '2026-05-15', '2021-H-45', 5, 'Toyota Hilux - Robustesse marocaine', agency_casa, cat_suv);

-- =====================================================
-- RABAT RENT CAR (6 voitures)
-- =====================================================

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
VALUES 
    (gen_random_uuid(), 'i10', 'Hyundai', 2023, 'MANUAL', 18000, 'Available', 220, 1300, 4500, false, NULL, NULL, NULL, 'EXCELLENT', '2026-12-31', '2026-07-31', '2223-I-67', 5, 'Hyundai i10 - Économique et agile', agency_rabat, cat_eco),
    (gen_random_uuid(), 'Civic', 'Honda', 2023, 'AUTOMATIC', 12000, 'Available', 450, 2700, 9000, true, 10, CURRENT_DATE, '2026-09-30', 'EXCELLENT', '2027-06-30', '2026-12-31', '2425-J-89', 5, 'Honda Civic - Fiable et sportive', agency_rabat, cat_compact),
    (gen_random_uuid(), 'X1', 'BMW', 2024, 'AUTOMATIC', 1000, 'Available', 950, 5700, 19000, false, NULL, NULL, NULL, 'EXCELLENT', '2028-12-31', '2027-06-30', '2627-K-01', 5, 'BMW X1 - SUV premium', agency_rabat, cat_luxe),
    (gen_random_uuid(), 'C3', 'Citroen', 2022, 'MANUAL', 35000, 'Reserved', 200, 1200, 4000, false, NULL, NULL, NULL, 'GOOD', '2026-11-30', '2026-06-15', '2829-L-23', 5, 'Citroen C3 - Originale et confortable', agency_rabat, cat_eco),
    (gen_random_uuid(), 'Kangoo', 'Renault', 2023, 'MANUAL', 25000, 'Available', 300, 1800, 6000, false, NULL, NULL, NULL, 'GOOD', '2027-03-31', '2026-09-30', '3031-M-45', 7, 'Renault Kangoo - Utilitaire spacieux', agency_rabat, cat_suv),
    (gen_random_uuid(), 'Fiesta', 'Ford', 2024, 'MANUAL', 4000, 'Available', 260, 1560, 5200, true, 12, CURRENT_DATE, '2026-10-31', 'EXCELLENT', '2027-12-31', '2027-06-30', '3243-N-67', 5, 'Ford Fiesta - Dynamique', agency_rabat, cat_eco);

-- =====================================================
-- MARRAKECH CAR RENTAL (6 voitures)
-- =====================================================

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
VALUES 
    (gen_random_uuid(), 'A3', 'Audi', 2024, 'AUTOMATIC', 2000, 'Available', 800, 4800, 16000, false, NULL, NULL, NULL, 'EXCELLENT', '2028-12-31', '2027-06-30', '3456-O-89', 5, 'Audi A3 - Luxe accessible', agency_marrakech, cat_luxe),
    (gen_random_uuid(), 'Corsa', 'Opel', 2022, 'MANUAL', 35000, 'AvailableSoon', 240, 1440, 4800, false, NULL, NULL, NULL, 'GOOD', '2026-11-30', '2026-06-15', '3637-P-01', 5, 'Opel Corsa - Fiable', agency_marrakech, cat_eco),
    (gen_random_uuid(), 'Qashqai', 'Nissan', 2023, 'AUTOMATIC', 15000, 'Rented', 500, 3000, 10000, false, NULL, NULL, NULL, 'EXCELLENT', '2027-08-31', '2026-08-31', '3839-Q-23', 5, 'Nissan Qashqai - SUV familial', agency_marrakech, cat_suv),
    (gen_random_uuid(), 'Polo', 'Volkswagen', 2024, 'MANUAL', 3000, 'Available', 280, 1680, 5600, true, 10, CURRENT_DATE, '2026-09-30', 'EXCELLENT', '2027-12-31', '2027-03-31', '4041-R-45', 5, 'Volkswagen Polo - Qualité allemande', agency_marrakech, cat_compact),
    (gen_random_uuid(), 'C4', 'Citroen', 2023, 'AUTOMATIC', 20000, 'Maintenance', 350, 2100, 7000, false, NULL, NULL, NULL, 'FAIR', '2027-05-31', '2026-05-31', '4243-S-67', 5, 'Citroen C4 - Confort', agency_marrakech, cat_compact),
    (gen_random_uuid(), 'Serie 3', 'BMW', 2024, 'AUTOMATIC', 1000, 'Available', 1100, 6600, 22000, false, NULL, NULL, NULL, 'EXCELLENT', '2029-12-31', '2027-12-31', '4445-T-89', 5, 'BMW Serie 3 - Performance', agency_marrakech, cat_luxe);

-- =====================================================
-- TANGIER DRIVE (6 voitures)
-- =====================================================

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
VALUES 
    (gen_random_uuid(), 'Picanto', 'Kia', 2023, 'MANUAL', 7000, 'Available', 210, 1260, 4200, true, 12, CURRENT_DATE, '2026-09-30', 'EXCELLENT', '2027-12-31', '2026-12-31', '4647-U-01', 5, 'Kia Picanto - Petite citadine économique', agency_tanger, cat_eco),
    (gen_random_uuid(), 'Tucson', 'Hyundai', 2023, 'AUTOMATIC', 12000, 'Reserved', 600, 3600, 12000, false, NULL, NULL, NULL, 'EXCELLENT', '2027-01-31', '2026-08-31', '4849-V-23', 5, 'Hyundai Tucson - SUV moderne', agency_tanger, cat_suv),
    (gen_random_uuid(), 'Rio', 'Kia', 2022, 'MANUAL', 40000, 'Available', 220, 1320, 4400, false, NULL, NULL, NULL, 'GOOD', '2026-10-31', '2026-05-31', '5051-W-45', 5, 'Kia Rio - Économique', agency_tanger, cat_eco),
    (gen_random_uuid(), 'Leon', 'Seat', 2023, 'AUTOMATIC', 18000, 'Available', 320, 1920, 6400, false, NULL, NULL, NULL, 'EXCELLENT', '2027-04-30', '2026-10-31', '5253-X-67', 5, 'Seat Leon - Sportive', agency_tanger, cat_compact),
    (gen_random_uuid(), 'Sportage', 'Kia', 2024, 'AUTOMATIC', 5000, 'Available', 550, 3300, 11000, true, 15, CURRENT_DATE, '2026-11-30', 'EXCELLENT', '2028-12-31', '2027-06-30', '5455-Y-89', 5, 'Kia Sportage - SUV premium', agency_tanger, cat_suv),
    (gen_random_uuid(), 'i20', 'Hyundai', 2023, 'MANUAL', 12000, 'Available', 240, 1440, 4800, false, NULL, NULL, NULL, 'EXCELLENT', '2027-06-30', '2026-12-31', '5657-Z-01', 5, 'Hyundai i20 - Moderne', agency_tanger, cat_eco);

-- =====================================================
-- FES AUTO LOCATION (3 voitures - agence en attente)
-- =====================================================

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
VALUES 
    (gen_random_uuid(), 'Logan', 'Dacia', 2022, 'MANUAL', 50000, 'Available', 200, 1200, 4000, false, NULL, NULL, NULL, 'GOOD', '2026-09-30', '2026-06-30', '5858-A-23', 5, 'Dacia Logan - Économique', agency_fes, cat_eco),
    (gen_random_uuid(), 'Sandero', 'Dacia', 2023, 'MANUAL', 25000, 'Available', 220, 1320, 4400, false, NULL, NULL, NULL, 'GOOD', '2027-03-31', '2026-09-30', '6060-B-45', 5, 'Dacia Sandero - Populaire', agency_fes, cat_eco),
    (gen_random_uuid(), 'Lodgy', 'Dacia', 2022, 'MANUAL', 60000, 'Available', 350, 2100, 7000, false, NULL, NULL, NULL, 'FAIR', '2026-08-31', '2026-05-31', '6262-C-67', 7, 'Dacia Lodgy - Familial', agency_fes, cat_suv);

END $$;

-- =====================================================
-- 4. CAR_FUEL_TYPES
-- =====================================================

INSERT INTO car_fuel_types (car_id, fuel_type)
SELECT c.id, 'GASOLINE' 
FROM cars c 
WHERE c.brand IN ('Peugeot', 'Renault', 'Citroen', 'Ford', 'Opel', 'Seat', 'Kia', 'Hyundai') 
AND c.mode NOT IN ('Duster', '5008', 'Hilux', 'Kangoo', 'Qashqai', 'Tucson', 'Sportage', 'Lodgy')
ON CONFLICT DO NOTHING;

INSERT INTO car_fuel_types (car_id, fuel_type)
SELECT c.id, 'DIESEL' 
FROM cars c 
WHERE c.mode IN ('Duster', '5008', 'Hilux', 'Kangoo', 'Qashqai', 'Tucson', 'Sportage', 'Lodgy')
ON CONFLICT DO NOTHING;

INSERT INTO car_fuel_types (car_id, fuel_type)
SELECT c.id, 'HYBRID' 
FROM cars c 
WHERE c.brand IN ('Toyota', 'Honda')
ON CONFLICT DO NOTHING;

-- =====================================================
-- 5. PHOTOS
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
INSERT INTO users (id, first_name, last_name, email, password, password_updated_at, inscription_date, role, account_status, failed_login_attempts, email_verified_at, user_type)
VALUES (gen_random_uuid(), 'Youssef', 'Admin', 'admin@carrental.ma', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'SUPER_ADMIN', 'ACTIVE', 0, CURRENT_TIMESTAMP, 'SUPER_ADMIN')
ON CONFLICT (email) DO NOTHING;

INSERT INTO admins (id, permissions, admin_level)
SELECT u.id, 'ALL', 1
FROM users u
WHERE u.email = 'admin@carrental.ma'
ON CONFLICT (id) DO NOTHING;

-- AGENCY MANAGERS
INSERT INTO users (id, first_name, last_name, email, password, password_updated_at, inscription_date, role, account_status, failed_login_attempts, email_verified_at, user_type)
VALUES 
    (gen_random_uuid(), 'Karim', 'Benjelloun', 'karim@speedcar.ma', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'AGENCY_MANAGER', 'ACTIVE', 0, CURRENT_TIMESTAMP, 'AGENCY_MANAGER'),
    (gen_random_uuid(), 'Fatima', 'Zahra', 'fatima@rabatrent.ma', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'AGENCY_MANAGER', 'ACTIVE', 0, CURRENT_TIMESTAMP, 'AGENCY_MANAGER'),
    (gen_random_uuid(), 'Mohamed', 'El Mansouri', 'mohamed@marrakechcar.ma', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'AGENCY_MANAGER', 'ACTIVE', 0, CURRENT_TIMESTAMP, 'AGENCY_MANAGER'),
    (gen_random_uuid(), 'Sofia', 'El Amrani', 'sofia@tangierdrive.ma', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'AGENCY_MANAGER', 'ACTIVE', 0, CURRENT_TIMESTAMP, 'AGENCY_MANAGER'),
    (gen_random_uuid(), 'Hassan', 'Fassi', 'hassan@fesauto.ma', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'AGENCY_MANAGER', 'PENDING', 0, NULL, 'AGENCY_MANAGER')
ON CONFLICT (email) DO NOTHING;

INSERT INTO agency_managers (id, phone, national_id, digital_signature, responsability_level, licence_number, approved_at, approved_by_admin_id, agency_id)
SELECT u.id, '+212622123456', 'BE123456', 'sig_karim_001', 1, 'LIC-001-2024', CURRENT_TIMESTAMP, (SELECT id FROM users WHERE email = 'admin@carrental.ma'), '123e4567-e89b-12d3-a456-426614174001'::uuid
FROM users u WHERE u.email = 'karim@speedcar.ma'
ON CONFLICT (id) DO NOTHING;

INSERT INTO agency_managers (id, phone, national_id, digital_signature, responsability_level, licence_number, approved_at, approved_by_admin_id, agency_id)
SELECT u.id, '+212637890123', 'ZA789012', 'sig_fatima_002', 1, 'LIC-002-2024', CURRENT_TIMESTAMP, (SELECT id FROM users WHERE email = 'admin@carrental.ma'), '123e4567-e89b-12d3-a456-426614174002'::uuid
FROM users u WHERE u.email = 'fatima@rabatrent.ma'
ON CONFLICT (id) DO NOTHING;

INSERT INTO agency_managers (id, phone, national_id, digital_signature, responsability_level, licence_number, approved_at, approved_by_admin_id, agency_id)
SELECT u.id, '+212661456789', 'EL456123', 'sig_mohamed_003', 1, 'LIC-003-2024', CURRENT_TIMESTAMP, (SELECT id FROM users WHERE email = 'admin@carrental.ma'), '123e4567-e89b-12d3-a456-426614174003'::uuid
FROM users u WHERE u.email = 'mohamed@marrakechcar.ma'
ON CONFLICT (id) DO NOTHING;

INSERT INTO agency_managers (id, phone, national_id, digital_signature, responsability_level, licence_number, approved_at, approved_by_admin_id, agency_id)
SELECT u.id, '+212639567890', 'AM789345', 'sig_sofia_004', 1, 'LIC-004-2024', CURRENT_TIMESTAMP, (SELECT id FROM users WHERE email = 'admin@carrental.ma'), '123e4567-e89b-12d3-a456-426614174004'::uuid
FROM users u WHERE u.email = 'sofia@tangierdrive.ma'
ON CONFLICT (id) DO NOTHING;

INSERT INTO agency_managers (id, phone, national_id, digital_signature, responsability_level, licence_number, approved_at, approved_by_admin_id, agency_id)
SELECT u.id, '+212678901234', 'FA901234', 'sig_hassan_005', 1, 'LIC-005-2024', NULL, NULL, '123e4567-e89b-12d3-a456-426614174005'::uuid
FROM users u WHERE u.email = 'hassan@fesauto.ma'
ON CONFLICT (id) DO NOTHING;

-- CLIENTS (10 clients)
INSERT INTO users (id, first_name, last_name, email, password, password_updated_at, inscription_date, role, account_status, failed_login_attempts, email_verified_at, user_type)
VALUES 
    (gen_random_uuid(), 'Ahmed', 'Benali', 'ahmed.benali@gmail.com', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'CLIENT', 'ACTIVE', 0, CURRENT_TIMESTAMP, 'CLIENT'),
    (gen_random_uuid(), 'Khadija', 'Mouradi', 'khadija.mouradi@yahoo.com', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'CLIENT', 'ACTIVE', 0, CURRENT_TIMESTAMP, 'CLIENT'),
    (gen_random_uuid(), 'Youssef', 'Tazi', 'youssef.tazi@hotmail.com', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'CLIENT', 'ACTIVE', 0, CURRENT_TIMESTAMP, 'CLIENT'),
    (gen_random_uuid(), 'Nadia', 'Chraibi', 'nadia.chraibi@gmail.com', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'CLIENT', 'ACTIVE', 0, CURRENT_TIMESTAMP, 'CLIENT'),
    (gen_random_uuid(), 'Omar', 'Idrissi', 'omar.idrissi@yahoo.com', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'CLIENT', 'SUSPENDED', 5, NULL, 'CLIENT'),
    (gen_random_uuid(), 'Leila', 'Bennis', 'leila.bennis@gmail.com', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'CLIENT', 'ACTIVE', 0, CURRENT_TIMESTAMP, 'CLIENT'),
    (gen_random_uuid(), 'Hamid', 'El Fassi', 'hamid.elfassi@hotmail.com', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'CLIENT', 'ACTIVE', 0, CURRENT_TIMESTAMP, 'CLIENT'),
    (gen_random_uuid(), 'Samira', 'Mernissi', 'samira.mernissi@gmail.com', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'CLIENT', 'ACTIVE', 0, CURRENT_TIMESTAMP, 'CLIENT'),
    (gen_random_uuid(), 'Rachid', 'Ouazzani', 'rachid.ouazzani@yahoo.com', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'CLIENT', 'LOCKED', 10, NULL, 'CLIENT'),
    (gen_random_uuid(), 'Mona', 'El Khayat', 'mona.elkhayat@gmail.com', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'CLIENT', 'ACTIVE', 0, CURRENT_TIMESTAMP, 'CLIENT')
ON CONFLICT (email) DO NOTHING;

-- Ajouter les détails clients
INSERT INTO clients (id, licence_number, location, licence_expiration_date, national_id, phone, digital_signature)
SELECT u.id, 'B123456', 'Casablanca', '2028-12-31', 'AB123456', '+212612345678', 'sig_ahmed_001'
FROM users u WHERE u.email = 'ahmed.benali@gmail.com'
ON CONFLICT (id) DO NOTHING;

INSERT INTO clients (id, licence_number, location, licence_expiration_date, national_id, phone, digital_signature)
SELECT u.id, 'C789012', 'Rabat', '2029-06-30', 'KM789012', '+212678901234', 'sig_khadija_002'
FROM users u WHERE u.email = 'khadija.mouradi@yahoo.com'
ON CONFLICT (id) DO NOTHING;

-- =====================================================
-- 7. RÉSERVATIONS
-- =====================================================

INSERT INTO reservations (id, start_date, end_date, creation_date, total_amount, status, deposit_amount, pickup_location, return_location, client_id, car_id)
SELECT gen_random_uuid(), 
       CURRENT_DATE + INTERVAL '2 days', 
       CURRENT_DATE + INTERVAL '7 days', 
       CURRENT_TIMESTAMP - INTERVAL '3 days',
       1500, 'CONFIRMED', 300, 'Casablanca Centre', 'Casablanca Centre',
       (SELECT id FROM users WHERE email = 'ahmed.benali@gmail.com'),
       (SELECT id FROM cars WHERE registration_number = '1234-A-78' LIMIT 1)
WHERE EXISTS (SELECT 1 FROM cars WHERE registration_number = '1234-A-78');

-- =====================================================
-- 8. STATISTIQUES FINALES
-- =====================================================

SELECT '=== STATISTIQUES ===' as info;
SELECT 'Agences: ' || COUNT(*) as count FROM agencies;
SELECT 'Catégories: ' || COUNT(*) as count FROM categories;
SELECT 'Voitures: ' || COUNT(*) as count FROM cars;
SELECT 'Voitures par statut:' as info;
SELECT current_status, COUNT(*) FROM cars GROUP BY current_status;
SELECT 'Utilisateurs par rôle:' as info;
SELECT role, COUNT(*) FROM users GROUP BY role;