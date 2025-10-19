package ao.tcc.projetofinal.jecuz.services.cliente;

import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteRequest;
import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteResponse;
import ao.tcc.projetofinal.jecuz.entities.Cliente;
import ao.tcc.projetofinal.jecuz.exceptions.DataViolationException;
import ao.tcc.projetofinal.jecuz.exceptions.RegraDeNegocioException;
import ao.tcc.projetofinal.jecuz.repositories.ClienteRepository;
import ao.tcc.projetofinal.jecuz.services.istrategy.INewClienteValidation;
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

    private final List<INewClienteValidation> newClienteValidations;
    private final ClienteRepository clienteRepository;
    private final ModelMapper mapper;

    public ClienteResponse save(ClienteRequest request) throws ParseException {

        newClienteValidations.forEach(validation -> validation.execute(request));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date nascimento = sdf.parse(request.getNascimento());
        Cliente cliente = Cliente.builder()
                                 .nome(request.getNome())
                                 .nascimento(nascimento.toString())
                                 .telefone(request.getTelefone())
                                 .numeroBi(request.getNumeroBi())
                                 .email(request.getEmail())
                                 .build();

        Cliente saved = clienteRepository.save(cliente);

        return mapper.map(saved, ClienteResponse.class);
    }

    public List<ClienteResponse> listAll() {
        return clienteRepository.findAll(Sort.by("nome"))
                                .stream()
                                .map(cliente -> {
                                    try {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date date = sdf.parse(String.valueOf(cliente.getNascimento()));
                                        cliente.setNascimento(date.toString());
                                    } catch (ParseException e) {
                                        throw new RegraDeNegocioException(e.getMessage());
                                    }
                                    return mapper.map(cliente, ClienteResponse.class);
                                }).toList();
    }

    public ClienteResponse findByID(String value) {
        Long id = ValidationParameter.validate(value);
        Cliente cliente = clienteRepository.findById(id)
                                           .stream()
                                           .map(c -> {
                                               SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                               Date date;
                                               try {
                                                   date = sdf.parse(String.valueOf(c.getNascimento()));
                                               } catch (ParseException e) {
                                                   throw new RegraDeNegocioException(e.getMessage());
                                               }
                                               c.setNascimento(date.toString());
                                               return c;
                                           }).findAny()
                                             .orElseThrow(() -> new RegraDeNegocioException("Cliente não encontrado"));

        return mapper.map(cliente, ClienteResponse.class);
    }
}