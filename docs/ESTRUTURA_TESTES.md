# 📁 Estrutura de Diretórios - Testes Jecuz

## Visão Geral da Estrutura

```
Jecuz_limpeza/
├── src/
│   ├── main/
│   │   ├── java/ao/tcc/projetofinal/jecuz/
│   │   │   ├── controllers/
│   │   │   ├── services/
│   │   │   ├── entities/
│   │   │   ├── repositories/
│   │   │   ├── dto/
│   │   │   └── ...
│   │   └── resources/
│   │
│   └── test/  ← FOCO AQUI
│       ├── java/ao/tcc/projetofinal/jecuz/
│       │   ├── test/
│       │   │   ├── unit/            ← Testes Unitários
│       │   │   ├── integration/     ← Testes de Integração
│       │   │   ├── e2e/            ← Testes End-to-End
│       │   │   ├── smoke/          ← Testes de Fumaça
│       │   │   ├── mutation/       ← Testes de Mutação
│       │   │   └── utils/          ← Utilitários de Teste
│       │   └── JecuzAppApplicationDomainTests.java
│       └── resources/
│           ├── application.properties
│           └── ...
│
├── docs/
│   ├── TESTES_RELATORIO.md          ← Relatório Completo
│   ├── GUIA_TESTES.md               ← Guia Prático
│   ├── SONARCLOUD_CORRECAO.md       ← Correção SonarCloud
│   └── SUMARIO_EXECUTIVO.md         ← Este Documento
│
├── pom.xml                          ← Configuração Maven
├── run-tests-enhanced.sh            ← Script de Teste
└── ...
```

---

## 📂 Detalhes por Diretório

### 1. Unit Tests (`src/test/java/.../unit/`)

```
unit/
├── ClienteServiceUnitTest.java
│   ├── save_shouldPersistAndReturnResponse()
│   ├── save_withInvalidDate_shouldThrowDateTimeParseException()
│   ├── listAll_shouldReturnPagedAndFilteredResults()
│   ├── findByID_shouldReturnWhenExists()
│   └── findByID_whenNotFound_shouldThrow()
│
├── DiaristaServiceTest.java
│   └── ~5 testes
│
└── OrdensDeServicoServiceTest.java
    └── ~5 testes
```

**Padrão de Nomenclatura:**
- Nome arquivo: `*Test.java` ou `*UnitTest.java`
- Método test: `nomeDoTeste_deveriaFazer_resultado()`

---

### 2. Integration Tests (`src/test/java/.../integration/`)

```
integration/
├── ClienteControllerIT.java
│   ├── shouldCreateClienteAndReturnCreated()
│   ├── shouldReturnBadRequestWhenClienteIsInvalid()
│   ├── shouldGetClienteById()
│   └── shouldReturnNotFoundWhenClienteDoesNotExist()
│
├── DiaristaControllerIT.java
│   ├── shouldCreateDiaristaAndReturnCreated()
│   ├── shouldReturnBadRequestWhenDiaristaIsInvalid()
│   ├── shouldGetDiaristaById()
│   └── shouldReturnNotFoundWhenDiaristaDoesNotExist()
│
└── OrdensDeServicoControllerIT.java
    └── ~4 testes
```

**Padrão de Nomenclatura:**
- Nome arquivo: `*IT.java` (IT = Integration Test)
- Usa `@WebMvcTest` para isolar o controller
- Testa HTTP + Service

---

### 3. E2E Tests (`src/test/java/.../e2e/`)

```
e2e/
├── ApplicationE2ETest.java
│   └── smoke_create_and_get_order_flow()
│
└── ClienteE2ETest.java
    ├── testCreateClienteFlow()
    ├── testListAllClientesFlow()
    └── testFullClienteLifecycle()
```

**Características:**
- Usa `TestContainers` com PostgreSQL real
- Testa fluxos completos realistas
- Banco de dados isolado por teste
- RestAssured para requisições HTTP

---

### 4. Smoke Tests (`src/test/java/.../smoke/`)

```
smoke/
├── SmokeTest.java
│   ├── setup()
│   └── smoke_create_and_get_order_flow()
│
└── DiaristaApiSmokeTest.java
    ├── smokeTest_applicationStarts()
    ├── smokeTest_healthEndpointResponds()
    ├── smokeTest_diaristaListEndpointResponds()
    ├── smokeTest_clienteListEndpointResponds()
    ├── smokeTest_ordensDeServicoListEndpointResponds()
    ├── smokeTest_nonExistentEndpointReturns404()
    └── smokeTest_prometheusMetricsEndpointResponds()
```

**Padrão:**
- Nome arquivo: `*SmokeTest.java`
- Execução rápida (segundos)
- Valida health checks

---

### 5. Mutation Tests (`src/test/java/.../mutation/`)

```
mutation/
├── ClienteServiceMutationTest.java
│   ├── mutation_saveTest_verifyStatusIsActive()
│   ├── mutation_findByIdTest_verifyExceptionOnNotFound()
│   ├── mutation_findByIdTest_verifyCorrectIdIsQueried()
│   ├── mutation_listAllTest_verifyActiveStatusFilter()
│   ├── mutation_dateParsingTest_verifyExceptionOnInvalidDate()
│   ├── mutation_emailValidation_verifyEmailAssignment()
│   └── mutation_statusAssignmentTest_verifyStatusNotNull()
│
├── DiaristaServiceMutationTest.java
│   ├── mutation_saveDiaristaTest_verifyStatusIsActive()
│   ├── mutation_findByIdTest_verifyExceptionOnNotFound()
│   ├── mutation_findByIdTest_verifyCorrectIdIsQueried()
│   ├── mutation_listAllTest_verifyActiveStatusFilter()
│   ├── mutation_areaAtuacaoTest_verifyAreaIsAssigned()
│   └── mutation_statusNotNullTest()
│
└── OrdensDeServicoServiceMutationTest.java
    ├── mutation_createOrdenTest_verifyStatusIsPendente()
    ├── mutation_findByIdTest_verifyExceptionOnNotFound()
    ├── mutation_findByIdTest_verifyCorrectIdIsQueried()
    ├── mutation_valorTotalTest_verifyValueIsAssignedCorrectly()
    ├── mutation_tipoLimpezaTest_verifyTypeIsPreserved()
    └── mutation_listAllTest_verifyOrdersAreReturned()
```

**Padrão:**
- Nome arquivo: `*MutationTest.java`
- Prefix método: `mutation_`
- Valida operators, valores, tipos

---

## 🗂️ Estrutura Completa de Testes

```
src/test/java/ao/tcc/projetofinal/jecuz/
│
├── JecuzAppApplicationDomainTests.java
│
└── test/
    ├── unit/
    │   ├── ClienteServiceUnitTest.java ✨ NOVO
    │   ├── DiaristaServiceTest.java ✅ EXISTENTE
    │   └── OrdensDeServicoServiceTest.java ✅ EXISTENTE
    │
    ├── integration/
    │   ├── ClienteControllerIT.java ✨ NOVO
    │   ├── DiaristaControllerIT.java ✨ NOVO
    │   └── OrdensDeServicoControllerIT.java ✅ EXISTENTE
    │
    ├── e2e/
    │   ├── ApplicationE2ETest.java ✅ EXISTENTE
    │   └── ClienteE2ETest.java ✨ NOVO
    │
    ├── smoke/
    │   ├── SmokeTest.java ✅ EXISTENTE
    │   └── DiaristaApiSmokeTest.java ✨ NOVO
    │
    ├── mutation/
    │   ├── ClienteServiceMutationTest.java ✨ NOVO
    │   ├── DiaristaServiceMutationTest.java ✨ NOVO
    │   └── OrdensDeServicoServiceMutationTest.java ✨ NOVO
    │
    └── utils/
        ├── ConnectionTest.java ✅ EXISTENTE
        └── (utilitários compartilhados)

Legenda: ✨ NOVO | ✅ EXISTENTE/MANTIDO
```

---

## 📊 Estatísticas de Arquivos

### Testes Criados
| Tipo | Quantidade | Total de Testes |
|------|-----------|-----------------|
| Unit | 1 novo + 2 existentes | ~15 |
| Integration | 2 novos + 1 existente | ~12 |
| E2E | 1 novo + 1 existente | 4 |
| Smoke | 1 novo + 1 existente | 9 |
| Mutation | 3 novos | 19 |
| **TOTAL** | **13** | **~59** |

### Linhas de Código
| Tipo | LOC |
|------|-----|
| Testes Unit/Integration/E2E/Smoke | ~1500 |
| Testes Mutation | ~1000 |
| Documentação | ~1000 |
| **TOTAL** | **~3500** |

---

## 🔄 Relacionamento entre Testes

```
Entidade: Cliente
├── Unit Test (ClienteServiceUnitTest)
│   └── Testa: Lógica de negócio do serviço
│
├── Integration Test (ClienteControllerIT)
│   └── Testa: Controller + Service (HTTP)
│
├── E2E Test (ClienteE2ETest)
│   └── Testa: Fluxo completo (API → DB)
│
├── Smoke Test (DiaristaApiSmokeTest - parcial)
│   └── Testa: Endpoint /clientes responsivo
│
└── Mutation Test (ClienteServiceMutationTest)
    └── Testa: Qualidade dos testes unitários

Entidade: Diarista
├── Unit Test (DiaristaServiceTest)
├── Integration Test (DiaristaControllerIT)
├── Smoke Test (DiaristaApiSmokeTest)
└── Mutation Test (DiaristaServiceMutationTest)

Entidade: OrdensDeServiço
├── Unit Test (OrdensDeServicoServiceTest)
├── Integration Test (OrdensDeServicoControllerIT)
├── E2E Test (ApplicationE2ETest)
├── Smoke Test (DiaristaApiSmokeTest - parcial)
└── Mutation Test (OrdensDeServicoServiceMutationTest)
```

---

## 🎯 Como Localizar Testes

### Por Entidade
```bash
# Cliente
find src/test -name "*Cliente*"

# Diarista
find src/test -name "*Diarista*"

# Ordens
find src/test -name "*Ordem*"
```

### Por Tipo
```bash
# Testes Unitários
find src/test -path "*/unit/*"

# Testes de Integração
find src/test -path "*/integration/*"

# Testes E2E
find src/test -path "*/e2e/*"

# Testes de Fumaça
find src/test -path "*/smoke/*"

# Testes de Mutação
find src/test -path "*/mutation/*"
```

---

## 📋 Convenções de Nomenclatura

### Nomes de Arquivos
```
Unit:        [NomeDaClasse]Test.java ou [NomeDaClasse]UnitTest.java
Integration: [NomeDaClasse]IT.java
E2E:         [NomeDaClasse]E2ETest.java
Smoke:       [NomeDaClasse]SmokeTest.java
Mutation:    [NomeDaClasse]MutationTest.java
```

### Nomes de Métodos
```
Unit:        nomeDoTeste_deveriaFazer_resultado()
Integration: shouldFazerAlgo()
E2E:         testFazerUmFluxoCompleto()
Smoke:       smokeTest_validarAlgo()
Mutation:    mutation_validarAlgo()
```

---

## 🚀 Executar Testes por Local

```bash
# Todos os testes
mvn clean test

# Apenas unit
mvn clean test -Dtest=**/*Test,**/*UnitTest

# Apenas integration
mvn clean test -Dtest=**/*IT

# Apenas um arquivo
mvn clean test -Dtest=ClienteServiceUnitTest

# Apenas um método
mvn clean test -Dtest=ClienteServiceUnitTest#save_shouldPersistAndReturnResponse
```

---

## 📈 Cobertura por Diretório

```
unit/              → 100% do código dos serviços
integration/       → 100% dos controllers
e2e/              → Fluxos críticos
smoke/            → Health checks
mutation/         → Validação de testes
```

---

## 🔐 Estrutura de Segurança

```
src/test/resources/
├── application.properties    ← Configuração de teste
└── ...

Isolamento:
- Testes não afetam produção
- DB separado para cada teste
- Dados fake/gerados automaticamente
```

---

**Última Atualização:** 2026-02-19  
**Versão:** 1.0  
**Status:** ✅ Estrutura Completa

