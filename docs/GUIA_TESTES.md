# 🧪 Testes do Projeto Jecuz - Guia Completo

## 📊 Resumo dos Testes Criados

### 1️⃣ Testes Unitários (Unit Tests)
**Padrão de arquivo:** `*Test.java` ou `*UnitTest.java`

| Classe | Testes | Propósito |
|--------|--------|----------|
| ClienteServiceUnitTest | 5 | Testar lógica de serviço de Cliente |
| DiaristaServiceTest | ~5 | Testar lógica de serviço de Diarista |
| OrdensDeServicoServiceTest | ~5 | Testar lógica de serviço de Ordens |

**Como executar:**
```bash
mvn clean test -Dtest=*Test,*UnitTest
```

---

### 2️⃣ Testes de Integração (Integration Tests)
**Padrão de arquivo:** `*IT.java` (IT = Integration Test)

| Classe | Testes | Propósito |
|--------|--------|----------|
| ClienteControllerIT | 4 | Testar HTTP + Service do Cliente |
| DiaristaControllerIT | 4 | Testar HTTP + Service de Diarista |
| OrdensDeServicoControllerIT | ~4 | Testar HTTP + Service de Ordens |

**Como executar:**
```bash
mvn clean test -Dtest=*IT
```

---

### 3️⃣ Testes End-to-End (E2E)
**Padrão de arquivo:** `*E2ETest.java`

| Classe | Testes | Propósito |
|--------|--------|----------|
| ApplicationE2ETest | 1 | Fluxo E2E completo da aplicação |
| ClienteE2ETest | 3 | Ciclo completo: criar → ler → listar |

**Como executar:**
```bash
mvn clean test -Dtest=*E2ETest
```

**Características:**
- ✅ Usa TestContainers com PostgreSQL real
- ✅ Testa fluxos completos realistas
- ✅ Banco de dados isolado por teste
- ✅ RestAssured para requisições HTTP

---

### 4️⃣ Testes de Fumaça (Smoke Tests)
**Padrão de arquivo:** `*SmokeTest.java`

| Classe | Testes | Propósito |
|--------|--------|----------|
| SmokeTest | 2 | Validar saúde básica da aplicação |
| DiaristaApiSmokeTest | 7 | Validar endpoints críticos |

**Como executar:**
```bash
mvn clean test -Dtest=*SmokeTest
```

**Características:**
- ✅ Execução rápida (poucos segundos)
- ✅ Valida health endpoints
- ✅ Detecta falhas críticas
- ✅ Testa resposta de endpoints

---

### 5️⃣ Testes de Mutação (Mutation Testing)
**Padrão de arquivo:** `*MutationTest.java`

| Classe | Testes | Propósito |
|--------|--------|----------|
| ClienteServiceMutationTest | 7 | Validar qualidade dos testes Cliente |
| DiaristaServiceMutationTest | 6 | Validar qualidade dos testes Diarista |
| OrdensDeServicoServiceMutationTest | 6 | Validar qualidade dos testes Ordens |

**Como executar:**
```bash
mvn clean test org.pitest:pitest-maven:mutationCoverage
```

**Características:**
- ✅ Valida operators (>, <, ==, !=)
- ✅ Valida valores e tipos
- ✅ Detecta testes inefetivos
- ✅ Gera relatório HTML no target/pit-reports

---

## 🚀 Comandos Rápidos

### Executar Todos os Testes
```bash
./run-tests-enhanced.sh all
# ou
mvn clean test verify
```

### Executar por Categoria
```bash
# Testes Unitários
mvn clean test -Dtest=*Test,*UnitTest

# Testes de Integração
mvn clean test -Dtest=*IT

# Testes E2E
mvn clean test -Dtest=*E2ETest

# Testes de Fumaça
mvn clean test -Dtest=*SmokeTest

# Testes de Mutação
mvn org.pitest:pitest-maven:mutationCoverage

# Todos com Cobertura
mvn clean verify jacoco:report
```

---

## 📈 Cobertura de Código

### JaCoCo (Cobertura Tradicional)
```bash
mvn clean test jacoco:report
```
Relatório: `target/site/jacoco/index.html`

### PIT (Mutation Testing)
```bash
mvn org.pitest:pitest-maven:mutationCoverage
```
Relatório: `target/pit-reports/index.html`

---

## 🔍 Estrutura de Testes por Módulo

### Cliente
```
├── ClienteServiceUnitTest
├── ClienteControllerIT
├── ClienteE2ETest (parcial)
└── ClienteServiceMutationTest
```

### Diarista
```
├── DiaristaServiceTest
├── DiaristaControllerIT
├── DiaristaApiSmokeTest
└── DiaristaServiceMutationTest
```

### Ordens de Serviço
```
├── OrdensDeServicoServiceTest
├── OrdensDeServicoControllerIT
├── ApplicationE2ETest (parcial)
└── OrdensDeServicoServiceMutationTest
```

---

## 🛠️ Tecnologias por Tipo de Teste

| Tipo | Tecnologias |
|------|-------------|
| Unit | JUnit 5, Mockito, AssertJ |
| Integration | Spring Test, MockMvc, Mockito |
| E2E | TestContainers, RestAssured, PostgreSQL |
| Smoke | Spring Boot Test, TestRestTemplate |
| Mutation | PIT, Pitest Maven Plugin |

---

## 📋 Checklist de Validação

- [x] Testes unitários implementados
- [x] Testes de integração implementados
- [x] Testes E2E implementados
- [x] Testes de fumaça implementados
- [x] Testes de mutação implementados
- [x] SonarCloud configurado corretamente
- [x] JaCoCo configurado
- [x] PIT (Pitest) configurado
- [x] Documentação criada
- [x] Scripts de teste criados

---

## 🎯 Métricas Esperadas

| Métrica | Alvo | Método |
|---------|------|--------|
| Code Coverage | > 80% | JaCoCo |
| Mutation Score | > 80% | PIT |
| Line Coverage | > 75% | JaCoCo |
| Branch Coverage | > 70% | JaCoCo |

---

## 📞 Troubleshooting

### TestContainers não funciona
```
Certifique-se de que Docker está instalado e em execução
docker version  # Verificar instalação
```

### Testes lentos
```
# Executar apenas testes rápidos (Smoke)
mvn clean test -Dtest=*SmokeTest
```

### Sem acesso ao SonarCloud
```
# Testes locais sem SonarCloud
mvn clean test jacoco:report
# Abrir relatório em target/site/jacoco/index.html
```

---

## 📚 Referências Adicionais

- [JUnit 5 Documentation](https://junit.org/junit5/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot Test Guide](https://spring.io/guides/gs/testing-web/)
- [TestContainers Documentation](https://www.testcontainers.org/)
- [PIT Mutation Testing](https://pitest.org/)

---

**Última atualização:** 2026-02-19  
**Versão:** 1.0  
**Status:** ✅ Pronto para uso

