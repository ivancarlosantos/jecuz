package ao.tcc.projetofinal.jecuz.services;

import ao.tcc.projetofinal.jecuz.dto.ClienteDTO;
import ao.tcc.projetofinal.jecuz.entities.Cliente;
import ao.tcc.projetofinal.jecuz.exceptions.DataViolationException;
import ao.tcc.projetofinal.jecuz.exceptions.RegraDeNegocioException;
import ao.tcc.projetofinal.jecuz.repositories.ClienteRepository;
import ao.tcc.projetofinal.jecuz.utils.ValidationParameter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ModelMapper mapper;

    public ClienteDTO save(ClienteDTO dto) throws ParseException {

        if (findByCliente(dto) != null) {
            throw new DataViolationException("Cliente já Cadastrado");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date nascimento = sdf.parse(dto.getNascimento());
        Cliente cliente = Cliente
                .builder()
                .nome(dto.getNome())
                .nascimento(nascimento)
                .telefone(dto.getTelefone())
                .numeroBi(dto.getNumeroBi())
                .email(dto.getEmail())
                .build();

        Cliente clienteSaved = clienteRepository.save(cliente);

        return mapper.map(clienteSaved, ClienteDTO.class);
    }

    public List<ClienteDTO> listAll() {
        return clienteRepository
                .findAll(Sort.by("nome"))
                .stream()
                .map(cliente -> {
                    try {

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = sdf.parse(String.valueOf(cliente.getNascimento()));
                        cliente.setNascimento(date);
                    } catch (ParseException e) {
                        throw new RegraDeNegocioException(e.getMessage());
                    }

                    return mapper.map(cliente, ClienteDTO.class);
                }).toList();
    }

    public ClienteDTO findByID(String value) {
        Long id = ValidationParameter.validate(value);
        Cliente cliente = clienteRepository
                .findById(id)
                .stream()
                .map(c -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = null;
                    try {
                        date = sdf.parse(String.valueOf(c.getNascimento()));
                    } catch (ParseException e) {
                        throw new RegraDeNegocioException(e.getMessage());
                    }
                    c.setNascimento(date);
                    return c;
                }).findAny()
                  .orElseThrow(() -> new RegraDeNegocioException("Cliente não encontrado"));
        return mapper.map(cliente, ClienteDTO.class);
    }

    private ClienteDTO findByCliente(ClienteDTO dto) {
        Cliente cliente = clienteRepository.findByCliente(dto.getEmail());
        if (cliente != null) {
            return mapper.map(cliente, ClienteDTO.class);
        }
        return null;
    }
}
