package ao.tcc.projetofinal.jecuz.services.ordens;

import ao.tcc.projetofinal.jecuz.dto.servicos.OrdemServicoRequest;
import ao.tcc.projetofinal.jecuz.dto.servicos.OrdemServicoResponse;
import ao.tcc.projetofinal.jecuz.entities.Cliente;
import ao.tcc.projetofinal.jecuz.entities.Diarista;
import ao.tcc.projetofinal.jecuz.entities.OrdensDeServico;
import ao.tcc.projetofinal.jecuz.enums.TipoLimpeza;
import ao.tcc.projetofinal.jecuz.exceptions.RegraDeNegocioException;
import ao.tcc.projetofinal.jecuz.repositories.ClienteRepository;
import ao.tcc.projetofinal.jecuz.repositories.DiaristaRepository;
import ao.tcc.projetofinal.jecuz.repositories.OrdensDeServicoRepository;
import ao.tcc.projetofinal.jecuz.utils.GerarNumeroOS;
import ao.tcc.projetofinal.jecuz.utils.PageableCommons;
import ao.tcc.projetofinal.jecuz.utils.ValidationParameter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public PageableCommons<List<OrdemServicoResponse>> listOS(String search, Integer page, Integer size){

        List<OrdemServicoResponse> responses = ordensDeServicoRepository.findAll()
                                                                        .stream()
                                                                        .map(os -> mapper.map(os, OrdemServicoResponse.class))
                                                                        .filter(response -> search == null || search.isEmpty() ||
                                                                                                    response.getDiarista().getNome().contains(search.toLowerCase()))
                                                                        .toList();

        Pageable pageable = PageRequest.of(page, size);
        int start = Math.min((int) pageable.getOffset(), responses.size());
        int end   = Math.min((start + pageable.getPageSize()), responses.size());
        double totalContentSlice = ((double) responses.size() /size);
        double totalPages = page < totalContentSlice ? (totalContentSlice - page) : 0;

        List<OrdemServicoResponse> pagedList = responses.subList(start, end);

        return new PageableCommons<>(pagedList, page, pagedList.size(), (int) Math.floor(totalPages), responses.size());
    }

    public OrdensDeServico findOS(String value){
        Long id = ValidationParameter.validate(value);
        return ordensDeServicoRepository.findById(id)
                                        .orElseThrow(() -> new RegraDeNegocioException("O.S não encontrado"));
    }

    public OrdensDeServico gerarOrdem(String idCliente, String idDiarista, OrdemServicoRequest request, TipoLimpeza tipoLimpeza) throws ParseException {
        Long indexCliente  = ValidationParameter.validate(idCliente);
        Long indexDiarista = ValidationParameter.validate(idDiarista);

        Cliente  cliente   = clienteRepository.findById(indexCliente).orElseThrow(() -> new RegraDeNegocioException("ID Cliente não encontrado"));
        Diarista diarista  = diaristaRepository.findById(indexDiarista).orElseThrow(() -> new RegraDeNegocioException("ID Diarista não encontrado"));

        List<Diarista> diaristas = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date solicitacao = sdf.parse(request.getDataSolicitacao());
        Date execucao    = sdf.parse(request.getDataExecucao());

        OrdensDeServico servico = OrdensDeServico.builder()
                                                 .nomeCliente(cliente.getNome())
                                                 .numOrdemServico(GerarNumeroOS.gerar())
                                                 .dataSolicitacao(solicitacao.toString())
                                                 .tipoLimpeza(tipoLimpeza)
                                                 .dataExecucao(execucao.toString())
                                                 .valorTotal(request.getValorTotal())
                                                 .build();
        diaristas.add(diarista);
        cliente.setDiaristas(diaristas);
        diarista.setCliente(cliente);
        servico.setDiarista(diarista);

        clienteRepository.save(cliente);
        diaristaRepository.save(diarista);
        ordensDeServicoRepository.save(servico);

        return servico;
    }
}
