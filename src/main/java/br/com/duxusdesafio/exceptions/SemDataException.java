package br.com.duxusdesafio.exceptions;

public class SemDataException extends RuntimeException {
    public SemDataException() {
        super("Necessário informar uma data válida no formato AAAA-MM-DD.");
    }
}