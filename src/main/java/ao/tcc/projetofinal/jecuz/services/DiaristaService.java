package ao.tcc.projetofinal.jecuz.services;

import ao.tcc.projetofinal.jecuz.dto.DiaristaDTO;
import ao.tcc.projetofinal.jecuz.entities.Cliente;
import ao.tcc.projetofinal.jecuz.entities.Diarista;
import ao.tcc.projetofinal.jecuz.exceptions.DataViolationException;
import ao.tcc.projetofinal.jecuz.exceptions.RegraDeNegocioException;
import ao.tcc.projetofinal.jecuz.repositories.ClienteRepository;
import ao.tcc.projetofinal.jecuz.repositories.DiaristaRepository;
import ao.tcc.projetofinal.jecuz.utils.GenerateCode;
import ao.tcc.projetofinal.jecuz.utils.ValidationParameter;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DiaristaService {

    private final ClienteRepository clienteRepository;
    private final DiaristaRepository diaristaRepository;
    private final MailService mailService;
    private final ModelMapper mapper;

    public DiaristaDTO save(DiaristaDTO dto) throws ParseException, MessagingException, UnsupportedEncodingException {

        if (findDiarista(dto) != null || findNumeroBi(dto) != null) {
            throw new DataViolationException("Diarista já Cadastrada");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date nascimento = sdf.parse(dto.getNascimento());
        String code = GenerateCode.generateCode(64);

        Diarista diarista = Diarista
                .builder()
                .nome(dto.getNome())
                .nascimento(nascimento)
                .telefone(dto.getTelefone())
                .numeroBi(dto.getNumeroBi())
                .email(dto.getEmail())
                .verificationCode(code)
                .enabled(false)
                .build();

        Diarista diaristaSaved = diaristaRepository.save(diarista);

        mailService.sendVerificationEmail(diaristaSaved);

        return mapper.map(diaristaSaved, DiaristaDTO.class);
    }

    public List<Diarista> listAll() {
        return diaristaRepository
                .findAll(Sort.by("nome"))
                .stream()
                .filter(Diarista::isEnabled)
                .map(diarista -> {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = sdf.parse(String.valueOf(diarista.getNascimento()));
                        diarista.setNascimento(date);
                    } catch (ParseException e) {
                        throw new RegraDeNegocioException(e.getMessage());
                    }
                    return diarista;
                }).toList();
    }

    public Diarista findByID(String value) {
        Long id = ValidationParameter.validate(value);
        return diaristaRepository
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
                  .orElseThrow(() -> new RegraDeNegocioException("Diarista não encontrada"));
    }

    private DiaristaDTO findDiarista(DiaristaDTO dto) {
        Diarista diarista = diaristaRepository.findByDiarista(dto.getEmail());
        if (diarista != null) {
            return mapper.map(diarista, DiaristaDTO.class);
        }
        return null;
    }

    public boolean verify(String verificationCode) {
        Diarista diarista = diaristaRepository.findByVerificationCode(verificationCode);

        if (diarista == null || diarista.isEnabled()) {
            return false;
        } else {
            diarista.setVerificationCode("--.--");
            diarista.setEnabled(true);
            diaristaRepository.save(diarista);

            return true;
        }
    }

    private DiaristaDTO findNumeroBi(DiaristaDTO dto) {
        Diarista diarista = diaristaRepository.findByNumeroBi(dto.getNumeroBi());
        if (diarista != null) {
            return mapper.map(diarista, DiaristaDTO.class);
        }
        return null;
    }
}
