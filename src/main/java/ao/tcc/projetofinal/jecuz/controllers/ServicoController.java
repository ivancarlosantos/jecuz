package ao.tcc.projetofinal.jecuz.controllers;

import ao.tcc.projetofinal.jecuz.dto.FlashMessage;
import ao.tcc.projetofinal.jecuz.dto.ServicoDTO;
import ao.tcc.projetofinal.jecuz.enums.Icone;
import ao.tcc.projetofinal.jecuz.services.ServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/servicos")
@RequiredArgsConstructor
public class ServicoController {
    private final ServicoService servicoService;


    @GetMapping
    public ModelAndView buscarTodos() {
        var modelAndView = new ModelAndView("admin/servico/lista");
        modelAndView.addObject("servicos", servicoService.listAll());
        return modelAndView;
    }

    @GetMapping("/cadastrar")
    public ModelAndView cadastrar() {
        var modelAndView = new ModelAndView("admin/servico/form");
        modelAndView.addObject("form", new ServicoDTO());
        return modelAndView;
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@Valid @ModelAttribute("form") ServicoDTO form, BindingResult result, RedirectAttributes attrs) {
        if (result.hasErrors()) {
            return "admin/servico/form";
        }
        servicoService.save(form);
        attrs.addFlashAttribute("alert", new FlashMessage("alert-success", "Serviço Cadastrado com sucesso!"));
        return "redirect:/admin/servicos";
    }


    @GetMapping("/{id}/editar")
    public ModelAndView editar(@PathVariable Long id) {
        var modelAndView = new ModelAndView("admin/servico/form");
        modelAndView.addObject("form", servicoService.findById(id));
        return modelAndView;
    }

    @PostMapping("/{id}/editar")
    public String editar(@PathVariable Long id, @ModelAttribute("form") @Valid ServicoDTO form, BindingResult result, RedirectAttributes attrs) {
        if (result.hasErrors()) {
            return "admin/servico/form";
        }
        servicoService.editar(form, id);
        attrs.addFlashAttribute("alert", new FlashMessage("alert-success", "Serviço Editado com sucesso!"));

        return "redirect:/admin/servicos";
    }

    @ModelAttribute("icones")
    public Icone[] getIcones() {
        return Icone.values();
    }


    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes attrs) {

        servicoService.excluirPorId(id);
        attrs.addFlashAttribute("alert", new FlashMessage("alert-success", "Serviço Cadastrado com sucesso!"));

        return "redirect:/admin/servicos";
    }
    
}
