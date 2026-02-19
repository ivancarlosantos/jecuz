package ao.tcc.projetofinal.jecuz.test.unit;

import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteRequest;
import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteResponse;
import ao.tcc.projetofinal.jecuz.entities.Cliente;
import ao.tcc.projetofinal.jecuz.enums.ClienteStatus;
import ao.tcc.projetofinal.jecuz.exceptions.RegraDeNegocioException;
import ao.tcc.projetofinal.jecuz.repositories.ClienteRepository;
import ao.tcc.projetofinal.jecuz.services.cliente.ClienteService;
import ao.tcc.projetofinal.jecuz.services.interfaces.IValidation;
import ao.tcc.projetofinal.jecuz.utils.PageableCommons;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class

ClienteServiceUnitTest {

    @Mock
    ClienteRepository clienteRepository;

    @Mock
    ModelMapper mapper;

    // empty validations list for unit tests
    List<IValidation> validations;

    ClienteService clienteService;

    @BeforeEach
    void setup() {
        validations = List.of();
        clienteService = new ClienteService(validations, clienteRepository, mapper);
    }

    @Test
    void save_shouldPersistAndReturnResponse() throws Exception {
        ClienteRequest request = new ClienteRequest("Nome Teste", "10/02/1990", "+244900000000", "001234", "teste@email.com");

        Cliente saved = Cliente.builder()
                .id(1L)
                .nome(request.getNome())
                .nascimento(LocalDate.of(1990, 2, 10))
                .telefone(request.getTelefone())
                .numeroBi(request.getNumeroBi())
                .email(request.getEmail())
                .dataRegistro(LocalDateTime.now())
                .status(ClienteStatus.ATIVO)
                .build();

        ClienteResponse expectedResponse = ClienteResponse.builder()
                .id(saved.getId())
                .nome(saved.getNome())
                .nascimento("10/02/1990")
                .telefone(saved.getTelefone())
                .numeroBi(saved.getNumeroBi())
                .email(saved.getEmail())
                .build();

        when(clienteRepository.save(any(Cliente.class))).thenReturn(saved);
        when(mapper.map(saved, ClienteResponse.class)).thenReturn(expectedResponse);

        ClienteResponse response = clienteService.save(request);

        assertNotNull(response);
        assertEquals(expectedResponse.getNome(), response.getNome());
        assertEquals(expectedResponse.getEmail(), response.getEmail());
    }

    @Test
    void save_withInvalidDate_shouldThrowDateTimeParseException() {
        ClienteRequest request = new ClienteRequest("Nome Teste", "31-02-1990", "+244900000000", "001234", "teste@email.com");

        assertThrows(RuntimeException.class, () -> {
            clienteService.save(request);
        });
    }

    @Test
    void listAll_shouldReturnPagedAndFilteredResults() {
        Cliente c1 = Cliente.builder().id(1L).nome("Alice").nascimento(LocalDate.of(1990,1,1)).telefone("t1").numeroBi("b1").email("a@a.com").status(ClienteStatus.ATIVO).build();
        Cliente c2 = Cliente.builder().id(2L).nome("Bob").nascimento(LocalDate.of(1992,2,2)).telefone("t2").numeroBi("b2").email("b@b.com").status(ClienteStatus.ATIVO).build();
        Cliente inactive = Cliente.builder().id(3L).nome("Zzzz").nascimento(LocalDate.of(2000,1,1)).telefone("t3").numeroBi("b3").email("c@c.com").status(ClienteStatus.INATIVO).build();

        when(clienteRepository.findAll()).thenReturn(List.of(c1, c2, inactive));

        ClienteResponse r1 = ClienteResponse.builder()
                .id(c1.getId())
                .nome(c1.getNome())
                .nascimento("01/01/1990")
                .telefone(c1.getTelefone())
                .numeroBi(c1.getNumeroBi())
                .email(c1.getEmail())
                .build();
        ClienteResponse r2 = ClienteResponse.builder()
                .id(c2.getId())
                .nome(c2.getNome())
                .nascimento("02/02/1992")
                .telefone(c2.getTelefone())
                .numeroBi(c2.getNumeroBi())
                .email(c2.getEmail())
                .build();

        when(mapper.map(c1, ClienteResponse.class)).thenReturn(r1);
        when(mapper.map(c2, ClienteResponse.class)).thenReturn(r2);

        PageableCommons<List<ClienteResponse>> page = clienteService.listAll(null, 0, 10);

        assertNotNull(page);
        assertEquals(2, page.getNumberContent());
        assertEquals(2, ((List<?>) page.getContent()).size());

        // test search filter
        PageableCommons<List<ClienteResponse>> filtered = clienteService.listAll("ali", 0, 10);
        assertEquals(1, filtered.getNumberContent());
    }

    @Test
    void findByID_shouldReturnWhenExists() {
        Cliente c = Cliente.builder().id(5L).nome("Carlos").nascimento(LocalDate.of(1985,5,5)).telefone("t").numeroBi("b").email("e@e.com").status(ClienteStatus.ATIVO).build();
        when(clienteRepository.findById(5L)).thenReturn(Optional.of(c));
        ClienteResponse resp = ClienteResponse.builder()
                .id(c.getId())
                .nome(c.getNome())
                .nascimento("05/05/1985")
                .telefone(c.getTelefone())
                .numeroBi(c.getNumeroBi())
                .email(c.getEmail())
                .build();
        when(mapper.map(c, ClienteResponse.class)).thenReturn(resp);

        ClienteResponse result = clienteService.findByID("5");

        assertNotNull(result);
        assertEquals("Carlos", result.getNome());
    }

    @Test
    void findByID_whenNotFound_shouldThrow() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RegraDeNegocioException.class, () -> clienteService.findByID("99"));
    }
}

