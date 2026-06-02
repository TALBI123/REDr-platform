#!/bin/bash

# =====================================================
# SCRIPT D'EXÉCUTION AVEC DOCKER POSTGRESQL
# =====================================================

RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

print_message() { echo -e "${BLUE}[INFO]${NC} $1"; }
print_success() { echo -e "${GREEN}[SUCCESS]${NC} $1"; }
print_error() { echo -e "${RED}[ERROR]${NC} $1"; }

CONTAINER_NAME="postgres_local"
SQL_FILE=${1:-"data.sql"}

start_postgres_docker() {
    print_message "Démarrage de PostgreSQL avec Docker..."
    
    if docker ps -a | grep -q $CONTAINER_NAME; then
        print_message "Conteneur existe déjà, démarrage..."
        docker start $CONTAINER_NAME
    else
        print_message "Création d'un nouveau conteneur PostgreSQL..."
        docker run -d \
            --name $CONTAINER_NAME \
            -e POSTGRES_DB=car_rental \
            -e POSTGRES_USER=postgres \
            -e POSTGRES_PASSWORD=postgres \
            -p 5432:5432 \
            postgres:15
    fi
    
    # Attendre que PostgreSQL soit prêt
    sleep 5
    print_success "PostgreSQL est prêt"
}

execute_sql_docker() {
    print_message "Exécution du script SQL dans Docker..."
    
    docker exec -i $CONTAINER_NAME psql -U postgres -d car_rental < "$SQL_FILE"
    
    if [ $? -eq 0 ]; then
        print_success "Script exécuté avec succès"
    else
        print_error "Erreur lors de l'exécution"
        exit 1
    fi
}

main() {
    echo "========================================="
    echo "   PostgreSQL Docker - Data Seeder"
    echo "========================================="
    
    if ! command -v docker &> /dev/null; then
        print_error "Docker n'est pas installé"
        exit 1
    fi
    
    start_postgres_docker
    execute_sql_docker
    
    print_success "Base de données initialisée dans Docker!"
    echo ""
    echo "Connexion: docker exec -it $CONTAINER_NAME psql -U postgres -d car_rental"
}

main "$@"