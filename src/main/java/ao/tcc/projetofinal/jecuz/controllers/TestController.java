package ao.tcc.projetofinal.jecuz.controllers;

import ao.tcc.projetofinal.jecuz.entities.ConnectionTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;

@RestController
@RequestMapping(path = "/test")
public class TestController {

    @GetMapping
    public ResponseEntity<ConnectionTest> test() throws UnknownHostException {
        return ResponseEntity.status(HttpStatus.OK).body(ConnectionTest.test());
    }
}
