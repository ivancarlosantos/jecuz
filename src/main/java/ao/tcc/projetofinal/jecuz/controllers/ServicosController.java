package ao.tcc.projetofinal.jecuz.controllers;

import ao.tcc.projetofinal.jecuz.dto.servicos.OrdemServicoRequest;
import ao.tcc.projetofinal.jecuz.dto.servicos.OrdemServicoResponse;
import ao.tcc.projetofinal.jecuz.dto.servicos.OrdensDeServicoDTO;
import ao.tcc.projetofinal.jecuz.services.ordens.OrdensDeServicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Controller
@RequestMapping(path = "/api/servicos")
@RequiredArgsConstructor
public class ServicosController {

    private final OrdensDeServicoService service;

    @PostMapping(path = "/os/save")
    public ResponseEntity<OrdemServicoResponse> saveOS(OrdemServicoRequest request) throws ParseException {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
    }
}
