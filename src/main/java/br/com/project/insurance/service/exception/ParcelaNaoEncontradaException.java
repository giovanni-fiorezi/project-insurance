package br.com.project.insurance.service.exception;

public class ParcelaNaoEncontradaException extends RuntimeException {

    public ParcelaNaoEncontradaException(String message) {
        super(message);
    }
}
