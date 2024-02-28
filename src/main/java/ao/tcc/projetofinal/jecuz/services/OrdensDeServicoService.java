package ao.tcc.projetofinal.jecuz.services;

import ao.tcc.projetofinal.jecuz.dto.OrdensDeServicoDTO;
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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrdensDeServicoService {

    private final OrdensDeServicoRepository ordensDeServicoRepository;
    private final ClienteRepository clienteRepository;
    private final DiaristaRepository diaristaRepository;
    private final ModelMapper mapper;

    public OrdensDeServicoDTO gerarOrdem(Long idCliente, Long idDiarista, OrdensDeServicoDTO dto) throws ParseException {

        Cliente c = clienteRepository.findById(idCliente).orElseThrow(() -> new RegraDeNegocioException("ID Cliente não encontrado"));
        Diarista d = diaristaRepository.findById(idDiarista).orElseThrow(() -> new RegraDeNegocioException("ID Diarista não encontrado"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date data = sdf.parse(dto.getDataSolicitacao());

        OrdensDeServico os = OrdensDeServico.builder()
                .numOrdemServico(GerarNumeroOS.gerar())
                .nomeCliente(c.getNome().toUpperCase())
                .diarista(d)
                .dataSolicitacao(data)
                .descricaoTarefa(dto.getDescricaoTarefa())
                .valorTotal(dto.getValorTotal())
                .dataExecucao(LocalDateTime.now())
                .build();

        List<OrdensDeServico> ordensDeServicos = new LinkedList<>();
        ordensDeServicos.add(os);

        d.setOrdensDeServicos(ordensDeServicos);

        clienteRepository.save(c);
        diaristaRepository.save(d);
        ordensDeServicoRepository.save(os);

        return mapper.map(os, OrdensDeServicoDTO.class);
    }

    public List<OrdensDeServicoDTO> findOSAll(){
        return ordensDeServicoRepository
                .findAll()
                .stream()
                .map(os -> mapper.map(os, OrdensDeServicoDTO.class))
                .toList();
    }

    public OrdensDeServico findOS(String value){
        Long id = ValidationParameter.validate(value);
        return ordensDeServicoRepository.findById(id).orElseThrow(() -> new RegraDeNegocioException("ID O.S não encontrado"));
    }
}
