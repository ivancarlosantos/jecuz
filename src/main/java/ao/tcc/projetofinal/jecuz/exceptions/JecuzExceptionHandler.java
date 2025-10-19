package ao.tcc.projetofinal.jecuz.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.util.Date;

@RestControllerAdvice
public class JecuzExceptionHandler {

    @ExceptionHandler(RegraDeNegocioException.class)
    ProblemDetail exceptionHandler(RegraDeNegocioException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
        problemDetail.setType(URI.create(""));
        problemDetail.setTitle(HttpStatus.NOT_FOUND.toString());
        problemDetail.setProperty("timestamp", new Date().toString());
        return problemDetail;
    }

    @ExceptionHandler(DataViolationException.class)
    ProblemDetail exceptionHandler(DataViolationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getLocalizedMessage());
        problemDetail.setType(URI.create(""));
        problemDetail.setTitle(HttpStatus.CONFLICT.toString());
        problemDetail.setProperty("timestamp", new Date().toString());
        return problemDetail;
    }

    @ExceptionHandler(ValidationParameterException.class)
    ProblemDetail exceptionHandler(ValidationParameterException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
        problemDetail.setType(URI.create(""));
        problemDetail.setTitle(HttpStatus.BAD_REQUEST.toString());
        problemDetail.setProperty("timestamp", new Date().toString());
        return problemDetail;
    }

    @ExceptionHandler(VerifyFieldsException.class)
    ProblemDetail exceptionHandler(VerifyFieldsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
        problemDetail.setType(URI.create(""));
        problemDetail.setTitle(HttpStatus.BAD_REQUEST.toString());
        problemDetail.setProperty("timestamp", new Date().toString());
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ProblemDetail exceptionHandler(MethodArgumentTypeMismatchException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
        problemDetail.setType(URI.create(""));
        problemDetail.setTitle(HttpStatus.BAD_REQUEST.toString());
        problemDetail.setProperty("timestamp", new Date().toString());
        return problemDetail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail exceptionHandler(IllegalArgumentException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
        problemDetail.setType(URI.create(""));
        problemDetail.setTitle(HttpStatus.BAD_REQUEST.toString());
        problemDetail.setProperty("timestamp", new Date().toString());
        return problemDetail;
    }
}
