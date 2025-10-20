package ao.tcc.projetofinal.jecuz.controllers;

import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaRequest;
import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaResponse;
import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaSOResponse;
import ao.tcc.projetofinal.jecuz.entities.Diarista;
import ao.tcc.projetofinal.jecuz.services.diarista.DiaristaService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/diarista")
public class DiaristaController {

    private final DiaristaService diaristaService;

    @PostMapping(path = "/save")
    public ResponseEntity<DiaristaResponse> save(@RequestBody @Valid DiaristaRequest request) throws ParseException, MessagingException, UnsupportedEncodingException {

        return ResponseEntity.status(HttpStatus.CREATED).body(diaristaService.save(request));
    }

    /*@GetMapping(path = "/list")
    public ResponseEntity<List<Diarista>> findAll() {

        return ResponseEntity.status(HttpStatus.OK).body(diaristaService.listAll());
    }*/

    @GetMapping
    public ResponseEntity<DiaristaSOResponse> findByID(@RequestParam String id) {

        return ResponseEntity.status(HttpStatus.OK).body(diaristaService.findByID(id));
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestParam Long id, @RequestBody DiaristaRequest request) {

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("");
    }

}
