package ao.tcc.projetofinal.jecuz.services.diarista;

import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaRequest;
import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaResponse;
import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaSOResponse;
import ao.tcc.projetofinal.jecuz.entities.Diarista;
import ao.tcc.projetofinal.jecuz.exceptions.RegraDeNegocioException;
import ao.tcc.projetofinal.jecuz.repositories.DiaristaRepository;
import ao.tcc.projetofinal.jecuz.services.cliente.ClienteService;
import ao.tcc.projetofinal.jecuz.services.interfaces.IValidation;
import ao.tcc.projetofinal.jecuz.utils.ValidationParameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DiaristaService {

    private final List<IValidation> validations;
    private final DiaristaRepository diaristaRepository;
    private final ModelMapper mapper;

    public DiaristaResponse save(DiaristaRequest request) throws ParseException {

        DiaristaService.log.debug("IN-DiaristaService-save()");

        validations.forEach(validation -> validation.execute(request));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date nascimento = sdf.parse(request.getNascimento());

        Diarista diarista = Diarista.builder()
                                    .nome(request.getNome())
                                    .nascimento(nascimento.toString())
                                    .telefone(request.getTelefone())
                                    .numeroBi(request.getNumeroBi())
                                    .email(request.getEmail())
                                    .enabled(false)
                                    .build();

        Diarista saved = diaristaRepository.save(diarista);

        DiaristaService.log.debug("OUT-DiaristaService-save()");

        return mapper.map(saved, DiaristaResponse.class);
    }

    public DiaristaSOResponse findByID(String value) {
        DiaristaService.log.debug("IN-DiaristaService-findByID()");

        Long id = ValidationParameter.validate(value);

        DiaristaService.log.debug("OUT-DiaristaService-findByID()");

        return diaristaRepository.findById(id)
                                 .map(diarista -> mapper.map(diarista, DiaristaSOResponse.class))
                                 .orElseThrow(() -> new RegraDeNegocioException("Diarista não encontrada"));
    }
}
