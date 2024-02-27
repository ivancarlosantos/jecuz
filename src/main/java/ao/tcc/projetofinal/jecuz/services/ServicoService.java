package ao.tcc.projetofinal.jecuz.services;

import ao.tcc.projetofinal.jecuz.dto.ServicoDTO;
import ao.tcc.projetofinal.jecuz.entities.Servico;
import ao.tcc.projetofinal.jecuz.exceptions.RegraDeNegocioException;
import ao.tcc.projetofinal.jecuz.repositories.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ServicoService {
    private final ServicoRepository servicoRepository;
    private final ModelMapper mapper;

    public ServicoDTO save(ServicoDTO dto) {
        Servico servico = Servico.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .valorMinimo(dto.getValorMinimo())
                .qtdHoras(dto.getQtdHoras())
                .percentagemComissao(dto.getPercentagemComissao())
                .horasQuarto(dto.getHorasQuarto())
                .valorQuarto(dto.getValorQuarto())
                .horasSala(dto.getHorasSala())
                .valorSala(dto.getValorSala())
                .horasBanheiro(dto.getHorasBanheiro())
                .valorBanheiro(dto.getValorBanheiro())
                .horasCozinha(dto.getHorasCozinha())
                .valorCozinha(dto.getValorCozinha())
                .horasQuintal(dto.getHorasQuintal())
                .valorQuintal(dto.getValorQuintal())
                .horasOutros(dto.getHorasOutros())
                .valorOutros(dto.getValorOutros())
                .icone(dto.getIcone()).build();
        servico = servicoRepository.save(servico);
        return mapper.map(servico, ServicoDTO.class);
    }

    public Set<ServicoDTO> listAll() {
        return servicoRepository.findAll().stream().map(servico -> mapper.map(servico, ServicoDTO.class)).collect(Collectors.toSet());
    }

    public ServicoDTO findById(Long id) {
        var servicoEncontrado = servicoRepository.findById(id);
        if (servicoEncontrado.isPresent()) {
            return mapper.map(servicoEncontrado.get(), ServicoDTO.class);
        }
        var mensagem = String.format("Serviço com o ID %d não encontrado" + id);
        throw new RegraDeNegocioException(mensagem);

    }


}
