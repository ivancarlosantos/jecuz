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
        // Given
        ClienteRequest request = TestDataBuilder.clienteBuilder()
                .withNome("Cliente E2E Test")
                .withEmail("e2e@test.com")
                .buildRequest();

        // When: Criar cliente
        var response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/clientes")
                .then()
                .statusCode(201)
                .extract()
                .response();

        Long clienteId = response.jsonPath().getLong("id");

        // Then: Verificar se foi criado
        assertThat(clienteId).isNotNull().isPositive();

        // When: Recuperar cliente criado
        given()
                .when()
                .get("/api/clientes/" + clienteId)
                .then()
                .statusCode(200)
                .body("id", equalTo(clienteId.intValue()))
                .body("nome", equalTo("Cliente E2E Test"))
                .body("email", equalTo("e2e@test.com"));
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
                    .post("/api/clientes");
        }

        // When: Listar clientes
        given()
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/api/clientes")
                .then()
                .statusCode(200)
                .body("content.size()", greaterThanOrEqualTo(3));
    }

    @Test
    @DisplayName("Fluxo E2E: Atualizar cliente")
    void testeFluxoCompleto_AtualizarCliente() {
        // Given: Criar cliente
        ClienteRequest createRequest = TestDataBuilder.clienteBuilder()
                .withNome("Original")
                .buildRequest();

        var createResponse = given()
                .contentType(ContentType.JSON)
                .body(createRequest)
                .when()
                .post("/api/clientes")
                .then()
                .statusCode(201)
                .extract()
                .response();

        Long clienteId = createResponse.jsonPath().getLong("id");

        // When: Atualizar cliente
        ClienteRequest updateRequest = TestDataBuilder.clienteBuilder()
                .withNome("Atualizado")
                .buildRequest();

        given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .put("/api/clientes/" + clienteId)
                .then()
                .statusCode(200)
                .body("nome", equalTo("Atualizado"));
    }

    @Test
    @DisplayName("Fluxo E2E: Deletar cliente")
    void testeFluxoCompleto_DeletarCliente() {
        // Given: Criar cliente
        ClienteRequest request = TestDataBuilder.clienteBuilder().buildRequest();

        var createResponse = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/clientes")
                .then()
                .statusCode(201)
                .extract()
                .response();

        Long clienteId = createResponse.jsonPath().getLong("id");

        // When: Deletar cliente
        given()
                .when()
                .delete("/api/clientes/" + clienteId)
                .then()
                .statusCode(204);

        // Then: Verificar que foi deletado
        given()
                .when()
                .get("/api/clientes/" + clienteId)
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Fluxo E2E: Validar campos obrigatórios")
    void testeFluxoCompleto_ValidarCamposObrigatorios() {
        // Given: Request com campos inválidos
        String requestInvalido = "{}";

        // When/Then: Deve falhar
        given()
                .contentType(ContentType.JSON)
                .body(requestInvalido)
                .when()
                .post("/api/clientes")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Fluxo E2E: Alternar status do cliente")
    void testeFluxoCompleto_AlternarStatusCliente() {
        // Given: Criar cliente com status ATIVO
        ClienteRequest request = TestDataBuilder.clienteBuilder()
                .buildRequest();

        var createResponse = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/clientes")
                .then()
                .statusCode(201)
                .extract()
                .response();

        Long clienteId = createResponse.jsonPath().getLong("id");

        // When: Alterar status
        given()
                .when()
                .patch("/api/clientes/" + clienteId + "/status?status=INATIVO")
                .then()
                .statusCode(200)
                .body("status", equalTo("INATIVO"));
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
                .post("/api/clientes")
                .then()
                .statusCode(201)
                .extract()
                .response();

        Long clienteId = createResponse.jsonPath().getLong("id");

        // When/Then: Verificar todos os dados
        given()
                .when()
                .get("/api/clientes/" + clienteId)
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("nome", equalTo("João Silva"))
                .body("email", equalTo("joao@example.com"))
                .body("telefone", notNullValue())
                .body("status", notNullValue());
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
                .post("/api/clientes")
                .then()
                .statusCode(201)
                .extract()
                .response();

        Long clienteId = createResponse.jsonPath().getLong("id");

        given()
                .when()
                .get("/api/clientes/" + clienteId)
                .then()
                .statusCode(200);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // Then: Deve ser rápido (< 5 segundos)
        assertThat(duration).isLessThan(5000);
    }
}
