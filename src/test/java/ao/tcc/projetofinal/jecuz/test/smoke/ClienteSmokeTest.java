package ao.tcc.projetofinal.jecuz.test.smoke;

import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteRequest;
import ao.tcc.projetofinal.jecuz.repositories.ClienteRepository;
import ao.tcc.projetofinal.jecuz.test.fixtures.TestContainersBaseClass;
import ao.tcc.projetofinal.jecuz.test.fixtures.TestDataBuilder;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * Testes de Fumaça (Smoke Tests) para funcionalidades críticas.
 * São testes rápidos que verificam se as funcionalidades principais estão funcionando.
 * Tag: @Tag("smoke") para executar separadamente: mvn test -Dgroups=smoke
 */
@DisplayName("Cliente - Testes de Fumaça (Smoke Tests)")
@Tag("smoke")
class ClienteSmokeTest extends TestContainersBaseClass {

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
    @DisplayName("[Smoke] Deve criar cliente com sucesso")
    void smoke_DeveAplicacaoResponder() {
        // Given
        ClienteRequest request = TestDataBuilder.clienteBuilder()
                .withNome("Smoke Test Cliente")
                .buildRequest();

        // When/Then: Deve responder com sucesso
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/clientes")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("nome", notNullValue());
    }

    @Test
    @DisplayName("[Smoke] Deve listar clientes com sucesso")
    void smoke_DeveListarClientes() {
        // Given: Criar cliente
        ClienteRequest request = TestDataBuilder.clienteBuilder().buildRequest();
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/clientes");

        // When/Then: Deve listar
        given()
                .when()
                .get("/api/clientes")
                .then()
                .statusCode(200)
                .body("content", notNullValue());
    }

    @Test
    @DisplayName("[Smoke] Deve recuperar cliente por ID")
    void smoke_DeveRecuperarClientePorID() {
        // Given: Criar cliente
        ClienteRequest request = TestDataBuilder.clienteBuilder().buildRequest();
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

        // When/Then: Deve recuperar
        given()
                .when()
                .get("/api/clientes/" + clienteId)
                .then()
                .statusCode(200)
                .body("id", equalTo(clienteId.intValue()));
    }

    @Test
    @DisplayName("[Smoke] Deve deletar cliente com sucesso")
    void smoke_DeveDeletarCliente() {
        // Given: Criar cliente
        ClienteRequest request = TestDataBuilder.clienteBuilder().buildRequest();
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

        // When/Then: Deve deletar
        given()
                .when()
                .delete("/api/clientes/" + clienteId)
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("[Smoke] Deve atualizar cliente com sucesso")
    void smoke_DeveAtualizarCliente() {
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

        // When: Atualizar
        ClienteRequest updateRequest = TestDataBuilder.clienteBuilder()
                .withNome("Atualizado")
                .buildRequest();

        given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .put("/api/clientes/" + clienteId)
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("[Smoke] Aplicação deve estar saudável")
    void smoke_AplicacaoSaudavel() {
        // When/Then: Deve responder com status 200 ou 404 (não 500)
        given()
                .when()
                .get("/api/clientes")
                .then()
                .statusCode(not(500));
    }

    @Test
    @DisplayName("[Smoke] Validação deve rejeitar dados inválidos")
    void smoke_DeveValidarDados() {
        // Given: Request inválido
        String requestInvalido = "{\"nome\": \"\"}"; // nome vazio

        // When/Then
        given()
                .contentType(ContentType.JSON)
                .body(requestInvalido)
                .when()
                .post("/api/clientes")
                .then()
                .statusCode(400);
    }
}
