package br.com.duxusdesafio.exceptions;

import java.time.LocalDate;

public class TimeNaoEncontradoException extends RuntimeException {
    public TimeNaoEncontradoException(LocalDate data) {

        super("Nenhum time foi escalado para a data: " + data);
    }
}