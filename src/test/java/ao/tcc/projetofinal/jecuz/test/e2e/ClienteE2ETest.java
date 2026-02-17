package ao.tcc.projetofinal.jecuz.test.e2e;

import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteRequest;
import ao.tcc.projetofinal.jecuz.repositories.ClienteRepository;
import ao.tcc.projetofinal.jecuz.test.fixtures.TestContainersBaseClass;
import ao.tcc.projetofinal.jecuz.test.fixtures.TestDataBuilder;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Testes E2E para fluxos completos de Cliente.
 * Testa a aplicação inteira: request → controller → service → repository → banco.
 */
@DisplayName("Cliente - Testes E2E (Ponta a Ponta)")
class ClienteE2ETest extends TestContainersBaseClass {

    @LocalServerPort
    int port;

    @Autowired
    ClienteRepository clienteRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "";
        clienteRepository.deleteAll();
    }

    @Test
    @DisplayName("Fluxo E2E: Criar cliente e recuperar")
    void testeFluxoCompleto_CriarERecuperarCliente() {
        // Given: Mock um cliente válido
        ClienteRequest request = TestDataBuilder.clienteBuilder()
                .withNome("Cliente E2E Test")
                .withEmail("e2e@test.com")
                .withNumeroBi("0002505BA012")
                .buildRequest();

        // When: Criar cliente
        var response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/cliente/save")
                .then()
                .extract()
                .response();

        System.out.println("Response Status: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        assertThat(response.getStatusCode()).isEqualTo(201);

        String clienteId = response.jsonPath().getString("id");

        // Then: Verificar se foi criado
        assertThat(clienteId).isNotNull();

        // When: Recuperar cliente criado
        given()
                .queryParam("id", clienteId)
                .when()
                .get("/api/cliente/findByID")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("nome", equalTo("Cliente E2E Test"))
                .body("email", equalTo("e2e@test.com"))
                .body("numeroBi", equalTo("0002505BA012"));
    }

    @Test
    @DisplayName("Fluxo E2E: Listar clientes paginados")
    void testeFluxoCompleto_ListarClientesPaginados() {
        // Given: Criar múltiplos clientes
        for (int i = 0; i < 3; i++) {
            ClienteRequest request = TestDataBuilder.clienteBuilder()
                    .withNome("Cliente " + i)
                    .buildRequest();
            given()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/api/cliente/save")
                    .then()
                    .statusCode(201);
        }

        // When: Listar clientes
        given()
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/api/cliente/list")
                .then()
                .statusCode(200)
                .body("content.size()", greaterThanOrEqualTo(3));
    }

    @Test
    @DisplayName("Fluxo E2E: Criar e recuperar múltiplos clientes")
    void testeFluxoCompleto_CriarERecuperarMultiplos() {
        // Given: Criar dois clientes com dados diferentes
        ClienteRequest createRequest1 = TestDataBuilder.clienteBuilder()
                .withNome("Cliente Um")
                .withEmail("um@test.com")
                .buildRequest();

        var createResponse1 = given()
                .contentType(ContentType.JSON)
                .body(createRequest1)
                .when()
                .post("/api/cliente/save")
                .then()
                .statusCode(201)
                .extract()
                .response();

        String clienteId1 = createResponse1.jsonPath().getString("id");

        ClienteRequest createRequest2 = TestDataBuilder.clienteBuilder()
                .withNome("Cliente Dois")
                .withEmail("dois@test.com")
                .buildRequest();

        var createResponse2 = given()
                .contentType(ContentType.JSON)
                .body(createRequest2)
                .when()
                .post("/api/cliente/save")
                .then()
                .statusCode(201)
                .extract()
                .response();

        String clienteId2 = createResponse2.jsonPath().getString("id");

        // When/Then: Recuperar ambos e verificar
        given()
                .queryParam("id", clienteId1)
                .when()
                .get("/api/cliente/findByID")
                .then()
                .statusCode(200)
                .body("nome", equalTo("Cliente Um"));

        given()
                .queryParam("id", clienteId2)
                .when()
                .get("/api/cliente/findByID")
                .then()
                .statusCode(200)
                .body("nome", equalTo("Cliente Dois"));
    }

    @Test
    @DisplayName("Fluxo E2E: Validar campos obrigatórios")
    void testeFluxoCompleto_ValidarCamposObrigatorios() {
        // Given: Request com campos inválidos
        ClienteRequest requestInvalido = TestDataBuilder.clienteBuilder()
                .withNome("")  // Nome vazio/inválido
                .buildRequest();

        // When/Then: Deve falhar com 400
        given()
                .contentType(ContentType.JSON)
                .body(requestInvalido)
                .when()
                .post("/api/cliente/save")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Fluxo E2E: Buscar cliente e verificar dados completos")
    void testeFluxoCompleto_BuscarEVerificarDadosCompletos() {
        // Given: Criar cliente com dados específicos
        ClienteRequest request = TestDataBuilder.clienteBuilder()
                .withNome("João Silva")
                .withEmail("joao@example.com")
                .withTelefone("+244923456789")
                .buildRequest();

        var createResponse = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/cliente/save")
                .then()
                .statusCode(201)
                .extract()
                .response();

        String clienteId = createResponse.jsonPath().getString("id");

        // When/Then: Verificar todos os dados
        given()
                .queryParam("id", clienteId)
                .when()
                .get("/api/cliente/findByID")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("nome", equalTo("João Silva"))
                .body("email", equalTo("joao@example.com"));
    }

    @Test
    @DisplayName("Fluxo E2E: Performance - Criar e recuperar rápido")
    void testeFluxoCompleto_Performance() {
        // Given
        ClienteRequest request = TestDataBuilder.clienteBuilder().buildRequest();

        // When: Criar cliente e medir tempo
        long startTime = System.currentTimeMillis();

        var createResponse = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/cliente/save")
                .then()
                .statusCode(201)
                .extract()
                .response();

        String clienteId = createResponse.jsonPath().getString("id");

        given()
                .queryParam("id", clienteId)
                .when()
                .get("/api/cliente/findByID")
                .then()
                .statusCode(200);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // Then: Deve ser rápido (< 5 segundos)
        assertThat(duration).isLessThan(5000);
    }
}
