package ao.tcc.projetofinal.jecuz.controllers;

import ao.tcc.projetofinal.jecuz.dto.servicos.OrdemServicoRequest;
import ao.tcc.projetofinal.jecuz.dto.servicos.OrdemServicoResponse;
import ao.tcc.projetofinal.jecuz.dto.servicos.OrdensDeServicoDTO;
import ao.tcc.projetofinal.jecuz.entities.OrdensDeServico;
import ao.tcc.projetofinal.jecuz.services.ordens.OrdensDeServicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/ordem/servico")
public class OrdensDeServicoController {

    private final OrdensDeServicoService ordensDeServicoService;

    @PostMapping(path = "/save")
    public ResponseEntity<OrdemServicoResponse> save(@RequestBody OrdemServicoRequest request) throws ParseException {

        return ResponseEntity.status(HttpStatus.CREATED).body(ordensDeServicoService.save(request));
    }

    @PostMapping(path = "/gerar")
    public ResponseEntity<OrdensDeServico> gerarOS(@RequestParam("idCliente") String idCliente, @RequestParam("idDiarista") String idDiarista, @RequestParam("idOS") String idOS) {

        return ResponseEntity.status(HttpStatus.CREATED).body(ordensDeServicoService.gerarOrdem(idCliente, idDiarista, idOS));
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<OrdensDeServico>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(ordensDeServicoService.findOSAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<OrdensDeServico> findByID(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(ordensDeServicoService.findOS(id));
    }
}
