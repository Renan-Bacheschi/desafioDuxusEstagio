package br.com.duxusdesafio.exceptions;

public class PeriodoSemDadosException extends RuntimeException {
    public PeriodoSemDadosException() {
        super("Não existem registros de jogos no período informado.");
    }
}