package ao.tcc.projetofinal.jecuz.test.slice;

import ao.tcc.projetofinal.jecuz.entities.OrdensDeServico;
import ao.tcc.projetofinal.jecuz.repositories.OrdensDeServicoRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes de Slice para OrdensDeServicoRepository.
 * Testa a camada de persistência com banco real.
 */
@Testcontainers
@DataJpaTest
@DisplayName("OrdensDeServicoRepository - Testes de Slice")
class OrdensDeServicoRepositorySliceTest {

    @Container
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:15")
    ).withDatabaseName("test_ordens");

    @Autowired
    OrdensDeServicoRepository repository;

    private OrdensDeServico ordemValida;

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
    }

    @Test
    @DisplayName("Deve salvar ordem de serviço com sucesso")
    void testSaveOrdem() {
        // Given
        OrdensDeServico ordem = new OrdensDeServico();
        ordem.setDataExecucao(LocalDate.now().toString());
        ordem.setValorTotal(150.0);

        // When
        OrdensDeServico saved = repository.save(ordem);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getValorTotal()).isEqualTo(150.0);
    }

    @Test
    @DisplayName("Deve recuperar ordem por ID")
    void testFindOrdemById() {
        // Given
        OrdensDeServico ordem = new OrdensDeServico();
        ordem.setDataSolicitacao(LocalDate.now().toString());
        ordem.setValorTotal(100.0);
        OrdensDeServico saved = repository.save(ordem);

        // When
        Optional<OrdensDeServico> found = repository.findById(saved.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getValorTotal()).isEqualTo(100.0);
    }

    @Test
    @DisplayName("Deve listar todas as ordens")
    void testFindAllOrdens() {
        // Given
        OrdensDeServico ordem1 = new OrdensDeServico();
        ordem1.setDataSolicitacao(LocalDate.now().toString());
        ordem1.setValorTotal(100.0);

        OrdensDeServico ordem2 = new OrdensDeServico();
        ordem2.setDataSolicitacao(LocalDate.now().toString());
        ordem2.setValorTotal(200.0);

        repository.save(ordem1);
        repository.save(ordem2);

        // When
        List<OrdensDeServico> ordens = repository.findAll();

        // Then
        assertThat(ordens).hasSize(2);
        assertThat(ordens).extracting(OrdensDeServico::getValorTotal)
                .containsExactlyInAnyOrder(100.0, 200.0);
    }

    @Test
    @DisplayName("Deve deletar ordem de serviço")
    void testDeleteOrdem() {
        // Given
        OrdensDeServico ordem = new OrdensDeServico();
        ordem.setDataSolicitacao(LocalDate.now().toString());
        ordem.setValorTotal(150.0);
        OrdensDeServico saved = repository.save(ordem);

        // When
        repository.deleteById(saved.getId());

        // Then
        Optional<OrdensDeServico> found = repository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Deve atualizar valor da ordem")
    void testUpdateValor() {
        // Given
        OrdensDeServico ordem = new OrdensDeServico();
        ordem.setDataExecucao(LocalDate.now().toString());
        ordem.setValorTotal(100.0);
        OrdensDeServico saved = repository.save(ordem);

        // When
        saved.setValorTotal(200.0);
        OrdensDeServico updated = repository.save(saved);

        // Then
        assertThat(updated.getValorTotal()).isEqualTo(200.0);
    }

    @Test
    @DisplayName("Deve contar total de ordens")
    void testCountOrdens() {
        // Given
        for (int i = 0; i < 3; i++) {
            OrdensDeServico ordem = new OrdensDeServico();
            ordem.setDataExecucao(LocalDate.now().toString());
            ordem.setValorTotal(100.0 * (i + 1));
            repository.save(ordem);
        }

        // When
        long count = repository.count();

        // Then
        assertThat(count).isEqualTo(3L);
    }

    @Test
    @DisplayName("Deve validar ordem com valor zero")
    void testOrderWithZeroValue() {
        // Given
        OrdensDeServico ordem = new OrdensDeServico();
        ordem.setDataExecucao(LocalDate.now().toString());
        ordem.setValorTotal(0.0);

        // When
        OrdensDeServico saved = repository.save(ordem);

        // Then
        assertThat(saved.getValorTotal()).isZero();
    }

    @Test
    @DisplayName("Deve manter ordem com data futura")
    void testOrderWithFutureDate() {
        // Given
        OrdensDeServico ordem = new OrdensDeServico();
        LocalDate futuraData = LocalDate.now().plusDays(5);
        ordem.setDataExecucao(futuraData.toString());
        ordem.setValorTotal(150.0);

        // When
        OrdensDeServico saved = repository.save(ordem);

        // Then
        assertThat(saved.getDataExecucao()).isEqualTo(futuraData.toString());
    }
}
