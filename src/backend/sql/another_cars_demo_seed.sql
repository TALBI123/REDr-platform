-- =====================================================
-- demo_data_seed.sql - Version corrigée
-- =====================================================

-- Activer l'extension UUID si nécessaire
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- =====================================================
-- 1. AGENCES (avec suppression des anciennes données si nécessaire)
-- =====================================================

-- Supprimer les données existantes (optionnel - pour réinitialiser)
-- TRUNCATE TABLE agencies CASCADE;

INSERT INTO agencies (id, name, city, phone, email, status, approval_date, inscription_date, rating, logo_url, iban, description, address)
VALUES 
    ('123e4567-e89b-12d3-a456-426614174001'::uuid, 'SpeedCar Maroc', 'Casablanca', '+212522123456', 'contact@speedcar.ma', 'APPROVED', CURRENT_DATE, CURRENT_DATE, 4.8, 'https://res.cloudinary.com/demo/image/upload/v1/logos/speedcar', 'MA6401234567890123456789', 'Location de voitures de luxe et économiques à Casablanca', '123 Bd Mohammed V, Casablanda')
ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name;

INSERT INTO agencies (id, name, city, phone, email, status, approval_date, inscription_date, rating, logo_url, iban, description, address)
VALUES 
    ('123e4567-e89b-12d3-a456-426614174002'::uuid, 'Rabat Rent Car', 'Rabat', '+212537789012', 'contact@rabatrent.ma', 'APPROVED', CURRENT_DATE, CURRENT_DATE, 4.5, 'https://res.cloudinary.com/demo/image/upload/v1/logos/rabatrent', 'MA6409876543210987654321', 'Service premium à Rabat', '45 Avenue Mohamed V, Rabat')
ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name;

INSERT INTO agencies (id, name, city, phone, email, status, approval_date, inscription_date, rating, logo_url, iban, description, address)
VALUES 
    ('123e4567-e89b-12d3-a456-426614174003'::uuid, 'Marrakech Car Rental', 'Marrakech', '+212524456789', 'contact@marrakechcar.ma', 'APPROVED', CURRENT_DATE, CURRENT_DATE, 4.9, 'https://res.cloudinary.com/demo/image/upload/v1/logos/marrakechcar', 'MA6405556667778889990001', 'Location de voitures à Marrakech - Prix compétitifs', '789 Rue de la Koutoubia, Marrakech')
ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name;

INSERT INTO agencies (id, name, city, phone, email, status, approval_date, inscription_date, rating, logo_url, iban, description, address)
VALUES 
    ('123e4567-e89b-12d3-a456-426614174004'::uuid, 'Tangier Drive', 'Tanger', '+212539345678', 'contact@tangierdrive.ma', 'APPROVED', CURRENT_DATE, CURRENT_DATE, 4.3, 'https://res.cloudinary.com/demo/image/upload/v1/logos/tangierdrive', 'MA6401112223334445556667', 'Location de voitures à Tanger', '321 Avenue des FAR, Tanger')
ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name;

INSERT INTO agencies (id, name, city, phone, email, status, approval_date, inscription_date, rating, logo_url, iban, description, address)
VALUES 
    ('123e4567-e89b-12d3-a456-426614174005'::uuid, 'Fes Auto Location', 'Fès', '+212535901234', 'contact@fesauto.ma', 'PENDING', NULL, CURRENT_DATE, NULL, 'https://res.cloudinary.com/demo/image/upload/v1/logos/fesauto', 'MA6409998887776665554443', 'Agence en attente d approbation', '567 Rue Talaa Kebira, Fès')
ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name;

-- =====================================================
-- 2. CATÉGORIES (version simplifiée sans conflit)
-- =====================================================

-- Supprimer les catégories existantes (optionnel)
-- TRUNCATE TABLE categories CASCADE;

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
-- 3. NETTOYAGE AVANT INSERTION (optionnel)
-- =====================================================

-- Supprimer les anciennes données liées (si vous voulez repartir de zéro)
-- DELETE FROM car_fuel_types;
-- DELETE FROM photos;
-- DELETE FROM cars;
-- DELETE FROM categories WHERE name IN ('Economique', 'Compacte', 'SUV', 'Luxe', 'Utilitaire');
-- DELETE FROM agencies WHERE name LIKE '%Maroc%' OR name LIKE '%Rent%';

-- =====================================================
-- 4. VOITURES (sans ON CONFLICT pour éviter les erreurs)
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

    -- Vérifier si les catégories existent
    IF cat_eco IS NULL OR cat_compact IS NULL OR cat_suv IS NULL OR cat_luxe IS NULL THEN
        RAISE NOTICE 'Catégories non trouvées, veuillez dabord insérer les catégories';
        RETURN;
    END IF;

-- Supprimer les anciennes voitures (optionnel)
-- DELETE FROM cars WHERE agency_id IN (agency_casa, agency_rabat, agency_marrakech, agency_tanger, agency_fes);

-- =====================================================
-- SPEEDCAR MAROC - CASABLANCA (8 voitures)
-- =====================================================

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), '208', 'Peugeot', 2023, 'MANUAL', 15000, 'Available', 250, 1500, 5000, false, NULL, NULL, NULL, 'EXCELLENT', '2026-12-31', '2026-06-30', '1234-A-78', 5, 'Peugeot 208 neuve - Économique et fiable', agency_casa, cat_eco
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '1234-A-78');

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Clio', 'Renault', 2024, 'MANUAL', 5000, 'Available', 230, 1400, 4800, true, 15, CURRENT_DATE, '2026-07-31', 'EXCELLENT', '2027-12-31', '2026-12-31', '5678-B-12', 5, 'Renault Clio - Promotion été 15%', agency_casa, cat_eco
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '5678-B-12');

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Duster', 'Dacia', 2022, 'MANUAL', 45000, 'Reserved', 400, 2400, 8000, false, NULL, NULL, NULL, 'GOOD', '2026-10-31', '2026-05-30', '9101-C-45', 5, 'Dacia Duster - SUV robuste pour routes marocaines', agency_casa, cat_suv
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '9101-C-45');

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Megane', 'Renault', 2023, 'AUTOMATIC', 8000, 'Available', 350, 2100, 7000, false, NULL, NULL, NULL, 'EXCELLENT', '2027-01-31', '2026-08-31', '1213-D-67', 5, 'Renault Megane - Confort et élégance', agency_casa, cat_compact
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '1213-D-67');

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), '5008', 'Peugeot', 2023, 'AUTOMATIC', 5000, 'Rented', 550, 3300, 11000, false, NULL, NULL, NULL, 'EXCELLENT', '2027-02-28', '2026-09-30', '1415-E-89', 7, 'Peugeot 5008 - Idéal pour les familles', agency_casa, cat_suv
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '1415-E-89');

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Classe A', 'Mercedes', 2024, 'AUTOMATIC', 2000, 'Available', 1200, 7200, 24000, true, 20, CURRENT_DATE, '2026-08-31', 'EXCELLENT', '2028-12-31', '2027-06-30', '1617-F-01', 5, 'Mercedes Classe A - Luxe et performance', agency_casa, cat_luxe
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '1617-F-01');

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Captur', 'Renault', 2024, 'AUTOMATIC', 3000, 'AvailableSoon', 380, 2280, 7600, false, NULL, NULL, NULL, 'EXCELLENT', '2027-11-30', '2026-11-30', '1819-G-23', 5, 'Renault Captur - Style urbain', agency_casa, cat_compact
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '1819-G-23');

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Hilux', 'Toyota', 2021, 'MANUAL', 85000, 'Maintenance', 500, 3000, 10000, false, NULL, NULL, NULL, 'FAIR', '2026-09-30', '2026-05-15', '2021-H-45', 5, 'Toyota Hilux - Robustesse marocaine', agency_casa, cat_suv
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '2021-H-45');

-- =====================================================
-- RABAT RENT CAR (6 voitures)
-- =====================================================

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'i10', 'Hyundai', 2023, 'MANUAL', 18000, 'Available', 220, 1300, 4500, false, NULL, NULL, NULL, 'EXCELLENT', '2026-12-31', '2026-07-31', '2223-I-67', 5, 'Hyundai i10 - Économique et agile', agency_rabat, cat_eco
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '2223-I-67');

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Civic', 'Honda', 2023, 'AUTOMATIC', 12000, 'Available', 450, 2700, 9000, true, 10, CURRENT_DATE, '2026-09-30', 'EXCELLENT', '2027-06-30', '2026-12-31', '2425-J-89', 5, 'Honda Civic - Fiable et sportive', agency_rabat, cat_compact
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '2425-J-89');

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'X1', 'BMW', 2024, 'AUTOMATIC', 1000, 'Available', 950, 5700, 19000, false, NULL, NULL, NULL, 'EXCELLENT', '2028-12-31', '2027-06-30', '2627-K-01', 5, 'BMW X1 - SUV premium', agency_rabat, cat_luxe
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '2627-K-01');

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'C3', 'Citroen', 2022, 'MANUAL', 35000, 'Reserved', 200, 1200, 4000, false, NULL, NULL, NULL, 'GOOD', '2026-11-30', '2026-06-15', '2829-L-23', 5, 'Citroen C3 - Originale et confortable', agency_rabat, cat_eco
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '2829-L-23');

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Kangoo', 'Renault', 2023, 'MANUAL', 25000, 'Available', 300, 1800, 6000, false, NULL, NULL, NULL, 'GOOD', '2027-03-31', '2026-09-30', '3031-M-45', 7, 'Renault Kangoo - Utilitaire spacieux', agency_rabat, cat_suv
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '3031-M-45');

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Fiesta', 'Ford', 2024, 'MANUAL', 4000, 'Available', 260, 1560, 5200, true, 12, CURRENT_DATE, '2026-10-31', 'EXCELLENT', '2027-12-31', '2027-06-30', '3243-N-67', 5, 'Ford Fiesta - Dynamique', agency_rabat, cat_eco
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '3243-N-67');

-- =====================================================
-- MARRAKECH CAR RENTAL (6 voitures)
-- =====================================================

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'A3', 'Audi', 2024, 'AUTOMATIC', 2000, 'Available', 800, 4800, 16000, false, NULL, NULL, NULL, 'EXCELLENT', '2028-12-31', '2027-06-30', '3456-O-89', 5, 'Audi A3 - Luxe accessible', agency_marrakech, cat_luxe
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '3456-O-89');

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Corsa', 'Opel', 2022, 'MANUAL', 35000, 'AvailableSoon', 240, 1440, 4800, false, NULL, NULL, NULL, 'GOOD', '2026-11-30', '2026-06-15', '3637-P-01', 5, 'Opel Corsa - Fiable', agency_marrakech, cat_eco
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '3637-P-01');

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Qashqai', 'Nissan', 2023, 'AUTOMATIC', 15000, 'Rented', 500, 3000, 10000, false, NULL, NULL, NULL, 'EXCELLENT', '2027-08-31', '2026-08-31', '3839-Q-23', 5, 'Nissan Qashqai - SUV familial', agency_marrakech, cat_suv
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '3839-Q-23');

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Polo', 'Volkswagen', 2024, 'MANUAL', 3000, 'Available', 280, 1680, 5600, true, 10, CURRENT_DATE, '2026-09-30', 'EXCELLENT', '2027-12-31', '2027-03-31', '4041-R-45', 5, 'Volkswagen Polo - Qualité allemande', agency_marrakech, cat_compact
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '4041-R-45');

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'C4', 'Citroen', 2023, 'AUTOMATIC', 20000, 'Maintenance', 350, 2100, 7000, false, NULL, NULL, NULL, 'FAIR', '2027-05-31', '2026-05-31', '4243-S-67', 5, 'Citroen C4 - Confort', agency_marrakech, cat_compact
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '4243-S-67');

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Serie 3', 'BMW', 2024, 'AUTOMATIC', 1000, 'Available', 1100, 6600, 22000, false, NULL, NULL, NULL, 'EXCELLENT', '2029-12-31', '2027-12-31', '4445-T-89', 5, 'BMW Serie 3 - Performance', agency_marrakech, cat_luxe
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '4445-T-89');

-- =====================================================
-- TANGIER DRIVE (6 voitures)
-- =====================================================

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Picanto', 'Kia', 2023, 'MANUAL', 7000, 'Available', 210, 1260, 4200, true, 12, CURRENT_DATE, '2026-09-30', 'EXCELLENT', '2027-12-31', '2026-12-31', '4647-U-01', 5, 'Kia Picanto - Petite citadine économique', agency_tanger, cat_eco
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '4647-U-01');

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Tucson', 'Hyundai', 2023, 'AUTOMATIC', 12000, 'Reserved', 600, 3600, 12000, false, NULL, NULL, NULL, 'EXCELLENT', '2027-01-31', '2026-08-31', '4849-V-23', 5, 'Hyundai Tucson - SUV moderne', agency_tanger, cat_suv
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '4849-V-23');

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Rio', 'Kia', 2022, 'MANUAL', 40000, 'Available', 220, 1320, 4400, false, NULL, NULL, NULL, 'GOOD', '2026-10-31', '2026-05-31', '5051-W-45', 5, 'Kia Rio - Économique', agency_tanger, cat_eco
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '5051-W-45');

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Leon', 'Seat', 2023, 'AUTOMATIC', 18000, 'Available', 320, 1920, 6400, false, NULL, NULL, NULL, 'EXCELLENT', '2027-04-30', '2026-10-31', '5253-X-67', 5, 'Seat Leon - Sportive', agency_tanger, cat_compact
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '5253-X-67');

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Sportage', 'Kia', 2024, 'AUTOMATIC', 5000, 'Available', 550, 3300, 11000, true, 15, CURRENT_DATE, '2026-11-30', 'EXCELLENT', '2028-12-31', '2027-06-30', '5455-Y-89', 5, 'Kia Sportage - SUV premium', agency_tanger, cat_suv
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '5455-Y-89');

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'i20', 'Hyundai', 2023, 'MANUAL', 12000, 'Available', 240, 1440, 4800, false, NULL, NULL, NULL, 'EXCELLENT', '2027-06-30', '2026-12-31', '5657-Z-01', 5, 'Hyundai i20 - Moderne', agency_tanger, cat_eco
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '5657-Z-01');

-- =====================================================
-- FES AUTO LOCATION (3 voitures - agence en attente)
-- =====================================================

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Logan', 'Dacia', 2022, 'MANUAL', 50000, 'Available', 200, 1200, 4000, false, NULL, NULL, NULL, 'GOOD', '2026-09-30', '2026-06-30', '5858-A-23', 5, 'Dacia Logan - Économique', agency_fes, cat_eco
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '5858-A-23');

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Sandero', 'Dacia', 2023, 'MANUAL', 25000, 'Available', 220, 1320, 4400, false, NULL, NULL, NULL, 'GOOD', '2027-03-31', '2026-09-30', '6060-B-45', 5, 'Dacia Sandero - Populaire', agency_fes, cat_eco
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '6060-B-45');

INSERT INTO cars (id, mode, brand, manufacture_year, transmission_type, mileage, current_status, daily_price, weekly_price, monthly_price, promotion_active, promotion_rate, promotion_start_date, promotion_end_date, condition_status, license_expiry_date, insurance_expiry_date, registration_number, seat_capacity, description, agency_id, category_id)
SELECT gen_random_uuid(), 'Lodgy', 'Dacia', 2022, 'MANUAL', 60000, 'Available', 350, 2100, 7000, false, NULL, NULL, NULL, 'FAIR', '2026-08-31', '2026-05-31', '6262-C-67', 7, 'Dacia Lodgy - Familial', agency_fes, cat_suv
WHERE NOT EXISTS (SELECT 1 FROM cars WHERE registration_number = '6262-C-67');

END $$;

-- =====================================================
-- 5. STATISTIQUES FINALES
-- =====================================================

SELECT '=== STATISTIQUES FINALES ===' as info;
SELECT 'Agences: ' || COUNT(*) as count FROM agencies;
SELECT 'Catégories: ' || COUNT(*) as count FROM categories;
SELECT 'Voitures: ' || COUNT(*) as count FROM cars;
SELECT 'Voitures par statut:' as info;
SELECT current_status, COUNT(*) FROM cars GROUP BY current_status ORDER BY current_status;