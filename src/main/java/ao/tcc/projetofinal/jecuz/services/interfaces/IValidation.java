package ao.tcc.projetofinal.jecuz.services.interfaces;

import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteRequest;
import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaRequest;

public interface IValidation {

    void execute(ClienteRequest request);

    void execute(DiaristaRequest request);
}
