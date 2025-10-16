package ao.tcc.projetofinal.jecuz.controllers;

import ao.tcc.projetofinal.jecuz.dto.ordens.OrdensDeServicoDTO;
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
    public ResponseEntity<OrdensDeServicoDTO> save(@RequestBody OrdensDeServicoDTO dto) throws ParseException {

        return ResponseEntity.status(HttpStatus.CREATED).body(ordensDeServicoService.save(dto));
    }

    @PostMapping(path = "/cliente/{idCliente}/diarista/{idDiarista}/os/{idOS}")
    public ResponseEntity<OrdensDeServico> gerarOS(@PathVariable("idCliente") String idCliente, @PathVariable("idDiarista") String idDiarista, @PathVariable("idOS") String idOS) {

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
