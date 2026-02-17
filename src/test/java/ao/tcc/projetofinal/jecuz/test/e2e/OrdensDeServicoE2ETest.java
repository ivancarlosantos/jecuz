package ao.tcc.projetofinal.jecuz.test.e2e;

import ao.tcc.projetofinal.jecuz.repositories.OrdensDeServicoRepository;
import ao.tcc.projetofinal.jecuz.test.fixtures.TestContainersBaseClass;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * Testes E2E para Ordens de Serviço.
 */
@DisplayName("OrdensDeServico - Testes E2E (Ponta a Ponta)")
class OrdensDeServicoE2ETest extends TestContainersBaseClass {

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
    @DisplayName("Fluxo E2E: Criar ordem de serviço")
    void testeFluxoCompleto_CriarOrdem() {
        // Given
        String requestBody = """
                {
                    "data": "2026-01-23",
                    "valor": 150.0,
                    "descricao": "Limpeza residencial"
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
                .body("id", notNullValue())
                .body("valor", equalTo(150.0f));
    }

    @Test
    @DisplayName("Fluxo E2E: Listar ordens de serviço")
    void testeFluxoCompleto_ListarOrdens() {
        // Given
        String requestBody = """
                {
                    "data": "2026-01-23",
                    "valor": 100.0,
                    "descricao": "Limpeza"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/ordens");

        // When/Then
        given()
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/api/ordens")
                .then()
                .statusCode(200)
                .body("content", notNullValue());
    }

    @Test
    @DisplayName("Fluxo E2E: Recuperar ordem por ID")
    void testeFluxoCompleto_RecuperarOrdem() {
        // Given
        String requestBody = """
                {
                    "data": "2026-01-23",
                    "valor": 200.0,
                    "descricao": "Faxina geral"
                }
                """;

        var createResponse = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/ordens")
                .then()
                .statusCode(201)
                .extract()
                .response();

        Long ordemId = createResponse.jsonPath().getLong("id");

        // When/Then
        given()
                .when()
                .get("/api/ordens/" + ordemId)
                .then()
                .statusCode(200)
                .body("id", equalTo(ordemId.intValue()))
                .body("valor", equalTo(200.0f));
    }

    @Test
    @DisplayName("Fluxo E2E: Atualizar ordem de serviço")
    void testeFluxoCompleto_AtualizarOrdem() {
        // Given
        String createRequest = """
                {
                    "data": "2026-01-23",
                    "valor": 100.0,
                    "descricao": "Original"
                }
                """;

        var createResponse = given()
                .contentType(ContentType.JSON)
                .body(createRequest)
                .when()
                .post("/api/ordens")
                .then()
                .statusCode(201)
                .extract()
                .response();

        Long ordemId = createResponse.jsonPath().getLong("id");

        // When
        String updateRequest = """
                {
                    "data": "2026-01-23",
                    "valor": 250.0,
                    "descricao": "Atualizado"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .put("/api/ordens/" + ordemId)
                .then()
                .statusCode(200)
                .body("valor", equalTo(250.0f));
    }

    @Test
    @DisplayName("Fluxo E2E: Deletar ordem de serviço")
    void testeFluxoCompleto_DeletarOrdem() {
        // Given
        String requestBody = """
                {
                    "data": "2026-01-23",
                    "valor": 150.0,
                    "descricao": "Teste"
                }
                """;

        var createResponse = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/ordens")
                .then()
                .statusCode(201)
                .extract()
                .response();

        Long ordemId = createResponse.jsonPath().getLong("id");

        // When
        given()
                .when()
                .delete("/api/ordens/" + ordemId)
                .then()
                .statusCode(204);

        // Then
        given()
                .when()
                .get("/api/ordens/" + ordemId)
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Fluxo E2E: Validar campos obrigatórios")
    void testeFluxoCompleto_ValidarCamposObrigatorios() {
        // Given
        String requestInvalido = "{}";

        // When/Then
        given()
                .contentType(ContentType.JSON)
                .body(requestInvalido)
                .when()
                .post("/api/ordens")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Fluxo E2E: Validar valor negativo deve ser rejeitado")
    void testeFluxoCompleto_ValidarValorNegativo() {
        // Given
        String requestInvalido = """
                {
                    "data": "2026-01-23",
                    "valor": -100.0,
                    "descricao": "Inválido"
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

    @Test
    @DisplayName("Fluxo E2E: Múltiplas ordens com diferentes valores")
    void testeFluxoCompleto_MultiplasordensDiferentes() {
        // Given
        for (int i = 1; i <= 3; i++) {
            String requestBody = String.format("""
                    {
                        "data": "2026-01-23",
                        "valor": %d,
                        "descricao": "Ordem %d"
                    }
                    """, i * 100, i);

            given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/api/ordens");
        }

        // When/Then
        given()
                .when()
                .get("/api/ordens")
                .then()
                .statusCode(200)
                .body("content.size()", greaterThanOrEqualTo(3));
    }
}
