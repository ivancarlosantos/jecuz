package ao.tcc.projetofinal.jecuz.services.ordens;

import ao.tcc.projetofinal.jecuz.dto.ordens.OrdensDeServicoDTO;
import ao.tcc.projetofinal.jecuz.entities.Cliente;
import ao.tcc.projetofinal.jecuz.entities.Diarista;
import ao.tcc.projetofinal.jecuz.entities.OrdensDeServico;
import ao.tcc.projetofinal.jecuz.exceptions.RegraDeNegocioException;
import ao.tcc.projetofinal.jecuz.repositories.ClienteRepository;
import ao.tcc.projetofinal.jecuz.repositories.DiaristaRepository;
import ao.tcc.projetofinal.jecuz.repositories.OrdensDeServicoRepository;
import ao.tcc.projetofinal.jecuz.utils.GerarNumeroOS;
import ao.tcc.projetofinal.jecuz.utils.ValidationParameter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrdensDeServicoService {

    private final OrdensDeServicoRepository ordensDeServicoRepository;
    private final ClienteRepository clienteRepository;
    private final DiaristaRepository diaristaRepository;
    private final ModelMapper mapper;

    public OrdensDeServicoDTO save(OrdensDeServicoDTO dto) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date data = sdf.parse(dto.getDataSolicitacao());

        OrdensDeServico os = OrdensDeServico.builder()
                .numOrdemServico(GerarNumeroOS.gerar())
                .nomeCliente(dto.getCliente())
                .dataSolicitacao(data)
                .descricaoTarefa(dto.getDescricaoTarefa())
                .valorTotal(dto.getValorTotal())
                .dataExecucao(LocalDateTime.now())
                .build();

        ordensDeServicoRepository.save(os);

        return mapper.map(os, OrdensDeServicoDTO.class);
    }

    public List<OrdensDeServico> findOSAll(){
        return ordensDeServicoRepository
                .findAll()
                .stream()
                .toList();
    }

    public OrdensDeServico findOS(String value){
        Long id = ValidationParameter.validate(value);
        return ordensDeServicoRepository.findById(id).orElseThrow(() -> new RegraDeNegocioException("ID O.S não encontrado"));
    }

    public OrdensDeServico gerarOrdem(String idCliente, String idDiarista, String idOS){
        Long indexCliente = ValidationParameter.validate(idCliente);
        Long indexDiarista = ValidationParameter.validate(idDiarista);
        Long indexOS = ValidationParameter.validate(idOS);

        Cliente cliente = clienteRepository.findById(indexCliente).orElseThrow(() -> new RegraDeNegocioException("ID Cliente não encontrado"));
        Diarista diarista = diaristaRepository.findById(indexDiarista).orElseThrow(() -> new RegraDeNegocioException("ID Diarista não encontrado"));
        OrdensDeServico os = ordensDeServicoRepository.findById(indexOS).orElseThrow(() -> new RegraDeNegocioException("ID O.S não encontrado"));

        List<Diarista> diaristas = new ArrayList<>();

        diaristas.add(diarista);
        cliente.setDiaristas(diaristas);
        diarista.setCliente(cliente);
        os.setDiarista(diarista);
        os.setNomeCliente(cliente.getNome());

        clienteRepository.save(cliente);
        diaristaRepository.save(diarista);
        ordensDeServicoRepository.save(os);

        return os;
    }
}
