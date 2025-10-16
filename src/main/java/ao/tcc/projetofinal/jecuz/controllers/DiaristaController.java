package ao.tcc.projetofinal.jecuz.controllers;

import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaDTO;
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

    @GetMapping()
    public ModelAndView home() {
        var modelAndView = new ModelAndView("diarista/index");
        modelAndView.addObject("mensage", "Home");
        return modelAndView;
    }

    @GetMapping("/cadastrar")
    public ModelAndView cadastrar(){
        var modelAndView= new ModelAndView("/diarista/login");

        modelAndView.addObject("cadastroDiarista", new DiaristaDTO());

        return modelAndView;
    }

    @PostMapping(path = "/save")
    public ResponseEntity<DiaristaDTO> save(@RequestBody @Valid DiaristaDTO dto) throws ParseException, MessagingException, UnsupportedEncodingException {

        return ResponseEntity.status(HttpStatus.CREATED).body(diaristaService.save(dto));
    }

    @GetMapping(path = "/list")
    public ResponseEntity<List<Diarista>> findAll() {

        return ResponseEntity.status(HttpStatus.OK).body(diaristaService.listAll());
    }

    @GetMapping(path = "/verify")
    public ResponseEntity<String> verify(@Param("code") String code) {
        if (diaristaService.verify(code)){
            return ResponseEntity.status(HttpStatus.OK).body("<h1>SUCESSO - E-mail validado</h1>");
        }else {
            return ResponseEntity.status(HttpStatus.OK).body("<h1>Falha - Diarista e e-mail já validado</h1>");
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Diarista> findByID(@PathVariable String id) {

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
