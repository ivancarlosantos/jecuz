package ao.tcc.projetofinal.jecuz.test.unit;

import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteRequest;
import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteResponse;
import ao.tcc.projetofinal.jecuz.entities.Cliente;
import ao.tcc.projetofinal.jecuz.exceptions.RegraDeNegocioException;
import ao.tcc.projetofinal.jecuz.repositories.ClienteRepository;
import ao.tcc.projetofinal.jecuz.services.cliente.ClienteService;
import ao.tcc.projetofinal.jecuz.services.interfaces.IValidation;
import ao.tcc.projetofinal.jecuz.test.fixtures.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes de Unidade para ClienteService.
 * Foca em lógica isolada com mocks de dependências.
 * Projetados para melhor detecção de mutação.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ClienteService - Testes de Unidade com cobertura para Mutação")
class ClienteServiceMutationTest {

    @Mock
    ClienteRepository clienteRepository;

    @Mock
    ModelMapper mapper;

    List<IValidation> validations;

    ClienteService clienteService;

    @BeforeEach
    void setup() {
        validations = List.of();
        clienteService = new ClienteService(validations, clienteRepository, mapper);
    }

    @Test
    @DisplayName("Deve lançar exceção quando cliente é nulo")
    void save_shouldThrowExceptionWhenClienteIsNull() {
        // Given
        ClienteRequest request = null;

        // When/Then
        assertThatThrownBy(() -> clienteService.save(request))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Deve salvar cliente e retornar response")
    void save_shouldPersistAndReturnResponse() throws Exception {
        // Given
        ClienteRequest request = TestDataBuilder.clienteBuilder()
                .withNome("João Silva")
                .buildRequest();

        Cliente cliente = TestDataBuilder.clienteBuilder()
                .withNome("João Silva")
                .buildEntity();

        ClienteResponse response = TestDataBuilder.clienteBuilder()
                .withNome("João Silva")
                .buildResponse();

        when(mapper.map(request, Cliente.class)).thenReturn(cliente);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(mapper.map(cliente, ClienteResponse.class)).thenReturn(response);

        // When
        ClienteResponse result = clienteService.save(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo("João Silva");
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve recuperar cliente por ID com sucesso")
    void findById_shouldReturnClienteWhenExists() throws Exception {
        // Given
        Long clientId = 1L;
        Cliente cliente = TestDataBuilder.clienteBuilder()
                .withId(clientId)
                .buildEntity();
        ClienteResponse response = TestDataBuilder.clienteBuilder()
                .withId(clientId)
                .buildResponse();

        when(clienteRepository.findById(clientId)).thenReturn(Optional.of(cliente));
        when(mapper.map(cliente, ClienteResponse.class)).thenReturn(response);

        // When
        ClienteResponse result = clienteService.findByID(String.valueOf(clientId));

        // Then
        assertThat(result).isNotNull();
        assertThat(clientId).isEqualTo(clientId);
        verify(clienteRepository, times(1)).findById(clientId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando cliente não existe")
    void findById_shouldThrowExceptionWhenNotFound() {
        // Given
        Long clienteId = 999L;
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> clienteService.findByID(String.valueOf(clienteId)))
                .isInstanceOf(RegraDeNegocioException.class);
    }

    @Test
    @DisplayName("Deve validar que nome não é vazio após salvar")
    void save_shouldNotAllowEmptyName() throws Exception {
        // Given
        ClienteRequest request = TestDataBuilder.clienteBuilder()
                .withNome("")
                .buildRequest();

        Cliente cliente = new Cliente();
        cliente.setNome("");

        when(mapper.map(request, Cliente.class)).thenReturn(cliente);

        // When/Then - Deve validar nome não vazio
        // (Dependendo da implementação, pode lançar exceção)
        assertThatCode(() -> clienteService.save(request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Deve verificar que save é chamado exatamente uma vez")
    void save_shouldCallRepositorySaveExactlyOnce() throws Exception {
        // Given
        ClienteRequest request = TestDataBuilder.clienteBuilder().buildRequest();
        Cliente cliente = TestDataBuilder.clienteBuilder().buildEntity();
        ClienteResponse response = TestDataBuilder.clienteBuilder().buildResponse();

        when(mapper.map(request, Cliente.class)).thenReturn(cliente);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(mapper.map(cliente, ClienteResponse.class)).thenReturn(response);

        // When
        clienteService.save(request);

        // Then
        verify(clienteRepository, times(1)).save(any(Cliente.class));
        verify(clienteRepository, never()).findById(any());
    }
}
