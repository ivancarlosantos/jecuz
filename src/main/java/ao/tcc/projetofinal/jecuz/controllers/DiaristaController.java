package ao.tcc.projetofinal.jecuz.controllers;

import ao.tcc.projetofinal.jecuz.dto.DiaristaDTO;
import ao.tcc.projetofinal.jecuz.services.DiaristaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/diarista")
public class DiaristaController {

    private final DiaristaService diaristaService;

    @PostMapping(path = "/save")
    public ResponseEntity<DiaristaDTO> save(@RequestBody DiaristaDTO dto) throws ParseException {

        return ResponseEntity.status(HttpStatus.CREATED).body(diaristaService.save(dto));
    }

    @GetMapping(path = "/list")
    public ResponseEntity<List<DiaristaDTO>> findAll() {

        return ResponseEntity.status(HttpStatus.OK).body(diaristaService.listAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<DiaristaDTO> findByID(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK).body(diaristaService.findByID(id));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody DiaristaDTO dto) {

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("");
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @RequestBody DiaristaDTO dto) {

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }
}
