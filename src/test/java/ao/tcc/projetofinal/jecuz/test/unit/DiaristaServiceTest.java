package ao.tcc.projetofinal.jecuz.test.unit;

import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaRequest;
import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaResponse;
import ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaSOResponse;
import ao.tcc.projetofinal.jecuz.entities.Diarista;
import ao.tcc.projetofinal.jecuz.exceptions.RegraDeNegocioException;
import ao.tcc.projetofinal.jecuz.exceptions.ValidationParameterException;
import ao.tcc.projetofinal.jecuz.repositories.DiaristaRepository;
import ao.tcc.projetofinal.jecuz.services.diarista.DiaristaService;
import ao.tcc.projetofinal.jecuz.services.interfaces.IValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiaristaServiceTest {

    @Mock
    private DiaristaRepository diaristaRepository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private IValidation validation1;

    @Mock
    private IValidation validation2;

    private List<IValidation> validations;

    private DiaristaService diaristaService;

    @Captor
    ArgumentCaptor<Diarista> diaristaCaptor;

    @BeforeEach
    void setup() {
        validations = List.of(validation1, validation2);
        diaristaService = new DiaristaService(validations, diaristaRepository, mapper);
    }

    @Test
    @DisplayName("save should persist and return DiaristaResponse when request is valid")
    void save_happyPath() throws ParseException {
        DiaristaRequest request = new DiaristaRequest();
        request.setNome("Maria");
        request.setNascimento("01/01/1990");
        request.setTelefone("912345678");
        request.setNumeroBi("BI12345");
        request.setEmail("maria@example.com");

        Diarista savedEntity = Diarista.builder()
                .id(1L)
                .nome(request.getNome())
                .nascimento(request.getNascimento())
                .telefone(request.getTelefone())
                .numeroBi(request.getNumeroBi())
                .email(request.getEmail())
                .enabled(false)
                .build();

        when(diaristaRepository.save(any(Diarista.class))).thenReturn(savedEntity);
        when(mapper.map(savedEntity, DiaristaResponse.class)).thenReturn(new DiaristaResponse(request.getNome(), request.getTelefone(), request.getEmail()));

        DiaristaResponse response = diaristaService.save(request);

        // verifications
        verify(validation1, times(1)).execute(Mockito.<ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaRequest>any());
        verify(validation2, times(1)).execute(Mockito.<ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaRequest>any());
        verify(diaristaRepository, times(1)).save(diaristaCaptor.capture());

        Diarista captured = diaristaCaptor.getValue();
        assertEquals(request.getNome(), captured.getNome());
        assertEquals(request.getTelefone(), captured.getTelefone());
        assertEquals(request.getEmail(), captured.getEmail());
        assertEquals(request.getNumeroBi(), captured.getNumeroBi());
        assertFalse(captured.isEnabled());

        assertNotNull(response);
        assertEquals(request.getNome(), response.getNome());
        assertEquals(request.getEmail(), response.getEmail());
    }

    @Test
    @DisplayName("save should throw ParseException when nascimento is invalid")
    void save_invalidNascimento_throwsParseException() {
        DiaristaRequest request = new DiaristaRequest();
        request.setNome("Joao");
        request.setNascimento("invalid-date");
        request.setTelefone("912345678");
        request.setNumeroBi("BI54321");
        request.setEmail("joao@example.com");

        assertThrows(ParseException.class, () -> diaristaService.save(request));

        verify(diaristaRepository, never()).save(any());
    }

    @Test
    @DisplayName("save should propagate RegraDeNegocioException when validation fails")
    void save_validationThrows_shouldPropagate() {
        DiaristaRequest request = new DiaristaRequest();
        request.setNome("Ana");
        request.setNascimento("01/01/1990");
        request.setTelefone("912345678");
        request.setNumeroBi("BI99999");
        request.setEmail("ana@example.com");

        doThrow(new RegraDeNegocioException("erro de negocio")).when(validation1).execute(Mockito.<ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaRequest>any());

        assertThrows(RegraDeNegocioException.class, () -> diaristaService.save(request));

        verify(diaristaRepository, never()).save(any());
        verify(validation1, times(1)).execute(Mockito.<ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaRequest>any());
        // validation2 should not be called because first throws
        verify(validation2, never()).execute(Mockito.<ao.tcc.projetofinal.jecuz.dto.diarista.DiaristaRequest>any());
    }

    @Test
    @DisplayName("findByID should return DiaristaSOResponse when diarista exists")
    void findByID_happyPath() {
        Diarista diarista = Diarista.builder()
                .id(1L)
                .nome("Carlos")
                .build();

        when(diaristaRepository.findById(1L)).thenReturn(Optional.of(diarista));
        when(mapper.map(diarista, DiaristaSOResponse.class)).thenReturn(new DiaristaSOResponse(diarista.getNome(), null));

        DiaristaSOResponse response = diaristaService.findByID("1");

        assertNotNull(response);
        assertEquals("Carlos", response.getNome());
        verify(diaristaRepository, times(1)).findById(1L);
        verify(mapper, times(1)).map(diarista, DiaristaSOResponse.class);
    }

    @Test
    @DisplayName("findByID should throw ValidationParameterException when id is not numeric")
    void findByID_invalidId_throwsValidationParameterException() {
        assertThrows(ValidationParameterException.class, () -> diaristaService.findByID("abc"));
        verify(diaristaRepository, never()).findById(any());
    }

    @Test
    @DisplayName("findByID should throw RegraDeNegocioException when diarista not found")
    void findByID_notFound_throwsRegraDeNegocioException() {
        when(diaristaRepository.findById(42L)).thenReturn(Optional.empty());

        RegraDeNegocioException ex = assertThrows(RegraDeNegocioException.class, () -> diaristaService.findByID("42"));
        assertEquals("Diarista não encontrada", ex.getMessage());
        verify(diaristaRepository, times(1)).findById(42L);
    }
}

