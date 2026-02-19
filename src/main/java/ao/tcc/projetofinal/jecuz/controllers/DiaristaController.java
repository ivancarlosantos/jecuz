package ao.tcc.projetofinal.jecuz.controllers;

import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaRequest;
import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaResponse;
import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaSOResponse;
import ao.tcc.projetofinal.jecuz.services.diarista.DiaristaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/diarista")
public class DiaristaController {

    private final DiaristaService diaristaService;

    @PostMapping(path = "/save")
    public ResponseEntity<DiaristaResponse> save(@RequestParam("nome")       String nome,
                                                 @RequestParam("nascimento") String nascimento,
                                                 @RequestParam("telefone")   String telefone,
                                                 @RequestParam("numeroBi")   String numeroBi,
                                                 @RequestParam("email")      String email) throws ParseException {

        DiaristaRequest request = new DiaristaRequest(nome, nascimento, telefone, numeroBi, email);

        return ResponseEntity.status(HttpStatus.CREATED).body(diaristaService.save(request));
    }

    @GetMapping
    public ResponseEntity<DiaristaSOResponse> findByID(@RequestParam String id) {

        return ResponseEntity.status(HttpStatus.OK).body(diaristaService.findByID(id));
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestParam Long id, @RequestBody DiaristaRequest request) {

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("");
    }

}
