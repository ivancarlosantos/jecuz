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
        print_info "Incluindo: Unit, Integration, E2E, Smoke, Mutation"

        print_info "1/5 - Executando testes unitários..."
        mvn clean test -Dtest="**/*Test,**/*UnitTest" -q
        print_success "Testes unitários concluídos"

        print_info "2/5 - Executando testes de integração..."
        mvn test -Dtest="**/*IT" -q
        print_success "Testes de integração concluídos"

        print_info "3/5 - Executando testes E2E..."
        mvn test -Dtest="**/*E2ETest" -q
        print_success "Testes E2E concluídos"

        print_info "4/5 - Executando testes de fumaça..."
        mvn test -Dtest="**/*SmokeTest" -q
        print_success "Testes de fumaça concluídos"

        print_info "5/5 - Gerando relatório de cobertura JaCoCo..."
        mvn jacoco:report -q
        print_success "Relatório de cobertura gerado"

        print_header "✓ TODOS OS TESTES CONCLUÍDOS COM SUCESSO!"
        print_info "Relatórios disponíveis em:"
        print_info "  • JaCoCo: target/site/jacoco/index.html"
        print_info "  • Surefire: target/surefire-reports/"
        print_info "  • Failsafe: target/failsafe-reports/"
        ;;

    unit)
        print_header "Executando testes de UNIDADE"
        print_info "Testes: ClienteServiceUnitTest, DiaristaServiceTest, OrdensDeServicoServiceTest"
        mvn clean test -Dtest="**/*Test,**/*UnitTest"
        print_success "Testes unitários executados com sucesso!"
        ;;

    mutation)
        print_header "Executando testes de MUTAÇÃO (PIT)"
        print_info "Analisando qualidade dos testes através de mutações de código"
        print_info "⚠️  Aviso: Isso pode levar alguns minutos..."
        mvn clean test org.pitest:pitest-maven:mutationCoverage
        print_success "Testes de mutação concluídos!"
        print_info "Relatório: target/pit-reports/index.html"
        ;;

    integration)
        print_header "Executando testes de INTEGRAÇÃO"
        print_info "Testes: ClienteControllerIT, DiaristaControllerIT, OrdensDeServicoControllerIT"
        mvn clean test -Dtest="**/*IT"
        print_success "Testes de integração executados com sucesso!"
        ;;

    e2e)
        print_header "Executando testes END-TO-END (E2E)"
        print_info "Testes: ApplicationE2ETest, ClienteE2ETest"
        mvn clean test -Dtest="**/*E2ETest"
        print_success "Testes E2E executados com sucesso!"
        ;;

    smoke)
        print_header "Executando testes de FUMAÇA (Smoke)"
        print_info "Testes: SmokeTest, DiaristaApiSmokeTest"
        mvn clean test -Dtest="**/*SmokeTest"
        print_success "Testes de fumaça executados com sucesso!"
        ;;

    coverage)
        print_header "Gerando relatório de COBERTURA (JaCoCo)"
        mvn clean test jacoco:report
        print_success "Relatório de cobertura gerado!"
        print_info "Abra: target/site/jacoco/index.html"
        ;;

    sonar)
        print_header "Executando análise SONARCLOUD"
        print_info "⚠️  Requer configuração válida de sonar.login"
        mvn clean verify sonar:sonar
        print_success "Análise SonarCloud concluída!"
        print_info "Acesse: https://sonarcloud.io"
        ;;

    verify)
        print_header "Executando VERIFY (Unit + Integration + Packaging)"
        mvn clean verify
        print_success "Verificação concluída com sucesso!"
        ;;

    help)
        print_header "JECUZ - Test Runner - Guia de Uso"
        echo "Sintaxe: ./run-tests-enhanced.sh [opção]"
        echo ""
        echo "Opções disponíveis:"
        echo ""
        echo -e "${GREEN}  all${NC}           - Executa todos os testes (padrão)"
        echo -e "${GREEN}  unit${NC}          - Testes unitários"
        echo -e "${GREEN}  mutation${NC}      - Testes de mutação (PIT)"
        echo -e "${GREEN}  integration${NC}   - Testes de integração"
        echo -e "${GREEN}  e2e${NC}           - Testes End-to-End"
        echo -e "${GREEN}  smoke${NC}         - Testes de fumaça"
        echo -e "${GREEN}  coverage${NC}      - Relatório de cobertura JaCoCo"
        echo -e "${GREEN}  sonar${NC}         - Análise SonarCloud"
        echo -e "${GREEN}  verify${NC}        - Maven verify"
        echo -e "${GREEN}  help${NC}          - Exibe este guia"
        echo ""
        ;;

    *)
        print_error "Opção inválida: $TEST_TYPE"
        echo "Use ./run-tests-enhanced.sh help"
        exit 1
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
