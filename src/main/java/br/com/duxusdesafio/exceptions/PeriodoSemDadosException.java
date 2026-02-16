package br.com.duxusdesafio.exceptions;

public class PeriodoSemDadosException extends RuntimeException {
    // Agora ela aceita uma mensagem customizada!
    public PeriodoSemDadosException(String mensagem) {
        super(mensagem);
    }
}