package ao.tcc.projetofinal.jecuz.test.smoke;

import ao.tcc.projetofinal.jecuz.repositories.OrdensDeServicoRepository;
import ao.tcc.projetofinal.jecuz.test.fixtures.TestContainersBaseClass;
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
 * Testes de Fumaça para Ordens de Serviço.
 */
@DisplayName("OrdensDeServico - Testes de Fumaça (Smoke Tests)")
@Tag("smoke")
class OrdensDeServicoSmokeTest extends TestContainersBaseClass {

    @LocalServerPort
    int port;

    @Autowired
    OrdensDeServicoRepository ordensRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "";
        ordensRepository.deleteAll();
    }

    @Test
    @DisplayName("[Smoke] Deve criar ordem de serviço")
    void smoke_DeveAplicacaoResponder() {
        // Given
        String requestBody = """
                {
                    "data": "2026-01-23",
                    "valor": 100.0,
                    "descricao": "Teste"
                }
                """;

        // When/Then
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/ordens")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("[Smoke] Deve listar ordens")
    void smoke_DeveListarOrdens() {
        // When/Then
        given()
                .when()
                .get("/api/ordens")
                .then()
                .statusCode(200)
                .body("content", notNullValue());
    }

    @Test
    @DisplayName("[Smoke] Deve recuperar ordem por ID")
    void smoke_DeveRecuperarOrdem() {
        // Given
        String requestBody = """
                {
                    "data": "2026-01-23",
                    "valor": 150.0,
                    "descricao": "Smoke Test"
                }
                """;

        var response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/ordens")
                .then()
                .statusCode(201)
                .extract()
                .response();

        Long ordemId = response.jsonPath().getLong("id");

        // When/Then
        given()
                .when()
                .get("/api/ordens/" + ordemId)
                .then()
                .statusCode(200)
                .body("id", equalTo(ordemId.intValue()));
    }

    @Test
    @DisplayName("[Smoke] Deve deletar ordem")
    void smoke_DeveDeletarOrdem() {
        // Given
        String requestBody = """
                {
                    "data": "2026-01-23",
                    "valor": 100.0,
                    "descricao": "Delete Test"
                }
                """;

        var response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/ordens")
                .then()
                .statusCode(201)
                .extract()
                .response();

        Long ordemId = response.jsonPath().getLong("id");

        // When/Then
        given()
                .when()
                .delete("/api/ordens/" + ordemId)
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("[Smoke] Aplicação deve estar saudável")
    void smoke_AplicacaoSaudavel() {
        // When/Then
        given()
                .when()
                .get("/api/ordens")
                .then()
                .statusCode(not(500));
    }

    @Test
    @DisplayName("[Smoke] Deve validar valor positivo")
    void smoke_DeveValidarValor() {
        // Given
        String requestInvalido = """
                {
                    "data": "2026-01-23",
                    "valor": -50.0,
                    "descricao": "Invalid"
                }
                """;

        // When/Then
        given()
                .contentType(ContentType.JSON)
                .body(requestInvalido)
                .when()
                .post("/api/ordens")
                .then()
                .statusCode(400);
    }
}
