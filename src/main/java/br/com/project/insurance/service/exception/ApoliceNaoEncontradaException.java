package br.com.project.insurance.service.exception;

public class ApoliceNaoEncontradaException extends RuntimeException {

    public ApoliceNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
