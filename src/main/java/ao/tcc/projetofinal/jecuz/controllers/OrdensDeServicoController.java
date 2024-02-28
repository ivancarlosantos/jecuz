package ao.tcc.projetofinal.jecuz.controllers;

import ao.tcc.projetofinal.jecuz.dto.OrdensDeServicoDTO;
import ao.tcc.projetofinal.jecuz.entities.OrdensDeServico;
import ao.tcc.projetofinal.jecuz.services.OrdensDeServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/ordem/servico")
public class OrdensDeServicoController {

    private final OrdensDeServicoService ordensDeServicoService;

    @PostMapping(path = "/cliente/{idCliente}/diarista/{idDiarista}")
    public ResponseEntity<OrdensDeServicoDTO> gerarOrdem(@PathVariable("idCliente") Long idCliente, @PathVariable("idDiarista") Long idDiarista, @RequestBody @Valid OrdensDeServicoDTO dto) throws ParseException {

        return ResponseEntity.status(HttpStatus.CREATED).body(ordensDeServicoService.gerarOrdem(idCliente, idDiarista, dto));
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<OrdensDeServicoDTO>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(ordensDeServicoService.findOSAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<OrdensDeServico> findByID(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(ordensDeServicoService.findOS(id));
    }
}
