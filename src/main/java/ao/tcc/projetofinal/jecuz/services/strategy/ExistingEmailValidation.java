package ao.tcc.projetofinal.jecuz.services.strategy;

import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteRequest;
import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaRequest;
import ao.tcc.projetofinal.jecuz.exceptions.VerifyFieldsException;
import ao.tcc.projetofinal.jecuz.repositories.ClienteRepository;
import ao.tcc.projetofinal.jecuz.repositories.DiaristaRepository;
import ao.tcc.projetofinal.jecuz.services.interfaces.IValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ExistingEmailValidation implements IValidation {

    private final ClienteRepository repository;
    private final DiaristaRepository diaristaRepository;

    @Override
    public void execute(ClienteRequest request) {
        log.info("Validando se e-mail existe");
        if(!isValidEmail(request.getEmail())){
            throw new VerifyFieldsException("Já existe um usuario registrado com esse e-mail");
        }
    }

    @Override
    public void execute(DiaristaRequest request) {
        log.info("Validando se e-mail existe!");
        if(!isValidDiaristaEmail(request.getEmail())){
            throw new VerifyFieldsException("Já existe uma diarista registrada com esse e-mail");
        }
    }

    private boolean isValidEmail(String email){
        return repository.findAll()
                         .stream()
                         .noneMatch(cliente -> repository.findById(cliente.getId()).get().getEmail().equals(email));
    }

    private boolean isValidDiaristaEmail(String email){
        return diaristaRepository.findAll()
                                 .stream()
                                 .noneMatch(diarista -> diaristaRepository.findById(diarista.getId()).get().getEmail().equals(email));
    }
}
