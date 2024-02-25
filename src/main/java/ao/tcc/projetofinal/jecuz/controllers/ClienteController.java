package ao.tcc.projetofinal.jecuz.controllers;

import ao.tcc.projetofinal.jecuz.dto.ClienteDTO;
import ao.tcc.projetofinal.jecuz.services.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/cliente")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping(path = "/save")
    public ResponseEntity<ClienteDTO> save(@RequestBody @Valid ClienteDTO dto) throws ParseException {

        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.save(dto));
    }

    @GetMapping(path = "/list")
    public ResponseEntity<List<ClienteDTO>> findAll() {

        return ResponseEntity.status(HttpStatus.OK).body(clienteService.listAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ClienteDTO> findByID(@PathVariable String id) {

        return ResponseEntity.status(HttpStatus.OK).body(clienteService.findByID(id));
    }

    @PutMapping(path = "/{idCliente}/diarista/{idDiarista}")
    public ResponseEntity<ClienteDTO> update(@PathVariable(value = "idCliente") String idCliente, @PathVariable(value = "idDiarista") String idDiarista) {

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(clienteService.joinClienteDiarista(idCliente, idDiarista));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @RequestBody ClienteDTO dto) {

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }
}
