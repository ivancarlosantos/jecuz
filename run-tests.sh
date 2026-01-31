#!/bin/bash

# Script para executar diferentes tipos de testes no projeto Jecuz
# Uso: ./run-tests.sh [tipo]
# Tipos: all, unit, slice, integration, e2e, smoke, mutation

set -e

COLORS=true
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

if [ "$COLORS" = false ]; then
    RED=''
    GREEN=''
    YELLOW=''
    BLUE=''
    NC=''
fi

print_header() {
    echo -e "${BLUE}============================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}============================================${NC}"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

# Padrão padrão: all
TEST_TYPE=${1:-all}

case $TEST_TYPE in
    all)
        print_header "Executando TODOS os testes"
        mvn clean test
        print_success "Todos os testes executados"
        ;;
    unit)
        print_header "Executando testes de UNIDADE"
        mvn test -Dtest="**/*UnitTest,**/*MutationTest"
        print_success "Testes de unidade executados"
        ;;
    slice)
        print_header "Executando testes de SLICE"
        mvn test -Dtest="**/*SliceTest"
        print_success "Testes de slice executados"
        ;;
    integration)
        print_header "Executando testes de INTEGRAÇÃO"
        mvn test -Dtest="**/*IT,**/*IntegrationTest,**/*ControllerTest"
        print_success "Testes de integração executados"
        ;;
    e2e)
        print_header "Executando testes E2E"
        mvn test -Dtest="**/*E2ETest"
        print_success "Testes E2E executados"
        ;;
    smoke)
        print_header "Executando testes de FUMAÇA"
        mvn test -Dgroups="smoke"
        print_success "Testes de fumaça executados"
        ;;
    mutation)
        print_header "Executando Mutation Testing com PIT"
        print_warning "Isso pode levar vários minutos..."
        mvn clean org.pitest:pitest-maven:mutationCoverage verify
        print_success "Mutation testing completo"
        echo -e "${BLUE}Relatório em: target/pit-reports/index.html${NC}"
        ;;
    coverage)
        print_header "Gerando relatórios de cobertura"
        mvn clean test
        print_success "JaCoCo coverage em: target/site/jacoco/index.html"
        ;;
    report)
        print_header "Abrindo relatório de cobertura"
        if [ -f "target/site/jacoco/index.html" ]; then
            open target/site/jacoco/index.html 2>/dev/null || xdg-open target/site/jacoco/index.html 2>/dev/null || start target/site/jacoco/index.html 2>/dev/null || print_warning "Abra manualmente: target/site/jacoco/index.html"
        else
            print_error "Relatório não encontrado. Execute primeiro: mvn test"
        fi
        ;;
    pit-report)
        print_header "Abrindo relatório PIT"
        if [ -f "target/pit-reports/index.html" ]; then
            open target/pit-reports/index.html 2>/dev/null || xdg-open target/pit-reports/index.html 2>/dev/null || start target/pit-reports/index.html 2>/dev/null || print_warning "Abra manualmente: target/pit-reports/index.html"
        else
            print_error "Relatório não encontrado. Execute primeiro: mvn org.pitest:pitest-maven:mutationCoverage"
        fi
        ;;
    help)
        print_header "Uso: ./run-tests.sh [tipo]"
        echo ""
        echo "Tipos disponíveis:"
        echo "  all         - Executa todos os testes"
        echo "  unit        - Testes de unidade"
        echo "  slice       - Testes de slice (repositories)"
        echo "  integration - Testes de integração"
        echo "  e2e         - Testes ponta a ponta"
        echo "  smoke       - Testes de fumaça"
        echo "  mutation    - Mutation testing com PIT"
        echo "  coverage    - Gera relatório de cobertura JaCoCo"
        echo "  report      - Abre relatório de cobertura"
        echo "  pit-report  - Abre relatório de mutação"
        echo "  help        - Mostra este help"
        echo ""
        echo "Exemplos:"
        echo "  ./run-tests.sh all"
        echo "  ./run-tests.sh unit"
        echo "  ./run-tests.sh smoke"
        echo "  ./run-tests.sh mutation"
        ;;
    *)
        print_error "Tipo de teste desconhecido: $TEST_TYPE"
        echo "Use './run-tests.sh help' para ver opções disponíveis"
        exit 1
        ;;
esac

exit 0
