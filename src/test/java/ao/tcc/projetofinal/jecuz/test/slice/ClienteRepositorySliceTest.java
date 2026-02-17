package ao.tcc.projetofinal.jecuz.test.slice;

import ao.tcc.projetofinal.jecuz.entities.Cliente;
import ao.tcc.projetofinal.jecuz.enums.ClienteStatus;
import ao.tcc.projetofinal.jecuz.repositories.ClienteRepository;
import ao.tcc.projetofinal.jecuz.test.fixtures.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
 * Testes de Slice para ClienteRepository.
 * Testa apenas a camada de persistência com banco real (PostgreSQL).
 * Escopo: @DataJpaTest
 */
@Testcontainers
@DataJpaTest
@DisplayName("ClienteRepository - Testes de Slice")
class ClienteRepositorySliceTest {

    @Container
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:15")
    ).withDatabaseName("test_cliente");

    @Autowired
    ClienteRepository repository;

    private Cliente clienteValido;

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
        clienteValido = TestDataBuilder.clienteBuilder()
                .withNome("João Silva")
                .buildEntity();
    }

    @Test
    @DisplayName("Deve salvar cliente com sucesso")
    void testSaveCliente() {
        // Given
        Cliente cliente = TestDataBuilder.clienteBuilder()
                .withNome("Maria Santos")
                .withStatus(ClienteStatus.ATIVO)
                .buildEntity();

        // When
        Cliente saved = repository.save(cliente);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getNome()).isEqualTo("Maria Santos");
        assertThat(saved.getStatus()).isEqualTo(ClienteStatus.ATIVO);
    }

    @Test
    @DisplayName("Deve recuperar cliente pelo ID")
    void testFindClienteById() {
        // Given
        Cliente saved = repository.save(clienteValido);

        // When
        Optional<Cliente> found = repository.findById(saved.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getNome()).isEqualTo(clienteValido.getNome());
    }

    @Test
    @DisplayName("Deve retornar empty quando cliente não existe")
    void testFindClienteByIdNotFound() {
        // When
        Optional<Cliente> found = repository.findById(999L);

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Deve listar todos os clientes")
    void testFindAllClientes() {
        // Given
        Cliente cliente1 = TestDataBuilder.clienteBuilder().withNome("Cliente 1").buildEntity();
        Cliente cliente2 = TestDataBuilder.clienteBuilder().withNome("Cliente 2").buildEntity();
        repository.save(cliente1);
        repository.save(cliente2);

        // When
        List<Cliente> clientes = repository.findAll();

        // Then
        assertThat(clientes).hasSize(2);
        assertThat(clientes).extracting(Cliente::getNome)
                .containsExactlyInAnyOrder("Cliente 1", "Cliente 2");
    }

    @Test
    @DisplayName("Deve deletar cliente por ID")
    void testDeleteCliente() {
        // Given
        Cliente saved = repository.save(clienteValido);

        // When
        repository.deleteById(saved.getId());

        // Then
        Optional<Cliente> found = repository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Deve atualizar cliente existente")
    void testUpdateCliente() {
        // Given
        Cliente saved = repository.save(clienteValido);
        String novoNome = "João Silva Atualizado";

        // When
        saved.setNome(novoNome);
        Cliente updated = repository.save(saved);

        // Then
        assertThat(updated.getNome()).isEqualTo(novoNome);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ATIVO", "INATIVO", "SUSPENSO"})
    @DisplayName("Deve salvar cliente com diferentes status")
    void testSaveClienteComDiferentesStatus(String statusStr) {
        // Given
        ClienteStatus status = ClienteStatus.valueOf(statusStr);
        Cliente cliente = TestDataBuilder.clienteBuilder()
                .withStatus(status)
                .buildEntity();

        // When
        Cliente saved = repository.save(cliente);

        // Then
        assertThat(saved.getStatus()).isEqualTo(status);
    }

    @Test
    @DisplayName("Deve contar total de clientes")
    void testCountClientes() {
        // Given
        repository.save(TestDataBuilder.clienteBuilder().buildEntity());
        repository.save(TestDataBuilder.clienteBuilder().buildEntity());

        // When
        long count = repository.count();

        // Then
        assertThat(count).isEqualTo(2L);
    }

    @Test
    @DisplayName("Deve verificar existência de cliente por ID")
    void testExistsById() {
        // Given
        Cliente saved = repository.save(clienteValido);

        // When
        boolean exists = repository.existsById(saved.getId());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false para cliente inexistente")
    void testExistsByIdNotFound() {
        // When
        boolean exists = repository.existsById(999L);

        // Then
        assertThat(exists).isFalse();
    }
}
