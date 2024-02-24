package ao.tcc.projetofinal.jecuz.utils;

import ao.tcc.projetofinal.jecuz.exceptions.ValidationParameterException;

public class ValidationParameter {

    private ValidationParameter() {}

    public static Long validate(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            throw new ValidationParameterException("Parâmetro Inválido: "+ex.getMessage());
        }
    }
}
