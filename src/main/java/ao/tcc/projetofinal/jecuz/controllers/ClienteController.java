package ao.tcc.projetofinal.jecuz.controllers;

import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteRequest;
import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteResponse;
import ao.tcc.projetofinal.jecuz.services.cliente.ClienteService;
import ao.tcc.projetofinal.jecuz.utils.PageableCommons;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/cliente")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping(path = "/save")
    public ResponseEntity<ClienteResponse> save(@RequestBody @Valid ClienteRequest request) throws ParseException {

        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.save(request));
    }

    @GetMapping(path = "/list")
    public ResponseEntity<PageableCommons<List<ClienteResponse>>> list(@RequestParam(value = "search", required = false)  String search,
                                                                       @RequestParam(value = "page", defaultValue = "0")  Integer page,
                                                                       @RequestParam(value = "size", defaultValue = "10") Integer size) {

        return ResponseEntity.status(HttpStatus.OK).body(clienteService.listAll(search, page, size));
    }

    @GetMapping(path = "/findByID")
    public ResponseEntity<ClienteResponse> findByID(@RequestParam String id) {

        return ResponseEntity.status(HttpStatus.OK).body(clienteService.findByID(id));
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<String> delete(@RequestParam Long id, @RequestBody ClienteResponse dto) {

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }
}
