package br.com.sistema_contas.global_exception;

public class CustomAccessDeniedException extends RuntimeException{

    public CustomAccessDeniedException(String message) {
        super(message);
    }
}
