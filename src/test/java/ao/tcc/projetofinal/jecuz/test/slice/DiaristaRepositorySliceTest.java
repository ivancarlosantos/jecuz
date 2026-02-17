package ao.tcc.projetofinal.jecuz.test.slice;

import ao.tcc.projetofinal.jecuz.entities.Diarista;
import ao.tcc.projetofinal.jecuz.repositories.DiaristaRepository;
import ao.tcc.projetofinal.jecuz.test.fixtures.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes de Slice para DiaristaRepository.
 * Testa apenas a camada de persistência com banco real.
 */
@Testcontainers
@DataJpaTest
@DisplayName("DiaristaRepository - Testes de Slice")
class DiaristaRepositorySliceTest {

    @Container
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:15")
    ).withDatabaseName("test_diarista");

    @Autowired
    DiaristaRepository repository;

    private Diarista diaristaValida;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        diaristaValida = TestDataBuilder.diaristaBuilder()
                .withNome("Diarista Teste")
                .buildEntity();
    }

    @Test
    @DisplayName("Deve salvar diarista com sucesso")
    void testSaveDiarista() {
        // Given
        Diarista diarista = TestDataBuilder.diaristaBuilder()
                .withNome("Ana Clara")
                .withTaxaDiaria(75.0)
                .buildEntity();

        // When
        Diarista saved = repository.save(diarista);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getNome()).isEqualTo("Ana Clara");
    }

    @Test
    @DisplayName("Deve recuperar diarista pelo ID")
    void testFindDiaristaById() {
        // Given
        Diarista saved = repository.save(diaristaValida);

        // When
        Optional<Diarista> found = repository.findById(saved.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getNome()).isEqualTo(diaristaValida.getNome());
    }

    @Test
    @DisplayName("Deve listar todas as diaristas")
    void testFindAllDiaristas() {
        // Given
        Diarista diarista1 = TestDataBuilder.diaristaBuilder().withNome("Diarista 1").buildEntity();
        Diarista diarista2 = TestDataBuilder.diaristaBuilder().withNome("Diarista 2").buildEntity();
        repository.save(diarista1);
        repository.save(diarista2);

        // When
        List<Diarista> diaristas = repository.findAll();

        // Then
        assertThat(diaristas).hasSize(2);
        assertThat(diaristas).extracting(Diarista::getNome)
                .containsExactlyInAnyOrder("Diarista 1", "Diarista 2");
    }

    @Test
    @DisplayName("Deve deletar diarista por ID")
    void testDeleteDiarista() {
        // Given
        Diarista saved = repository.save(diaristaValida);

        // When
        repository.deleteById(saved.getId());

        // Then
        Optional<Diarista> found = repository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Deve validar taxa diária de diarista")
    void testValidateTaxaDiaria() {
        // Given
        Diarista diarista = TestDataBuilder.diaristaBuilder()
                .withTaxaDiaria(100.0)
                .buildEntity();

        // When
        Diarista saved = repository.save(diarista);

        // Then
        assertThat(saved.getTaxaDiaria())
                .isPositive()
                .isEqualTo(100.0);
    }

    @Test
    @DisplayName("Deve contar total de diaristas")
    void testCountDiaristas() {
        // Given
        repository.save(TestDataBuilder.diaristaBuilder().buildEntity());
        repository.save(TestDataBuilder.diaristaBuilder().buildEntity());

        // When
        long count = repository.count();

        // Then
        assertThat(count).isEqualTo(2L);
    }
}
