package ao.tcc.projetofinal.jecuz.services.istrategy;

import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteRequest;

public interface INewClienteValidation {

    void execute(ClienteRequest request);
}
