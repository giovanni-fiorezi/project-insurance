package br.com.project.insurance.service.exception;

public class FalhaAtualizacaoApoliceException extends RuntimeException{

    public FalhaAtualizacaoApoliceException(String message) {
        super(message);
    }

    public FalhaAtualizacaoApoliceException(String message, Throwable cause) {
        super(message, cause);
    }
}
