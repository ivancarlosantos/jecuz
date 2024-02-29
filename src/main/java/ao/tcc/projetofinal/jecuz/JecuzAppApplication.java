package ao.tcc.projetofinal.jecuz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;

@Slf4j
@SpringBootApplication
public class JecuzAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(JecuzAppApplication.class, args);
        log.info("Application UP on status: " + HttpStatus.OK);
    }
}
