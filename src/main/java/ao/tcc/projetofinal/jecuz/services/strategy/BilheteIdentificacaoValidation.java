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
public class BilheteIdentificacaoValidation implements INewClienteValidation {

    private final ClienteRepository repository;

    @Override
    public void execute(ClienteRequest request) {
        log.info("Verificando o bilhete de identificação (BI)");
        if(!request.getNumeroBi().matches("^[0-9]{9}[A-Z]{2}[0-9]{3}$")){
            throw new VerifyFieldsException("O modelo de Bilhete de Identificação (BI) pode estar incorreto, por favor, verifique o campo");
        }

        log.info("Validando se o BI existe");
        if(!isValidBI(request.getNumeroBi())){
            throw new VerifyFieldsException("Já existe um cliente registrado com esse BI");
        }
    }

    private boolean isValidBI(String bi) {
        return repository.findAll()
                         .stream()
                         .noneMatch(cliente -> repository.findById(cliente.getId()).get().getNumeroBi().equals(bi));
    }
}
