#!/bin/bash

# Script para executar diferentes tipos de testes no projeto Jecuz
# Compatível com Windows (Git Bash) e Linux/macOS
# Uso: ./run-tests.sh [tipo]

set -e

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_header() {
    echo ""
    echo -e "${BLUE}════════════════════════════════════════════════════════════${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}════════════════════════════════════════════════════════════${NC}"
    echo ""
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_info() {
    echo -e "${BLUE}ℹ $1${NC}"
}

# Verificar argumentos
TEST_TYPE=${1:-help}

case $TEST_TYPE in
    all)
        print_header "Executando TODOS os testes"
        print_info "Incluindo: Unit, Slice, Integration, E2E, Smoke"
        mvn clean test
        print_success "Todos os testes executados com sucesso!"
        ;;

    unit)
        print_header "Executando testes de UNIDADE"
        print_info "ClienteServiceMutationTest, DiaristaServiceMutationTest, OrdensDeServicoServiceMutationTest"
        mvn test -Dtest="**/*UnitTest,**/*MutationTest"
        print_success "Testes de unidade executados!"
        ;;

    slice)
        print_header "Executando testes de SLICE"
        print_info "ClienteRepositorySliceTest, DiaristaRepositorySliceTest, OrdensDeServicoRepositorySliceTest"
        mvn test -Dtest="**/*SliceTest"
        print_success "Testes de slice executados!"
        ;;

    integration)
        print_header "Executando testes de INTEGRAÇÃO"
        print_info "Controllers e integração com Spring Context"
        mvn test -Dtest="**/*IT,**/*IntegrationTest,**/*ControllerTest"
        print_success "Testes de integração executados!"
        ;;

    e2e)
        print_header "Executando testes E2E (Ponta a Ponta)"
        print_info "ClienteE2ETest, DiaristaE2ETest, OrdensDeServicoE2ETest"
        mvn test -Dtest="**/*E2ETest"
        print_success "Testes E2E executados!"
        ;;

    smoke)
        print_header "Executando testes de FUMAÇA (Smoke Tests)"
        print_info "Funcionalidades críticas - Rápido e essencial"
        mvn test -Dgroups="smoke"
        print_success "Testes de fumaça executados!"
        ;;

    mutation)
        print_header "Executando Mutation Testing com PIT"
        print_info "Validando qualidade dos testes (pode levar 10-15 minutos)"
        print_info "Relatório em: target/pit-reports/index.html"
        mvn clean org.pitest:pitest-maven:mutationCoverage verify
        print_success "Mutation testing completo!"
        print_info "Abra o relatório: target/pit-reports/index.html"
        ;;

    coverage)
        print_header "Gerando relatórios de COBERTURA (JaCoCo)"
        print_info "Compilando código e executando testes..."
        mvn clean test
        print_success "Cobertura gerada!"
        print_info "Abra o relatório: target/site/jacoco/index.html"
        ;;

    report)
        print_header "Abrindo relatório JaCoCo"
        if [ -f "target/site/jacoco/index.html" ]; then
            if command -v xdg-open &> /dev/null; then
                xdg-open target/site/jacoco/index.html
            elif command -v open &> /dev/null; then
                open target/site/jacoco/index.html
            else
                print_info "Abra manualmente: target/site/jacoco/index.html"
            fi
            print_success "Abrindo relatório JaCoCo..."
        else
            print_error "Relatório não encontrado!"
            print_info "Execute primeiro: mvn test"
        fi
        ;;

    pit-report)
        print_header "Abrindo relatório PIT (Mutation)"
        if [ -f "target/pit-reports/index.html" ]; then
            if command -v xdg-open &> /dev/null; then
                xdg-open target/pit-reports/index.html
            elif command -v open &> /dev/null; then
                open target/pit-reports/index.html
            else
                print_info "Abra manualmente: target/pit-reports/index.html"
            fi
            print_success "Abrindo relatório PIT..."
        else
            print_error "Relatório não encontrado!"
            print_info "Execute primeiro: ./run-tests.sh mutation"
        fi
        ;;

    compile)
        print_header "Compilando projeto"
        mvn clean compile
        print_success "Compilação concluída!"
        ;;

    quick)
        print_header "Execução RÁPIDA (Unit + Smoke)"
        print_info "Apenas testes essenciais (< 1 minuto)"
        mvn test -Dtest="**/*UnitTest,**/*MutationTest" -Dgroups="smoke"
        print_success "Testes rápidos executados!"
        ;;

    client)
        print_header "Testes de CLIENTE"
        print_info "ClienteServiceMutationTest, ClienteRepositorySliceTest, ClienteE2ETest, ClienteSmokeTest"
        mvn test -Dtest="Cliente*"
        print_success "Testes de Cliente executados!"
        ;;

    diarista)
        print_header "Testes de DIARISTA"
        print_info "DiaristaServiceMutationTest, DiaristaRepositorySliceTest, DiaristaE2ETest, DiaristasSmokeTest"
        mvn test -Dtest="Diarista*"
        print_success "Testes de Diarista executados!"
        ;;

    ordens)
        print_header "Testes de ORDENS DE SERVIÇO"
        print_info "OrdensDeServicoServiceMutationTest, OrdensDeServicoRepositorySliceTest, OrdensDeServicoE2ETest, OrdensDeServicoSmokeTest"
        mvn test -Dtest="OrdensDeServico*"
        print_success "Testes de Ordens de Serviço executados!"
        ;;

    docs)
        print_header "Documentação de Testes"
        print_info "Arquivos:"
        echo "  - TEST_SUITE_DOCUMENTATION.md (Guia completo)"
        echo "  - TESTS_SUMMARY.md (Sumário executivo)"
        echo "  - BEST_PRACTICES.md (Melhores práticas)"
        echo "  - QUICK_START_TESTS.md (Quick start)"
        echo ""
        print_info "Abra em um editor:"
        echo "  code src/test/java/ao/tcc/projetofinal/jecuz/test/TEST_SUITE_DOCUMENTATION.md"
        ;;

    help|--help|-h)
        print_header "Jecuz - Suíte Completa de Testes"
        echo ""
        echo "Uso: ./run-tests.sh [comando]"
        echo ""
        echo -e "${YELLOW}Comandos principais:${NC}"
        echo "  all              - Executa todos os testes"
        echo "  unit             - Testes de unidade"
        echo "  slice            - Testes de slice (repositories)"
        echo "  integration      - Testes de integração (controllers)"
        echo "  e2e              - Testes ponta a ponta"
        echo "  smoke            - Testes de fumaça (críticos)"
        echo "  mutation         - Mutation testing com PIT"
        echo "  coverage         - Gera relatório JaCoCo"
        echo ""
        echo -e "${YELLOW}Utilitários:${NC}"
        echo "  quick            - Apenas Unit + Smoke (rápido)"
        echo "  client           - Todos os testes de Cliente"
        echo "  diarista         - Todos os testes de Diarista"
        echo "  ordens           - Todos os testes de Ordens"
        echo "  compile          - Apenas compilar"
        echo "  report           - Abrir relatório JaCoCo"
        echo "  pit-report       - Abrir relatório PIT"
        echo "  docs             - Ver documentação"
        echo "  help             - Esta mensagem"
        echo ""
        echo -e "${YELLOW}Exemplos:${NC}"
        echo "  ./run-tests.sh all        # Todos os testes"
        echo "  ./run-tests.sh unit       # Apenas testes de unidade"
        echo "  ./run-tests.sh smoke      # Testes de fumaça"
        echo "  ./run-tests.sh quick      # Execução rápida"
        echo "  ./run-tests.sh coverage   # Gerar cobertura"
        echo "  ./run-tests.sh mutation   # Mutation testing (lento)"
        echo ""
        ;;

    *)
        print_error "Comando desconhecido: '$TEST_TYPE'"
        echo ""
        echo "Use './run-tests.sh help' para ver opcões disponíveis"
        exit 1
        ;;
esac

exit 0
