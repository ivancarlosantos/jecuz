package ao.tcc.projetofinal.jecuz.controllers;

import ao.tcc.projetofinal.jecuz.dto.cliente.ClienteDTO;
import ao.tcc.projetofinal.jecuz.services.cliente.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/admin/usuarios")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping(path = "/save")
    public ResponseEntity<ClienteDTO> save(@RequestBody @Valid ClienteDTO dto) throws ParseException {

        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.save(dto));
    }

    @GetMapping
    public ModelAndView buscarTodos(){
        var modelAndView= new ModelAndView("admin/usuario/lista");
        modelAndView.addObject("usuarios", clienteService.listAll());
        return modelAndView;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ClienteDTO> findByID(@PathVariable String id) {

        return ResponseEntity.status(HttpStatus.OK).body(clienteService.findByID(id));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @RequestBody ClienteDTO dto) {

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }
}
