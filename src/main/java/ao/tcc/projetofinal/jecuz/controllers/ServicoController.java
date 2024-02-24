package ao.tcc.projetofinal.jecuz.controllers;

import ao.tcc.projetofinal.jecuz.dto.FlashMessage;
import ao.tcc.projetofinal.jecuz.dto.ServicoDTO;
import ao.tcc.projetofinal.jecuz.services.ServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/servicos")
public class ServicoController {

private ServicoService servicoService;
    @GetMapping("/cadastrar")
    public ModelAndView cadastrar(){
        var modelAndView= new ModelAndView("admin/servico/form");
        modelAndView.addObject("form", new ServicoDTO());
        return  modelAndView;
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@Valid @ModelAttribute("form") ServicoDTO dto, BindingResult result, RedirectAttributes attrs){
        if (result.hasErrors()){
            return "admin/servico/form";
        }

        servicoService.save(dto);

        attrs.addFlashAttribute("alert", new FlashMessage("alert-success", "Serviço Cadastrado com sucesso!"));

        return "redirect:/admin/servicos";
    }
}
