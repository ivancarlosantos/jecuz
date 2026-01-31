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
public class TelemovelValidation implements IValidation {

    private final ClienteRepository clienteRepository;
    private final DiaristaRepository diaristaRepository;

    @Override
    public void execute(ClienteRequest request) {
        log.info("Verificando a formatação número de telemóvel");

        if(!request.getTelefone().matches("^\\+244\\s?(?:\\d\\s?){9}$") && request.getTelefone().length() != 13){
            throw new VerifyFieldsException("O formato de número de telemóvel pode estar incorreto, por favor, verifique o campo");
        }

        log.info("Validando se o Telemóvel existe");
        if(!isValidTelemovelCliente(request.getTelefone())){
            throw new VerifyFieldsException("Já existe um cliente registrado com esse Telemóvel");
        }
    }

    @Override
    public void execute(DiaristaRequest request) {
        log.info("Verificando a formatação número de telemóvel!");

        if(!request.getTelefone().matches("^\\+244\\s?(?:\\d\\s?){9}$") && request.getTelefone().length() != 13){
            throw new VerifyFieldsException("O formato do número de telemóvel pode estar incorreto, por favor, verifique o campo");
        }

        log.info("Validando se o Telemóvel existe!");
        if(!isValidTelemovelDiarista(request.getTelefone())){
            throw new VerifyFieldsException("Já existe uma diarista registrado com esse Telemóvel");
        }
    }

    private boolean isValidTelemovelDiarista(String cel) {
        return diaristaRepository.findAll()
                                 .stream()
                                 .noneMatch(diarista -> diaristaRepository.findById(diarista.getId()).get().getTelefone().equals(cel));
    }

    private boolean isValidTelemovelCliente(String cel) {
        return clienteRepository.findAll()
                                .stream()
                                .noneMatch(diarista -> clienteRepository.findById(diarista.getId()).get().getTelefone().equals(cel));
    }
}
