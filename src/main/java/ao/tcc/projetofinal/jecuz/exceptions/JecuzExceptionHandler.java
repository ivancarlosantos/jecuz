package ao.tcc.projetofinal.jecuz.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class JecuzExceptionHandler {

    @ExceptionHandler(value = {RegraDeNegocioException.class})
    public ResponseEntity<ExceptionMessage> handlerExceptionBadRequest(RuntimeException ex) {

        ExceptionMessage exceptionMessage = new ExceptionMessage(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND,
                new Date(),
                List.of(ex.getMessage()));

        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ExceptionMessage> handlerExceptionConflict(IllegalArgumentException ex) {

        ExceptionMessage exceptionMessage = new ExceptionMessage(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST,
                new Date(),
                List.of(ex.getMessage()));

        return new ResponseEntity<>(exceptionMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ValidationParameterException.class})
    public ResponseEntity<ExceptionMessage> handlerExceptionValidationParameter(ValidationParameterException ex) {

        ExceptionMessage exceptionMessage = new ExceptionMessage(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST,
                new Date(),
                List.of(ex.getMessage()));

        return new ResponseEntity<>(exceptionMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {DataViolationException.class})
    public ResponseEntity<ExceptionMessage> handlerExceptionExistData(DataViolationException ex) {

        ExceptionMessage exceptionMessage = new ExceptionMessage(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT,
                new Date(),
                List.of(ex.getMessage()));

        return new ResponseEntity<>(exceptionMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ExceptionMessage> handlerExceptionFormatNumber(MethodArgumentTypeMismatchException ex) {

        ExceptionMessage exceptionMessage = new ExceptionMessage(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST,
                new Date(),
                List.of(ex.getLocalizedMessage()));

        return new ResponseEntity<>(exceptionMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionMessage> handlerFieldException(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        ExceptionMessage exceptionMessage = new ExceptionMessage(
                ex.getStatusCode().value(),
                HttpStatus.BAD_REQUEST,
                new Date(),
                List.of(errors.toString()));

        return new ResponseEntity<>(exceptionMessage, HttpStatus.BAD_REQUEST);
    }
}
