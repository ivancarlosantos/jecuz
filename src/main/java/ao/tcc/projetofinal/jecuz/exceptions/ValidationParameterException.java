package ao.tcc.projetofinal.jecuz.exceptions;

public class ValidationParameterException extends RuntimeException{

    public ValidationParameterException(String message,Throwable throwable){
        super(message, throwable);
    }

    public ValidationParameterException(String message){
        super(message);
    }
}
