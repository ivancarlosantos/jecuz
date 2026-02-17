package ao.tcc.projetofinal.jecuz.test.smoke;

import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaRequest;
import ao.tcc.projetofinal.jecuz.repositories.DiaristaRepository;
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
 * Testes de Fumaça para Diarista.
 */
@DisplayName("Diarista - Testes de Fumaça (Smoke Tests)")
@Tag("smoke")
class DiaristasSmokeTest extends TestContainersBaseClass {

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
    @DisplayName("[Smoke] Deve criar diarista com sucesso")
    void smoke_DeveAplicacaoResponder() {
        // Given
        DiaristaRequest request = TestDataBuilder.diaristaBuilder()
                .withNome("Smoke Test Diarista")
                .buildRequest();

        // When/Then
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/diaristas")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("[Smoke] Deve listar diaristas com sucesso")
    void smoke_DeveListarDiaristas() {
        // Given
        DiaristaRequest request = TestDataBuilder.diaristaBuilder().buildRequest();
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/diaristas");

        // When/Then
        given()
                .when()
                .get("/api/diaristas")
                .then()
                .statusCode(200)
                .body("content", notNullValue());
    }

    @Test
    @DisplayName("[Smoke] Deve recuperar diarista por ID")
    void smoke_DeveRecuperarDiaristaPorID() {
        // Given
        DiaristaRequest request = TestDataBuilder.diaristaBuilder().buildRequest();
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

        // When/Then
        given()
                .when()
                .get("/api/diaristas/" + diaristaId)
                .then()
                .statusCode(200)
                .body("id", equalTo(diaristaId.intValue()));
    }

    @Test
    @DisplayName("[Smoke] Deve deletar diarista com sucesso")
    void smoke_DeveDeletarDiarista() {
        // Given
        DiaristaRequest request = TestDataBuilder.diaristaBuilder().buildRequest();
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

        // When/Then
        given()
                .when()
                .delete("/api/diaristas/" + diaristaId)
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("[Smoke] Aplicação deve estar saudável")
    void smoke_AplicacaoSaudavel() {
        // When/Then
        given()
                .when()
                .get("/api/diaristas")
                .then()
                .statusCode(not(500));
    }

    @Test
    @DisplayName("[Smoke] Taxa diária deve ser positiva")
    void smoke_DeveValidarTaxaDiaria() {
        // Given: Diarista com taxa negativa deve ser rejeitado
        DiaristaRequest request = TestDataBuilder.diaristaBuilder()
                .withTaxaDiaria(-10.0)
                .buildRequest();

        // When/Then: Deve rejeitar
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/diaristas")
                .then()
                .statusCode(400);
    }
}
