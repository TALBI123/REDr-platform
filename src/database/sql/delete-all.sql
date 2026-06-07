-- Script de suppression complète de la base de données
-- Désactiver temporairement les vérifications de clés étrangères
SET session_replication_role = 'replica';

-- Supprimer d'abord les tables dépendantes (celles qui ont des FK vers d'autres tables)
DROP TABLE IF EXISTS email_verification_token CASCADE;
DROP TABLE IF EXISTS condition_reports CASCADE;
DROP TABLE IF EXISTS contracts CASCADE;
DROP TABLE IF EXISTS executions CASCADE;
DROP TABLE IF EXISTS payments CASCADE;
DROP TABLE IF EXISTS reservations CASCADE;
DROP TABLE IF EXISTS photos CASCADE;
DROP TABLE IF EXISTS notifications CASCADE;
DROP TABLE IF EXISTS system_maintenance CASCADE;
DROP TABLE IF EXISTS car_fuel_types CASCADE;
DROP TABLE IF EXISTS cars CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS agencies CASCADE;

-- Ensuite supprimer les tables d'utilisateurs (héritage JOINED)
DROP TABLE IF EXISTS admins CASCADE;
DROP TABLE IF EXISTS agency_managers CASCADE;
DROP TABLE IF EXISTS clients CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Réactiver les vérifications
SET session_replication_role = 'origin';

-- Message de confirmation
DO $$
BEGIN
    RAISE NOTICE 'Toutes les tables ont été supprimées avec succès !';
END $$;