-- =====================================================
-- SCRIPT COMPLET POUR POSTGRESQL
-- À exécuter dans l'ordre : agences d'abord, puis utilisateurs
-- =====================================================

-- =====================================================
-- 1. AGENCES (doivent exister avant les agency_managers)
-- =====================================================

-- Générer des UUIDs valides pour les agences
DO $$
DECLARE
    agency_casa UUID := '123e4567-e89b-12d3-a456-426614174001';
    agency_rabat UUID := '123e4567-e89b-12d3-a456-426614174002';
    agency_marrakech UUID := '123e4567-e89b-12d3-a456-426614174003';
    agency_tanger UUID := '123e4567-e89b-12d3-a456-426614174004';
    agency_fes UUID := '123e4567-e89b-12d3-a456-426614174005';
BEGIN

-- Insérer les agences si elles n'existent pas
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
-- 2. CATEGORIES
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

-- =====================================================
-- 3. UTILISATEURS (avec user_type correct pour l'héritage JOINED)
-- =====================================================

-- 3.1 SUPER ADMIN
INSERT INTO users (id, first_name, last_name, email, password, password_updated_at, inscription_date, role, account_status, failed_login_attempts, lock_until, email_verified_at, user_type)
SELECT gen_random_uuid(), 'Youssef', 'Admin', 'admin@carrental.ma', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'SUPER_ADMIN', 'ACTIVE', 0, NULL, CURRENT_TIMESTAMP, 'SUPER_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@carrental.ma');

-- 3.2 ADMIN (table admins)
INSERT INTO admins (id, permissions, admin_level)
SELECT u.id, 'ALL', 1
FROM users u
WHERE u.email = 'admin@carrental.ma'
AND NOT EXISTS (SELECT 1 FROM admins a WHERE a.id = u.id);

-- 3.3 AGENCY MANAGERS - SPEEDCAR (APPROUVÉ)
INSERT INTO users (id, first_name, last_name, email, password, password_updated_at, inscription_date, role, account_status, failed_login_attempts, lock_until, email_verified_at, user_type)
SELECT gen_random_uuid(), 'Karim', 'Benjelloun', 'karim@speedcar.ma', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'AGENCY_MANAGER', 'ACTIVE', 0, NULL, CURRENT_TIMESTAMP, 'AGENCY_MANAGER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'karim@speedcar.ma');

INSERT INTO agency_managers (id, phone, national_id, digital_signature, responsability_level, licence_number, approved_at, approved_by_admin_id, agency_id)
SELECT u.id, '+212622123456', 'BE123456', 'sig_karim_001', 1, 'LIC-001-2024', CURRENT_TIMESTAMP, (SELECT id FROM users WHERE email = 'admin@carrental.ma'), agency_casa
FROM users u
WHERE u.email = 'karim@speedcar.ma'
AND NOT EXISTS (SELECT 1 FROM agency_managers am WHERE am.id = u.id);

-- 3.4 AGENCY MANAGERS - RABAT (APPROUVÉ)
INSERT INTO users (id, first_name, last_name, email, password, password_updated_at, inscription_date, role, account_status, failed_login_attempts, lock_until, email_verified_at, user_type)
SELECT gen_random_uuid(), 'Fatima', 'Zahra', 'fatima@rabatrent.ma', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'AGENCY_MANAGER', 'ACTIVE', 0, NULL, CURRENT_TIMESTAMP, 'AGENCY_MANAGER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'fatima@rabatrent.ma');

INSERT INTO agency_managers (id, phone, national_id, digital_signature, responsability_level, licence_number, approved_at, approved_by_admin_id, agency_id)
SELECT u.id, '+212637890123', 'ZA789012', 'sig_fatima_002', 1, 'LIC-002-2024', CURRENT_TIMESTAMP, (SELECT id FROM users WHERE email = 'admin@carrental.ma'), agency_rabat
FROM users u
WHERE u.email = 'fatima@rabatrent.ma'
AND NOT EXISTS (SELECT 1 FROM agency_managers am WHERE am.id = u.id);

-- 3.5 AGENCY MANAGERS - MARRAKECH (APPROUVÉ)
INSERT INTO users (id, first_name, last_name, email, password, password_updated_at, inscription_date, role, account_status, failed_login_attempts, lock_until, email_verified_at, user_type)
SELECT gen_random_uuid(), 'Mohamed', 'El Mansouri', 'mohamed@marrakechcar.ma', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'AGENCY_MANAGER', 'ACTIVE', 0, NULL, CURRENT_TIMESTAMP, 'AGENCY_MANAGER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'mohamed@marrakechcar.ma');

INSERT INTO agency_managers (id, phone, national_id, digital_signature, responsability_level, licence_number, approved_at, approved_by_admin_id, agency_id)
SELECT u.id, '+212661456789', 'EL456123', 'sig_mohamed_003', 1, 'LIC-003-2024', CURRENT_TIMESTAMP, (SELECT id FROM users WHERE email = 'admin@carrental.ma'), agency_marrakech
FROM users u
WHERE u.email = 'mohamed@marrakechcar.ma'
AND NOT EXISTS (SELECT 1 FROM agency_managers am WHERE am.id = u.id);

-- 3.6 AGENCY MANAGERS - TANGER (APPROUVÉ)
INSERT INTO users (id, first_name, last_name, email, password, password_updated_at, inscription_date, role, account_status, failed_login_attempts, lock_until, email_verified_at, user_type)
SELECT gen_random_uuid(), 'Sofia', 'El Amrani', 'sofia@tangierdrive.ma', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'AGENCY_MANAGER', 'ACTIVE', 0, NULL, CURRENT_TIMESTAMP, 'AGENCY_MANAGER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'sofia@tangierdrive.ma');

INSERT INTO agency_managers (id, phone, national_id, digital_signature, responsability_level, licence_number, approved_at, approved_by_admin_id, agency_id)
SELECT u.id, '+212639567890', 'AM789345', 'sig_sofia_004', 1, 'LIC-004-2024', CURRENT_TIMESTAMP, (SELECT id FROM users WHERE email = 'admin@carrental.ma'), agency_tanger
FROM users u
WHERE u.email = 'sofia@tangierdrive.ma'
AND NOT EXISTS (SELECT 1 FROM agency_managers am WHERE am.id = u.id);

-- 3.7 AGENCY MANAGERS - FES (PENDING)
INSERT INTO users (id, first_name, last_name, email, password, password_updated_at, inscription_date, role, account_status, failed_login_attempts, lock_until, email_verified_at, user_type)
SELECT gen_random_uuid(), 'Hassan', 'Fassi', 'hassan@fesauto.ma', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'AGENCY_MANAGER', 'PENDING', 0, NULL, NULL, 'AGENCY_MANAGER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'hassan@fesauto.ma');

INSERT INTO agency_managers (id, phone, national_id, digital_signature, responsability_level, licence_number, approved_at, approved_by_admin_id, agency_id)
SELECT u.id, '+212678901234', 'FA901234', 'sig_hassan_005', 1, 'LIC-005-2024', NULL, NULL, agency_fes
FROM users u
WHERE u.email = 'hassan@fesauto.ma'
AND NOT EXISTS (SELECT 1 FROM agency_managers am WHERE am.id = u.id);

-- =====================================================
-- 4. CLIENTS (10 clients marocains)
-- =====================================================

-- Client 1
INSERT INTO users (id, first_name, last_name, email, password, password_updated_at, inscription_date, role, account_status, failed_login_attempts, lock_until, email_verified_at, user_type)
SELECT gen_random_uuid(), 'Ahmed', 'Benali', 'ahmed.benali@gmail.com', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'CLIENT', 'ACTIVE', 0, NULL, CURRENT_TIMESTAMP, 'CLIENT'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'ahmed.benali@gmail.com');

INSERT INTO clients (id, licence_number, location, licence_expiration_date, national_id, passport_number, phone, digital_signature)
SELECT u.id, 'B123456', 'Casablanca', '2028-12-31', 'AB123456', NULL, '+212612345678', 'sig_ahmed_001'
FROM users u
WHERE u.email = 'ahmed.benali@gmail.com'
AND NOT EXISTS (SELECT 1 FROM clients c WHERE c.id = u.id);

-- Client 2
INSERT INTO users (id, first_name, last_name, email, password, password_updated_at, inscription_date, role, account_status, failed_login_attempts, lock_until, email_verified_at, user_type)
SELECT gen_random_uuid(), 'Khadija', 'Mouradi', 'khadija.mouradi@yahoo.com', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'CLIENT', 'ACTIVE', 0, NULL, CURRENT_TIMESTAMP, 'CLIENT'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'khadija.mouradi@yahoo.com');

INSERT INTO clients (id, licence_number, location, licence_expiration_date, national_id, passport_number, phone, digital_signature)
SELECT u.id, 'C789012', 'Rabat', '2029-06-30', 'KM789012', NULL, '+212678901234', 'sig_khadija_002'
FROM users u
WHERE u.email = 'khadija.mouradi@yahoo.com'
AND NOT EXISTS (SELECT 1 FROM clients c WHERE c.id = u.id);

-- Client 3
INSERT INTO users (id, first_name, last_name, email, password, password_updated_at, inscription_date, role, account_status, failed_login_attempts, lock_until, email_verified_at, user_type)
SELECT gen_random_uuid(), 'Youssef', 'Tazi', 'youssef.tazi@hotmail.com', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'CLIENT', 'ACTIVE', 0, NULL, CURRENT_TIMESTAMP, 'CLIENT'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'youssef.tazi@hotmail.com');

INSERT INTO clients (id, licence_number, location, licence_expiration_date, national_id, passport_number, phone, digital_signature)
SELECT u.id, 'D345678', 'Marrakech', '2027-11-15', 'YT345678', 'P9876543', '+212661234567', 'sig_youssef_003'
FROM users u
WHERE u.email = 'youssef.tazi@hotmail.com'
AND NOT EXISTS (SELECT 1 FROM clients c WHERE c.id = u.id);

-- Client 4
INSERT INTO users (id, first_name, last_name, email, password, password_updated_at, inscription_date, role, account_status, failed_login_attempts, lock_until, email_verified_at, user_type)
SELECT gen_random_uuid(), 'Nadia', 'Chraibi', 'nadia.chraibi@gmail.com', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'CLIENT', 'ACTIVE', 0, NULL, CURRENT_TIMESTAMP, 'CLIENT'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'nadia.chraibi@gmail.com');

INSERT INTO clients (id, licence_number, location, licence_expiration_date, national_id, passport_number, phone, digital_signature)
SELECT u.id, 'E901234', 'Tanger', '2028-08-20', 'NC901234', NULL, '+212650123456', 'sig_nadia_004'
FROM users u
WHERE u.email = 'nadia.chraibi@gmail.com'
AND NOT EXISTS (SELECT 1 FROM clients c WHERE c.id = u.id);

-- Client 5 (SUSPENDED)
INSERT INTO users (id, first_name, last_name, email, password, password_updated_at, inscription_date, role, account_status, failed_login_attempts, lock_until, email_verified_at, user_type)
SELECT gen_random_uuid(), 'Omar', 'Idrissi', 'omar.idrissi@yahoo.com', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'CLIENT', 'SUSPENDED', 5, CURRENT_TIMESTAMP + INTERVAL '1 day', NULL, 'CLIENT'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'omar.idrissi@yahoo.com');

INSERT INTO clients (id, licence_number, location, licence_expiration_date, national_id, passport_number, phone, digital_signature)
SELECT u.id, 'F567890', 'Fès', '2026-12-01', 'OI567890', NULL, '+212698765432', 'sig_omar_005'
FROM users u
WHERE u.email = 'omar.idrissi@yahoo.com'
AND NOT EXISTS (SELECT 1 FROM clients c WHERE c.id = u.id);

-- Client 6
INSERT INTO users (id, first_name, last_name, email, password, password_updated_at, inscription_date, role, account_status, failed_login_attempts, lock_until, email_verified_at, user_type)
SELECT gen_random_uuid(), 'Leila', 'Bennis', 'leila.bennis@gmail.com', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'CLIENT', 'ACTIVE', 0, NULL, CURRENT_TIMESTAMP, 'CLIENT'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'leila.bennis@gmail.com');

INSERT INTO clients (id, licence_number, location, licence_expiration_date, national_id, passport_number, phone, digital_signature)
SELECT u.id, 'G123789', 'Agadir', '2029-03-25', 'LB123789', 'P4567890', '+212665432109', 'sig_leila_006'
FROM users u
WHERE u.email = 'leila.bennis@gmail.com'
AND NOT EXISTS (SELECT 1 FROM clients c WHERE c.id = u.id);

-- Client 7
INSERT INTO users (id, first_name, last_name, email, password, password_updated_at, inscription_date, role, account_status, failed_login_attempts, lock_until, email_verified_at, user_type)
SELECT gen_random_uuid(), 'Hamid', 'El Fassi', 'hamid.elfassi@hotmail.com', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'CLIENT', 'ACTIVE', 0, NULL, CURRENT_TIMESTAMP, 'CLIENT'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'hamid.elfassi@hotmail.com');

INSERT INTO clients (id, licence_number, location, licence_expiration_date, national_id, passport_number, phone, digital_signature)
SELECT u.id, 'H456321', 'Oujda', '2028-07-10', 'HE456321', NULL, '+212712345678', 'sig_hamid_007'
FROM users u
WHERE u.email = 'hamid.elfassi@hotmail.com'
AND NOT EXISTS (SELECT 1 FROM clients c WHERE c.id = u.id);

-- Client 8
INSERT INTO users (id, first_name, last_name, email, password, password_updated_at, inscription_date, role, account_status, failed_login_attempts, lock_until, email_verified_at, user_type)
SELECT gen_random_uuid(), 'Samira', 'Mernissi', 'samira.mernissi@gmail.com', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'CLIENT', 'ACTIVE', 0, NULL, CURRENT_TIMESTAMP, 'CLIENT'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'samira.mernissi@gmail.com');

INSERT INTO clients (id, licence_number, location, licence_expiration_date, national_id, passport_number, phone, digital_signature)
SELECT u.id, 'I789654', 'Casablanca', '2029-01-05', 'SM789654', NULL, '+212622334455', 'sig_samira_008'
FROM users u
WHERE u.email = 'samira.mernissi@gmail.com'
AND NOT EXISTS (SELECT 1 FROM clients c WHERE c.id = u.id);

-- Client 9 (LOCKED)
INSERT INTO users (id, first_name, last_name, email, password, password_updated_at, inscription_date, role, account_status, failed_login_attempts, lock_until, email_verified_at, user_type)
SELECT gen_random_uuid(), 'Rachid', 'Ouazzani', 'rachid.ouazzani@yahoo.com', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'CLIENT', 'LOCKED', 10, CURRENT_TIMESTAMP + INTERVAL '2 days', NULL, 'CLIENT'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'rachid.ouazzani@yahoo.com');

INSERT INTO clients (id, licence_number, location, licence_expiration_date, national_id, passport_number, phone, digital_signature)
SELECT u.id, 'J987321', 'Rabat', '2027-09-12', 'RO987321', NULL, '+212633445566', 'sig_rachid_009'
FROM users u
WHERE u.email = 'rachid.ouazzani@yahoo.com'
AND NOT EXISTS (SELECT 1 FROM clients c WHERE c.id = u.id);

-- Client 10
INSERT INTO users (id, first_name, last_name, email, password, password_updated_at, inscription_date, role, account_status, failed_login_attempts, lock_until, email_verified_at, user_type)
SELECT gen_random_uuid(), 'Mona', 'El Khayat', 'mona.elkhayat@gmail.com', '{noop}password123', CURRENT_TIMESTAMP, CURRENT_DATE, 'CLIENT', 'ACTIVE', 0, NULL, CURRENT_TIMESTAMP, 'CLIENT'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'mona.elkhayat@gmail.com');

INSERT INTO clients (id, licence_number, location, licence_expiration_date, national_id, passport_number, phone, digital_signature)
SELECT u.id, 'K258147', 'Marrakech', '2030-02-28', 'ME258147', 'P1239876', '+212644556677', 'sig_mona_010'
FROM users u
WHERE u.email = 'mona.elkhayat@gmail.com'
AND NOT EXISTS (SELECT 1 FROM clients c WHERE c.id = u.id);

-- =====================================================
-- 5. RÉSERVATIONS
-- =====================================================

-- Insertion de réservations (optionnel)
-- À exécuter seulement si vous avez des voitures

END $$;

-- Afficher un résumé
SELECT '=== AGENCES ===' as info;
SELECT id, name, city, status FROM agencies;

SELECT '=== ADMIN ===' as info;
SELECT id, email, role FROM users WHERE role = 'SUPER_ADMIN';

SELECT '=== AGENCY MANAGERS ===' as info;
SELECT u.email, u.account_status, a.responsability_level, ag.name as agency_name
FROM users u
JOIN agency_managers a ON u.id = a.id
JOIN agencies ag ON a.agency_id = ag.id;

SELECT '=== CLIENTS ===' as info;
SELECT u.email, u.account_status, c.location
FROM users u
JOIN clients c ON u.id = c.id;