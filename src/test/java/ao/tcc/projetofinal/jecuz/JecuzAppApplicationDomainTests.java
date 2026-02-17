package ao.tcc.projetofinal.jecuz;

import ao.tcc.projetofinal.jecuz.entities.Cliente;
import ao.tcc.projetofinal.jecuz.enums.ClienteStatus;
import ao.tcc.projetofinal.jecuz.repositories.ClienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class JecuzAppApplicationDomainTests {

    @Mock
    ClienteRepository repository;

    @Test
    void testDomain(){

        String nome = "Kaunda Daniel";
        LocalDate nascimento = LocalDate.of(1990,02,10);
        String telefone = "+244951753147";
        String numeroBi = "009547305KD104";
        String email = "kaunda.daniel@mail.com";
        LocalDateTime dataRegistro = LocalDateTime.now();
        ClienteStatus status = ClienteStatus.ATIVO;

        Cliente cliente = new Cliente(null, nome, nascimento, telefone, numeroBi, email, dataRegistro, status, null);

        repository.save(cliente);

        assertNotNull(cliente);
        assertEquals(nome, cliente.getNome());
        assertEquals(nascimento, cliente.getNascimento());
        assertEquals(telefone, cliente.getTelefone());
        assertEquals(numeroBi, cliente.getNumeroBi());
        assertEquals(email, cliente.getEmail());
        assertEquals(dataRegistro, cliente.getDataRegistro());
        assertEquals(status, cliente.getStatus());
    }

    @Test
    void testDomainListAll() {

        String nome = "Kaunda Daniel";
        LocalDate nascimento = LocalDate.of(1990,02,10);
        String telefone = "+244951753147";
        String numeroBi = "009547305KD104";
        String email = "kaunda.daniel@mail.com";
        LocalDateTime dataRegistro = LocalDateTime.now();
        ClienteStatus status = ClienteStatus.ATIVO;

        String nome2 = "Armando Kalei";
        LocalDate nascimento2 = LocalDate.of(1990,11,14);
        String telefone2 = "+244951753178";
        String numeroBi2 = "009547305AK106";
        String email2 = "armando.kalei@mail.com";
        LocalDateTime dataRegistro2 = LocalDateTime.now();
        ClienteStatus status2 = ClienteStatus.ATIVO;

        Cliente c1 = new Cliente(null, nome, nascimento, telefone, numeroBi, email, dataRegistro, status, null);
        Cliente c2 = new Cliente(null, nome2, nascimento2, telefone2, numeroBi2, email2, dataRegistro2, status2, null);

        List<Cliente> clients = Arrays.asList(c1, c2);

        List<Cliente> clientsAll = repository.saveAll(clients);

        List<Cliente> found = clientsAll.stream().toList();
        assertNotEquals(2, found.size());
    }
}
