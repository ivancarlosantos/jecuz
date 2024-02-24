package ao.tcc.projetofinal.jecuz.services;

import ao.tcc.projetofinal.jecuz.dto.DiaristaDTO;
import ao.tcc.projetofinal.jecuz.entities.Diarista;
import ao.tcc.projetofinal.jecuz.exceptions.DataViolationException;
import ao.tcc.projetofinal.jecuz.exceptions.RegraDeNegocioException;
import ao.tcc.projetofinal.jecuz.repositories.DiaristaRepository;
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
public class DiaristaService {

    private final DiaristaRepository diaristaRepository;
    private final ModelMapper mapper;

    public DiaristaDTO save(DiaristaDTO dto) throws ParseException {

        if (findDiarista(dto) != null || findNumeroBi(dto) != null) {
            throw new DataViolationException("Diarista já Cadastrada");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date nascimento = sdf.parse(dto.getNascimento());
        Diarista diarista = Diarista
                .builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .nascimento(nascimento)
                .telefone(dto.getTelefone())
                .numeroBi(dto.getNumeroBi())
                .email(dto.getEmail())
                .build();

        Diarista diaristaSaved = diaristaRepository.save(diarista);

        return mapper.map(diaristaSaved, DiaristaDTO.class);
    }

    public List<DiaristaDTO> listAll() {
        return diaristaRepository
                .findAll(Sort.by("nome"))
                .stream()
                .map(diarista -> {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = sdf.parse(String.valueOf(diarista.getNascimento()));
                        diarista.setNascimento(date);
                    } catch (ParseException e) {
                        throw new RegraDeNegocioException(e.getMessage());
                    }

                    return mapper.map(diarista, DiaristaDTO.class);
                }).toList();
    }

    public DiaristaDTO findByID(Long id) {
        Diarista diarista = diaristaRepository
                .findById(id)
                .stream()
                .map(d -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = null;
                    try {
                        date = sdf.parse(String.valueOf(d.getNascimento()));
                    } catch (ParseException e) {
                        throw new RegraDeNegocioException(e.getMessage());
                    }
                    d.setNascimento(date);
                    return d;
                }).findAny()
                .orElseThrow(() -> new RegraDeNegocioException("ID não encontrado"));
        return mapper.map(diarista, DiaristaDTO.class);
    }

    private DiaristaDTO findDiarista(DiaristaDTO dto) {
        Diarista diarista = diaristaRepository.findByDiarista(dto.getEmail());
        if (diarista != null) {
            return mapper.map(diarista, DiaristaDTO.class);
        }
        return null;
    }

    private DiaristaDTO findNumeroBi(DiaristaDTO dto) {
        Diarista diarista = diaristaRepository.findByNumeroBi(dto.getNumeroBi());
        if (diarista != null) {
            return mapper.map(diarista, DiaristaDTO.class);
        }
        return null;
    }
}
