package ao.tcc.projetofinal.jecuz.controllers;

import ao.tcc.projetofinal.jecuz.dto.servicos.OrdemServicoRequest;
import ao.tcc.projetofinal.jecuz.dto.servicos.OrdemServicoResponse;
import ao.tcc.projetofinal.jecuz.entities.OrdensDeServico;
import ao.tcc.projetofinal.jecuz.services.ordens.OrdensDeServicoService;
import ao.tcc.projetofinal.jecuz.utils.PageableCommons;
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

    @PostMapping(path = "/gerar")
    public ResponseEntity<OrdensDeServico> gerarOS(@RequestParam("idCliente") String idCliente,
                                                   @RequestParam("idDiarista") String idDiarista,
                                                   @RequestBody OrdemServicoRequest request) throws ParseException {

        return ResponseEntity.status(HttpStatus.CREATED).body(ordensDeServicoService.gerarOrdem(idCliente, idDiarista, request));
    }

    @GetMapping(path = "/list")
    public ResponseEntity<PageableCommons<List<OrdemServicoResponse>>> findAll(@RequestParam(value = "search", required = false)  String search,
                                                                               @RequestParam(value = "page", defaultValue = "0")  Integer page,
                                                                               @RequestParam(value = "size", defaultValue = "10") Integer size) {

        return ResponseEntity.status(HttpStatus.OK).body(ordensDeServicoService.listOS(search, page, size));
    }

    @GetMapping
    public ResponseEntity<OrdensDeServico> findByID(@RequestParam String id) {
        return ResponseEntity.status(HttpStatus.OK).body(ordensDeServicoService.findOS(id));
    }
}
