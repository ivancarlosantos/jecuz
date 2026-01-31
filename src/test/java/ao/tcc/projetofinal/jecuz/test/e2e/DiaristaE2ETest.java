package ao.tcc.projetofinal.jecuz.test.e2e;

import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaRequest;
import ao.tcc.projetofinal.jecuz.repositories.DiaristaRepository;
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
import static org.hamcrest.Matchers.*;

/**
 * Testes E2E para fluxos completos de Diarista.
 */
@DisplayName("Diarista - Testes E2E (Ponta a Ponta)")
class DiaristaE2ETest extends TestContainersBaseClass {

    @LocalServerPort
    int port;

    @Autowired
    DiaristaRepository diaristaRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "";
        diaristaRepository.deleteAll();
    }

    @Test
    @DisplayName("Fluxo E2E: Criar e recuperar diarista")
    void testeFluxoCompleto_CriarERecuperarDiarista() {
        // Given
        DiaristaRequest request = TestDataBuilder.diaristaBuilder()
                .withNome("Diarista E2E")
                .withEspecialidade("Limpeza Residencial")
                .withTaxaDiaria(80.0)
                .buildRequest();

        // When: Criar diarista
        var response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/diaristas")
                .then()
                .statusCode(201)
                .extract()
                .response();

        Long diaristaId = response.jsonPath().getLong("id");

        // Then: Recuperar e validar
        given()
                .when()
                .get("/api/diaristas/" + diaristaId)
                .then()
                .statusCode(200)
                .body("id", equalTo(diaristaId.intValue()))
                .body("nome", equalTo("Diarista E2E"))
                .body("especialidade", equalTo("Limpeza Residencial"))
                .body("taxaDiaria", equalTo(80.0f));
    }

    @Test
    @DisplayName("Fluxo E2E: Listar diaristas com filtro")
    void testeFluxoCompleto_ListarDiaristasFiltrado() {
        // Given: Criar diaristas
        for (int i = 0; i < 2; i++) {
            DiaristaRequest request = TestDataBuilder.diaristaBuilder()
                    .withNome("Diarista " + i)
                    .buildRequest();
            given()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/api/diaristas");
        }

        // When/Then: Listar
        given()
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/api/diaristas")
                .then()
                .statusCode(200)
                .body("content.size()", greaterThanOrEqualTo(2));
    }

    @Test
    @DisplayName("Fluxo E2E: Atualizar taxa diária")
    void testeFluxoCompleto_AtualizarTaxaDiaria() {
        // Given: Criar diarista
        DiaristaRequest createRequest = TestDataBuilder.diaristaBuilder()
                .withTaxaDiaria(60.0)
                .buildRequest();

        var createResponse = given()
                .contentType(ContentType.JSON)
                .body(createRequest)
                .when()
                .post("/api/diaristas")
                .then()
                .statusCode(201)
                .extract()
                .response();

        Long diaristaId = createResponse.jsonPath().getLong("id");

        // When: Atualizar taxa
        DiaristaRequest updateRequest = TestDataBuilder.diaristaBuilder()
                .withTaxaDiaria(100.0)
                .buildRequest();

        given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .put("/api/diaristas/" + diaristaId)
                .then()
                .statusCode(200)
                .body("taxaDiaria", equalTo(100.0f));
    }

    @Test
    @DisplayName("Fluxo E2E: Deletar diarista")
    void testeFluxoCompleto_DeletarDiarista() {
        // Given: Criar diarista
        DiaristaRequest request = TestDataBuilder.diaristaBuilder().buildRequest();

        var createResponse = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/diaristas")
                .then()
                .statusCode(201)
                .extract()
                .response();

        Long diaristaId = createResponse.jsonPath().getLong("id");

        // When: Deletar
        given()
                .when()
                .delete("/api/diaristas/" + diaristaId)
                .then()
                .statusCode(204);

        // Then: Verificar deleção
        given()
                .when()
                .get("/api/diaristas/" + diaristaId)
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Fluxo E2E: Validar campos obrigatórios")
    void testeFluxoCompleto_ValidarCamposObrigatorios() {
        // Given: Request inválido
        String requestInvalido = "{}";

        // When/Then
        given()
                .contentType(ContentType.JSON)
                .body(requestInvalido)
                .when()
                .post("/api/diaristas")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Fluxo E2E: Verificar dados completos da diarista")
    void testeFluxoCompleto_VerificarDadosCompletos() {
        // Given
        DiaristaRequest request = TestDataBuilder.diaristaBuilder()
                .withNome("Maria Limpeza")
                .withEspecialidade("Faxina Geral")
                .withTaxaDiaria(85.0)
                .buildRequest();

        var createResponse = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/diaristas")
                .then()
                .statusCode(201)
                .extract()
                .response();

        Long diaristaId = createResponse.jsonPath().getLong("id");

        // When/Then
        given()
                .when()
                .get("/api/diaristas/" + diaristaId)
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("nome", equalTo("Maria Limpeza"))
                .body("especialidade", equalTo("Faxina Geral"))
                .body("taxaDiaria", equalTo(85.0f));
    }
}
