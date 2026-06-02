#!/bin/bash

# =====================================================
# SCRIPT D'EXÉCUTION POUR POSTGRESQL DISTANT (SUPABASE)
# =====================================================

# Couleurs pour l'affichage
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Configuration par défaut
DB_HOST=""
DB_PORT="5432"
DB_NAME="postgres"
DB_USER="postgres"
DB_PASSWORD=""
SSL_MODE="require"
SQL_FILE=${1:-"data.sql"}

# Fonctions d'affichage
print_message() { echo -e "${BLUE}[INFO]${NC} $1"; }
print_success() { echo -e "${GREEN}[SUCCESS]${NC} $1"; }
print_error() { echo -e "${RED}[ERROR]${NC} $1"; }
print_warning() { echo -e "${YELLOW}[WARNING]${NC} $1"; }

# Vérifier si psql est installé
check_psql() {
    if ! command -v psql &> /dev/null; then
        print_error "PostgreSQL client (psql) n'est pas installé"
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

# Configurer la connexion Supabase
configure_supabase() {
    echo ""
    echo "=== Configuration Supabase ==="
    echo "Vous pouvez trouver ces informations dans:"
    echo "1. Dashboard Supabase -> Project Settings -> Database"
    echo "2. Connection string -> URI"
    echo ""
    
    read -p "URL de votre projet Supabase (ex: xxxxx.supabase.co): " PROJECT_URL
    DB_HOST="$PROJECT_URL"
    
    read -p "Mot de passe de la base de données: " DB_PASSWORD
    
    # Option SSL
    read -p "Mode SSL (require/prefer/disable) [$SSL_MODE]: " input
    SSL_MODE=${input:-$SSL_MODE}
    
    # Tester la connexion
    print_message "Test de connexion à Supabase..."
    
    export PGPASSWORD=$DB_PASSWORD
    if psql "sslmode=$SSL_MODE host=$DB_HOST port=$DB_PORT dbname=$DB_NAME user=$DB_USER" -c "SELECT 1" &> /dev/null; then
        print_success "Connexion réussie à Supabase!"
        
        # Sauvegarder la configuration
        cat > .db_config_remote <<EOF
DB_HOST=$DB_HOST
DB_PORT=$DB_PORT
DB_NAME=$DB_NAME
DB_USER=$DB_USER
SSL_MODE=$SSL_MODE
# DB_PASSWORD est gardé en mémoire uniquement
EOF
        print_success "Configuration sauvegardée dans .db_config_remote"
        return 0
    else
        print_error "Connexion échouée. Vérifiez vos identifiants."
        return 1
    fi
}

# Configurer depuis une URL de connexion complète
configure_from_url() {
    echo ""
    print_message "Configuration depuis une URL de connexion"
    read -p "URL de connexion PostgreSQL (format: postgresql://user:pass@host:port/db): " CONN_URL
    
    # Extraire les informations de l'URL
    # Format: postgresql://postgres:password@db.xxxxx.supabase.co:5432/postgres
    DB_USER=$(echo $CONN_URL | sed -n 's/.*:\/\/\([^:]*\):.*/\1/p')
    DB_PASSWORD=$(echo $CONN_URL | sed -n 's/.*:\/\/[^:]*:\([^@]*\)@.*/\1/p')
    DB_HOST=$(echo $CONN_URL | sed -n 's/.*@\([^:]*\):.*/\1/p')
    DB_PORT=$(echo $CONN_URL | sed -n 's/.*:\([0-9]*\)\/.*/\1/p')
    DB_NAME=$(echo $CONN_URL | sed -n 's/.*\/\([^?]*\).*/\1/p')
    
    print_success "Configuration extraite de l'URL"
    print_message "Host: $DB_HOST"
    print_message "Database: $DB_NAME"
}

# Fonction pour désactiver RLS temporairement
disable_rls() {
    print_message "Désactivation temporaire de RLS pour l'insertion..."
    
    local tables=("agencies" "cars" "users" "reservations" "payments" "clients" "admins" "agency_managers")
    
    for table in "${tables[@]}"; do
        PGPASSWORD=$DB_PASSWORD psql "sslmode=$SSL_MODE host=$DB_HOST port=$DB_PORT dbname=$DB_NAME user=$DB_USER" \
            -c "ALTER TABLE IF EXISTS $table DISABLE ROW LEVEL SECURITY;" 2>/dev/null
    done
    
    print_success "RLS désactivé"
}

# Fonction pour réactiver RLS
enable_rls() {
    print_message "Réactivation de RLS..."
    
    local tables=("agencies" "cars" "users" "reservations" "payments" "clients" "admins" "agency_managers")
    
    for table in "${tables[@]}"; do
        PGPASSWORD=$DB_PASSWORD psql "sslmode=$SSL_MODE host=$DB_HOST port=$DB_PORT dbname=$DB_NAME user=$DB_USER" \
            -c "ALTER TABLE IF EXISTS $table ENABLE ROW LEVEL SECURITY;" 2>/dev/null
    done
    
    print_success "RLS réactivé"
}

# Exécuter le script SQL
execute_sql() {
    print_message "Exécution du script SQL sur Supabase..."
    
    local start_time=$(date +%s)
    
    # Exécuter avec gestion d'erreurs
    PGPASSWORD=$DB_PASSWORD psql "sslmode=$SSL_MODE host=$DB_HOST port=$DB_PORT dbname=$DB_NAME user=$DB_USER" \
        -v ON_ERROR_STOP=1 \
        -f "$SQL_FILE" 2>&1
    
    if [ $? -eq 0 ]; then
        local end_time=$(date +%s)
        local duration=$((end_time - start_time))
        print_success "Script exécuté avec succès en ${duration} secondes"
        return 0
    else
        print_error "Erreur lors de l'exécution du script"
        return 1
    fi
}

# Vérifier les résultats
verify_results() {
    print_message "Vérification des résultats..."
    
    local queries=(
        "SELECT COUNT(*) as agencies FROM agencies;"
        "SELECT COUNT(*) as cars FROM cars;"
        "SELECT COUNT(*) as users FROM users;"
        "SELECT role, COUNT(*) FROM users GROUP BY role;"
    )
    
    for query in "${queries[@]}"; do
        echo ""
        PGPASSWORD=$DB_PASSWORD psql "sslmode=$SSL_MODE host=$DB_HOST port=$DB_PORT dbname=$DB_NAME user=$DB_USER" \
            -c "$query" 2>/dev/null
    done
}

# Menu principal
main_menu() {
    echo ""
    echo "========================================="
    echo "   PostgreSQL Distant - Data Seeder"
    echo "========================================="
    echo "1. Configurer depuis les informations manuelles"
    echo "2. Configurer depuis une URL de connexion"
    echo "3. Utiliser la configuration existante"
    echo "4. Quitter"
    echo ""
    read -p "Choisissez une option [1-4]: " option
    
    case $option in
        1)
            configure_supabase
            ;;
        2)
            configure_from_url
            ;;
        3)
            if [ -f ".db_config_remote" ]; then
                source .db_config_remote
                print_success "Configuration chargée"
            else
                print_error "Aucune configuration existante"
                configure_supabase
            fi
            ;;
        4)
            exit 0
            ;;
        *)
            print_error "Option invalide"
            main_menu
            ;;
    esac
}

# Fonction pour sauvegarder les données existantes (optionnel)
backup_database() {
    print_message "Sauvegarde de la base de données existante..."
    
    local backup_file="backup_$(date +%Y%m%d_%H%M%S).sql"
    
    PGPASSWORD=$DB_PASSWORD pg_dump "sslmode=$SSL_MODE host=$DB_HOST port=$DB_PORT dbname=$DB_NAME user=$DB_USER" \
        --no-owner --no-privileges > "$backup_file" 2>/dev/null
    
    if [ $? -eq 0 ]; then
        print_success "Sauvegarde créée: $backup_file"
    else
        print_warning "Impossible de créer la sauvegarde"
    fi
}

# Fonction principale
main() {
    echo "========================================="
    echo "   PostgreSQL Distant - Data Seeder"
    echo "========================================="
    
    check_psql
    check_sql_file
    
    if [ -f ".db_config_remote" ]; then
        read -p "Utiliser la configuration existante ? (y/n): " use_config
        if [[ $use_config == "y" ]]; then
            source .db_config_remote
        else
            main_menu
        fi
    else
        main_menu
    fi
    
    # Vérifier la connexion
    if ! PGPASSWORD=$DB_PASSWORD psql "sslmode=$SSL_MODE host=$DB_HOST port=$DB_PORT dbname=$DB_NAME user=$DB_USER" -c "SELECT 1" &> /dev/null; then
        print_error "Impossible de se connecter à la base distante"
        exit 1
    fi
    
    # Option de backup
    read -p "Créer une sauvegarde avant d'insérer ? (y/n): " do_backup
    if [[ $do_backup == "y" ]]; then
        backup_database
    fi
    
    # Désactiver RLS pour l'insertion
    read -p "Désactiver RLS pour l'insertion ? (recommandé pour Supabase) (y/n): " disable_security
    if [[ $disable_security == "y" ]]; then
        disable_rls
    fi
    
    # Exécuter le script
    execute_sql
    
    if [ $? -eq 0 ]; then
        verify_results
        print_success "✅ Base de données distante initialisée avec succès!"
    else
        print_error "❌ Échec de l'initialisation"
    fi
    
    # Réactiver RLS
    if [[ $disable_security == "y" ]]; then
        enable_rls
    fi
}

# Exécuter le script
main "$@"