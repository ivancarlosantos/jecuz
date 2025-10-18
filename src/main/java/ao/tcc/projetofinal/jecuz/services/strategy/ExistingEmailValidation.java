package ao.tcc.projetofinal.jecuz.services.strategy;

import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteRequest;
import ao.tcc.projetofinal.jecuz.exceptions.VerifyFieldsException;
import ao.tcc.projetofinal.jecuz.repositories.ClienteRepository;
import ao.tcc.projetofinal.jecuz.services.istrategy.INewClienteValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ExistingEmailValidation implements INewClienteValidation {

    private final ClienteRepository repository;

    @Override
    public void execute(ClienteRequest request) {
        log.info("Validando se e-mail existe");
        if(!isValidEmail(request.getEmail())){
            throw new VerifyFieldsException("Ja existe um usuario registrado com esse e-mail");
        }
    }

    private boolean isValidEmail(String email){
        return repository.findAll()
                         .stream()
                         .noneMatch(users -> repository.findById(users.getId()).get().getEmail().equals(email));
    }
}
