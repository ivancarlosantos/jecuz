package ao.tcc.projetofinal.jecuz.services.cliente;

import ao.tcc.projetofinal.jecuz.dto.cliente.ClientePattern;
import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteRequest;
import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteResponse;
import ao.tcc.projetofinal.jecuz.entities.Cliente;
import ao.tcc.projetofinal.jecuz.enums.ClienteStatus;
import ao.tcc.projetofinal.jecuz.exceptions.RegraDeNegocioException;
import ao.tcc.projetofinal.jecuz.repositories.ClienteRepository;
import ao.tcc.projetofinal.jecuz.services.interfaces.IValidation;
import ao.tcc.projetofinal.jecuz.utils.PageableCommons;
import ao.tcc.projetofinal.jecuz.utils.ValidationParameter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ClienteService {

    private final List<IValidation> validations;
    private final ClienteRepository clienteRepository;
    private final ModelMapper mapper;

    public ClienteResponse save(ClienteRequest request) throws ParseException {

        validations.forEach(validation -> validation.execute(request));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate nascimento = LocalDate.parse(request.getNascimento(), formatter);

        Cliente cliente = Cliente.builder()
                                 .nome(request.getNome())
                                 .nascimento(nascimento)
                                 .telefone(request.getTelefone())
                                 .numeroBi(request.getNumeroBi())
                                 .email(request.getEmail())
                                 .dataRegistro(LocalDateTime.now())
                                 .status(ClienteStatus.ATIVO)
                                 .build();

        Cliente saved = clienteRepository.save(cliente);

        return mapper.map(saved, ClienteResponse.class);
    }

    public PageableCommons<List<ClienteResponse>> listAll(String search, Integer page, Integer size) {
        List<ClienteResponse> responses = clienteRepository.findAll()
                                                           .stream()
                                                           .sorted(Comparator.comparing(Cliente::getNome))
                                                           .filter((cli) -> cli.getStatus() == ClienteStatus.ATIVO)
                                                           .map((cli) -> mapper.map(cli, ClienteResponse.class))
                                                           .filter(response -> search == null || search.isEmpty() ||
                                                                                  response.getNome().toLowerCase().contains(search.toLowerCase()))
                                                           .toList();

        Pageable pageable = PageRequest.of(page, size);
        int start = Math.min((int) pageable.getOffset(), responses.size());
        int end   = Math.min((start + pageable.getPageSize()), responses.size());
        double totalContentSlice = ((double) responses.size() /size);
        double totalPages = page < totalContentSlice ? (totalContentSlice - page) : 0;

        List<ClienteResponse> pagedList = responses.subList(start, end);

        return new PageableCommons<>(pagedList, page, pagedList.size(), (int) Math.floor(totalPages), responses.size());
    }

    public ClienteResponse findByID(String value) {
        Long id         = ValidationParameter.validate(value);
        Cliente cliente = clienteRepository.findById(id)
                                           .orElseThrow(() -> new RegraDeNegocioException("Cliente não encontrado"));

        return mapper.map(cliente, ClienteResponse.class);
    }
}