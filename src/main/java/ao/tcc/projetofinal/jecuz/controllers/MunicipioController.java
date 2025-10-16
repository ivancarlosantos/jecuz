package ao.tcc.projetofinal.jecuz.controllers;

import ao.tcc.projetofinal.jecuz.dto.municipio.MunicipioDTO;
import ao.tcc.projetofinal.jecuz.services.MunicipioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "api/municipio")
public class MunicipioController {

    private final MunicipioService municipioService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<MunicipioDTO> findByID(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(municipioService.findByID(id));
    }

    @GetMapping(path = "/list")
    public ResponseEntity<List<MunicipioDTO>> list() {
        return ResponseEntity.status(HttpStatus.OK).body(municipioService.list());
    }

    @PostMapping(path = "/save")
    public ResponseEntity<MunicipioDTO> save(@RequestBody @Valid MunicipioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(municipioService.save(dto));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<MunicipioDTO> update(@PathVariable(value = "id") String id, @Valid @RequestBody MunicipioDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(municipioService.update(id, dto));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(municipioService.delete(id));
    }
}
