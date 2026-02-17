package ao.tcc.projetofinal.jecuz.test.integration;

import ao.tcc.projetofinal.jecuz.controllers.TestController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TestController.class, excludeAutoConfiguration = {JacksonAutoConfiguration.class})
@Import(OrdensDeServicoControllerTest.TestConfig.class)
public class TestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testEndpointShouldReturnOk() throws Exception {
        mockMvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.owner", notNullValue()))
                .andExpect(jsonPath("$.dateCreateAt", notNullValue()));
    }
}
