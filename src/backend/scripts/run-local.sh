#!/bin/bash

# =====================================================
# SCRIPT D'EXÉCUTION POUR POSTGRESQL LOCAL
# =====================================================

# Couleurs pour l'affichage
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration par défaut
DB_HOST=${DB_HOST:-"localhost"}
DB_PORT=${DB_PORT:-"5432"}
DB_NAME=${DB_NAME:-"postgres"}
DB_USER=${DB_USER:-"postgres"}
DB_PASSWORD=${DB_PASSWORD:-""}
SQL_FILE=${1:-"data.sql"}  # Premier argument ou data.sql par défaut

# Fonction pour afficher les messages
print_message() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

# Vérifier si psql est installé
check_psql() {
    if ! command -v psql &> /dev/null; then
        print_error "PostgreSQL client (psql) n'est pas installé"
        echo "Installation sur Ubuntu/Debian: sudo apt-get install postgresql-client"
        echo "Installation sur Mac: brew install postgresql"
        echo "Installation sur Windows: téléchargez depuis https://www.postgresql.org/download/"
        exit 1
    fi
    print_success "PostgreSQL client trouvé"
}

# Vérifier si le fichier SQL existe
check_sql_file() {
    if [ ! -f "$SQL_FILE" ]; then
        print_error "Fichier SQL '$SQL_FILE' non trouvé"
        exit 1
    fi
    print_success "Fichier SQL trouvé: $SQL_FILE"
}

# Tester la connexion
test_connection() {
    print_message "Test de connexion à PostgreSQL local..."
    
    if [ -z "$DB_PASSWORD" ]; then
        export PGPASSWORD=$DB_PASSWORD
        if psql -h "$DB_HOST" -p "$DB_PORT" -d "$DB_NAME" -U "$DB_USER" -c "SELECT 1" &> /dev/null; then
            print_success "Connexion réussie à $DB_NAME sur $DB_HOST:$DB_PORT"
            return 0
        else
            print_error "Impossible de se connecter à PostgreSQL"
            return 1
        fi
    else
        if PGPASSWORD=$DB_PASSWORD psql -h "$DB_HOST" -p "$DB_PORT" -d "$DB_NAME" -U "$DB_USER" -c "SELECT 1" &> /dev/null; then
            print_success "Connexion réussie à $DB_NAME sur $DB_HOST:$DB_PORT"
            return 0
        else
            print_error "Impossible de se connecter à PostgreSQL"
            return 1
        fi
    fi
}

# Exécuter le script SQL
execute_sql() {
    print_message "Exécution du script SQL: $SQL_FILE"
    
    local start_time=$(date +%s)
    
    if [ -z "$DB_PASSWORD" ]; then
        if psql -h "$DB_HOST" -p "$DB_PORT" -d "$DB_NAME" -U "$DB_USER" -f "$SQL_FILE" 2>&1; then
            local end_time=$(date +%s)
            local duration=$((end_time - start_time))
            print_success "Script exécuté avec succès en ${duration} secondes"
            return 0
        else
            print_error "Erreur lors de l'exécution du script"
            return 1
        fi
    else
        if PGPASSWORD=$DB_PASSWORD psql -h "$DB_HOST" -p "$DB_PORT" -d "$DB_NAME" -U "$DB_USER" -f "$SQL_FILE" 2>&1; then
            local end_time=$(date +%s)
            local duration=$((end_time - start_time))
            print_success "Script exécuté avec succès en ${duration} secondes"
            return 0
        else
            print_error "Erreur lors de l'exécution du script"
            return 1
        fi
    fi
}

# Fonction pour vérifier les tables créées
verify_tables() {
    print_message "Vérification des tables créées..."
    
    local tables=("agencies" "cars" "users" "reservations" "payments")
    
    for table in "${tables[@]}"; do
        if [ -z "$DB_PASSWORD" ]; then
            local count=$(psql -h "$DB_HOST" -p "$DB_PORT" -d "$DB_NAME" -U "$DB_USER" -t -c "SELECT COUNT(*) FROM $table" 2>/dev/null | xargs)
        else
            local count=$(PGPASSWORD=$DB_PASSWORD psql -h "$DB_HOST" -p "$DB_PORT" -d "$DB_NAME" -U "$DB_USER" -t -c "SELECT COUNT(*) FROM $table" 2>/dev/null | xargs)
        fi
        
        if [ "$count" != "" ] && [ "$count" != "0" ]; then
            print_success "Table $table: $count enregistrements"
        else
            print_warning "Table $table: vide ou inexistante"
        fi
    done
}

# Menu interactif pour la configuration
interactive_config() {
    echo ""
    echo "=== Configuration PostgreSQL Local ==="
    read -p "Hôte [$DB_HOST]: " input
    DB_HOST=${input:-$DB_HOST}
    
    read -p "Port [$DB_PORT]: " input
    DB_PORT=${input:-$DB_PORT}
    
    read -p "Nom de la base de données [$DB_NAME]: " input
    DB_NAME=${input:-$DB_NAME}
    
    read -p "Utilisateur [$DB_USER]: " input
    DB_USER=${input:-$DB_USER}
    
    read -sp "Mot de passe: " DB_PASSWORD
    echo ""
    
    # Sauvegarder la configuration
    cat > .db_config_local <<EOF
DB_HOST=$DB_HOST
DB_PORT=$DB_PORT
DB_NAME=$DB_NAME
DB_USER=$DB_USER
# DB_PASSWORD est gardé en mémoire uniquement
EOF
    print_success "Configuration sauvegardée dans .db_config_local"
}

# Charger la configuration sauvegardée
load_config() {
    if [ -f ".db_config_local" ]; then
        source .db_config_local
        print_message "Configuration chargée depuis .db_config_local"
    fi
}

# Fonction principale
main() {
    echo "========================================="
    echo "   PostgreSQL Local - Data Seeder"
    echo "========================================="
    
    check_psql
    
    if [ -f ".db_config_local" ]; then
        load_config
        read -p "Utiliser la configuration existante ? (y/n): " use_config
        if [[ $use_config != "y" ]]; then
            interactive_config
        fi
    else
        interactive_config
    fi
    
    check_sql_file
    test_connection
    
    if [ $? -eq 0 ]; then
        execute_sql
        if [ $? -eq 0 ]; then
            verify_tables
            print_success "✅ Base de données initialisée avec succès!"
        else
            print_error "❌ Échec de l'initialisation"
            exit 1
        fi
    else
        print_error "❌ Connexion échouée"
        exit 1
    fi
}

# Exécuter le script
main "$@"