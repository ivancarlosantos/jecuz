package ao.tcc.projetofinal.jecuz.services;

import ao.tcc.projetofinal.jecuz.dto.MunicipioDTO;
import ao.tcc.projetofinal.jecuz.entities.Municipio;
import ao.tcc.projetofinal.jecuz.exceptions.RegraDeNegocioException;
import ao.tcc.projetofinal.jecuz.repositories.MunicipioRepository;
import ao.tcc.projetofinal.jecuz.utils.ValidationParameter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MunicipioService {

    private final MunicipioRepository municipioRepository;
    private final ModelMapper mapper;

    public MunicipioDTO save(MunicipioDTO dto) {

        Municipio municipio = Municipio.builder()
                .cep(dto.getCep())
                .logradouro(dto.getLogradouro())
                .complemento(dto.getComplemento())
                .bairro(dto.getBairro())
                .localidade(dto.getLocalidade())
                .uf(dto.getUf())
                .build();

        municipioRepository.save(municipio);

        return mapper.map(municipio, MunicipioDTO.class);
    }

    public List<MunicipioDTO> list() {
        return municipioRepository
                .findAll()
                .stream()
                .map(m -> mapper.map(m, MunicipioDTO.class))
                .toList();
    }

    public MunicipioDTO findByID(String value) {
        Long id = ValidationParameter.validate(value);
        Municipio municipio = municipioRepository
                .findById(id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Município não encontrado"));

        return mapper.map(municipio, MunicipioDTO.class);
    }

    public MunicipioDTO update(String value, MunicipioDTO dto) {
        Long id = ValidationParameter.validate(value);

        Municipio m = municipioRepository.findById(id).orElseThrow(() -> new RegraDeNegocioException("Município não encontrado"));

        Municipio municipio = Municipio.builder()
                .id(m.getId())
                .cep(dto.getCep())
                .logradouro(dto.getLogradouro())
                .complemento(dto.getComplemento())
                .bairro(dto.getBairro())
                .localidade(dto.getLocalidade())
                .uf(dto.getUf())
                .build();

        municipioRepository.save(municipio);

        return mapper.map(municipio, MunicipioDTO.class);
    }

    public String delete(String value) {
        Long id = ValidationParameter.validate(value);
        municipioRepository.deleteById(id);

        return "MUNICÍPIO REMOVIDO";
    }
}