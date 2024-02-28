package ao.tcc.projetofinal.jecuz.controllers;

import ao.tcc.projetofinal.jecuz.dto.ClienteDTO;
import ao.tcc.projetofinal.jecuz.entities.Cliente;
import ao.tcc.projetofinal.jecuz.services.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;

@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/admin/usuarios")
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping("/cadastrar")
    public ModelAndView cadastrar(){
        var modelAndView= new ModelAndView("admin/usuario/cadastro_form");
        modelAndView.addObject("cadastroFormulario", new ClienteDTO());

        return modelAndView;
    }

    /*@PostMapping("/cadastrar")
    public String cadastrar(@Valid @ModelAttribute("cadastroFormulario") ClienteDTO usuarioForm, BindingResult result, RedirectAttributes attrs) {
        if (result.hasErrors()) {
            return "admin/usuario/cadastro_form";
        }
        try {
            clienteService.save(usuarioForm);

            attrs.addFlashAttribute("alert", new FlashMessage("alert-success", "Usuário cadastrado com sucesso!"));

        } catch (RegraDeNegocioException e) {
            //result.addError(e.getFieldError());
            return "admin/usuario/cadastro_form";
        } catch (ParseException e) {
            throw new RegraDeNegocioException(e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }*/

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

    @PostMapping(path = "/{idCliente}/diarista/{idDiarista}")
    public ResponseEntity<Cliente> update(@PathVariable(value = "idCliente") String idCliente, @PathVariable(value = "idDiarista") String idDiarista) {

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(clienteService.joinClienteDiarista(idCliente, idDiarista));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @RequestBody ClienteDTO dto) {

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }
}
